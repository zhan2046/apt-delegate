
Android APT 技术浅谈
===============


安卓AOP三剑客: APT, AspectJ, Javassist
-----

![Apt、AspectJ、Javassisit](http://upload-images.jianshu.io/upload_images/751860-0641778f0bc265ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


Android APT
-----

>APT(Annotation Processing Tool 的简称)，可以在代码编译期解析注解，并且生成新的 Java 文件，减少手动的代码输入。现在有很多主流库都用上了 APT，比如 Dagger2, ButterKnife, EventBus3 等



代表框架：

* DataBinding
* Dagger2
* ButterKnife
* EventBus3
* DBFlow
* AndroidAnnotation


项目例子
-----

[T-MVP](https://github.com/north2016/T-MVP)



使用姿势
-----

1，在android工程中，创建一个java的Module，写一个类继承AbstractProcessor

```java

	@AutoService(Processor.class) // javax.annotation.processing.IProcessor
	@SupportedSourceVersion(SourceVersion.RELEASE_7) //java
	@SupportedAnnotationTypes({ // 标注注解处理器支持的注解类型
	    "com.annotation.SingleDelegate",
	    "com.annotation.MultiDelegate"
	})
	public class AnnotationProcessor extends AbstractProcessor {
	
	public static final String PACKAGE = "com.poet.delegate";
	public static final String CLASS_DESC = "From poet compiler";
	
	public Filer filer; //文件相关的辅助类
	public Elements elements; //元素相关的辅助类
	public Messager messager; //日志相关的辅助类
	public Types types;
	
	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
	    filer = processingEnv.getFiler();
	    elements = processingEnv.getElementUtils();
	    messager = processingEnv.getMessager();
	    types = processingEnv.getTypeUtils();
	
	    new SingleDelegateProcessor().process(set, roundEnvironment, this);
	    new MultiDelegateProcessor().process(set, roundEnvironment, this);
	
	    return true;
	}
	}
```


2，在继承AbstractProcessor类中的process方法，处理我们自定义的注解，生成代码：


```java

	public class SingleDelegateProcessor implements IProcessor {
	
	@Override
	public void process(Set<? extends TypeElement> set, RoundEnvironment roundEnv,
	                AnnotationProcessor abstractProcessor) {
	// 查询注解是否存在
	Set<? extends Element> elementSet =
	        roundEnv.getElementsAnnotatedWith(SingleDelegate.class);
	Set<TypeElement> typeElementSet = ElementFilter.typesIn(elementSet);
	if (typeElementSet == null || typeElementSet.isEmpty()) {
	    return;
	}
	
	// 循环处理注解
	for (TypeElement typeElement : typeElementSet) {
	    if (!(typeElement.getKind() == ElementKind.INTERFACE)) { // 只处理接口类型
	        continue;
	    }
	
	    // 处理 SingleDelegate，只处理 annotation.classNameImpl() 不为空的注解
	    SingleDelegate annotation = typeElement.getAnnotation(SingleDelegate.class);
	    if ("".equals(annotation.classNameImpl())) {
	        continue;
	    }
	    Delegate delegate = annotation.delegate();
	
	    // 添加构造器
	    MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
	            .addModifiers(Modifier.PUBLIC);
	
	    // 创建类名相关 class builder
	    TypeSpec.Builder builder =
	            ProcessUtils.createTypeSpecBuilder(typeElement, annotation.classNameImpl());
	
	    // 处理 delegate
	    builder = ProcessUtils.processDelegate(typeElement, builder,
	            constructorBuilder, delegate);
	
	    // 检查是否继承其它接口
	    builder = processSuperSingleDelegate(abstractProcessor, builder, constructorBuilder, typeElement);
	
	    // 完成构造器
	    builder.addMethod(constructorBuilder.build());
	
	    // 创建 JavaFile
	    JavaFile javaFile = JavaFile.builder(AnnotationProcessor.PACKAGE, builder.build()).build();
	    try {
	        javaFile.writeTo(abstractProcessor.filer);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	}
```

3，在项目Gradle中添加 annotationProcessor project 引用


```java

	compile project(':apt-delegate-annotation')
	annotationProcessor project(':apt-delegate-compiler')
```


4，如果有自定义注解的话，创建一个java的Module，专门放入自定义注解。项目与apt Module都需引用自定义注解Module

4-1，主工程：

```java

	compile project(':apt-delegate-annotation')
	annotationProcessor project(':apt-delegate-compiler')
```

4-2，apt Module：

```java

	compile project(':apt-delegate-annotation')
	
	compile 'com.google.auto.service:auto-service:1.0-rc2'
	compile 'com.squareup:javapoet:1.4.0'
```

5，生成的源代码在build/generated/source/apt下可以看到


![](http://upload-images.jianshu.io/upload_images/751860-efc9f4bdf0aa21bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


难点
-----

就apt本身来说没有任何难点可言，难点一在于设计模式和解耦思想的灵活应用，二在与代码生成的繁琐，你可以手动字符串拼接，当然有更高级的玩法用squareup的javapoet，用建造者的模式构建出任何你想要的源代码


优点
-----

它的强大之处无需多言，看代表框架的源码，你可以学到很多新姿势。总的一句话：它可以做任何你不想做的繁杂的工作，它可以帮你写任何你不想重复代码。懒人福利，老司机必备神技，可以提高车速，让你以任何姿势漂移。它可以生成任何源代码供你在任何地方使用，就像剑客的剑，快疾如风，无所不及

>我想稍微研究一下,APT还可以在哪些地方使用，比如：Repository层?

APT在Repository层的尝试
-----

   了解APT与简单学习之后，搭建Repository层时，发现有一些简单，重复模版的代码。

   每一次添加新接口都需要简单地修改很多地方，能不能把一部分代码自动生成，减少改动的次数呢？


知识库Repository层
-----


![](https://raw.githubusercontent.com/ruzhan123/apt-delegate/master/gif/apt-delegate.png)


* IRemoteDataSource， RemoteDataSource

远程数据源，属于网络请求相关

* ILocalDataSource， LocalDataSource

本地数据源，属于本地数据持久化相关

* IRepository，Repository

仓库代理类，代理远程数据源与本地数据源



Repository层APT设计思路
-----

发现在具体实现类中，大多都是以代理类的形式调用：方法中调用代理对象，方法名称与参数，返回值类型都相同。显然可以进行APT的尝试


简单的情况，具体实现类中只有一个代理对象


复杂的情况，有多个代理对象，方法内并有一些变化


>期望结果：

* 把RemoteDataSourceImpl自动化生成
* 把LocalDataSourceImpl自动化生成
* 把RepositoryImpl自动化生成


自定义注解设计
-----

   要想具体实现类自动生成，首先要知道需要什么：

* 方便自动生成java文件的类库
* 自动生成类名字是什么
* 需要注入的代理对象
* 让代理对象代理的方法集

自动生成java文件的类库，可以使用[squareup javapoet](https://github.com/square/javapoet)

自动生成类名字，代理对象，方法集需要通过自定义注解配置参数的形成，在AbstractProcessor中获取



**Delegate**

```java

	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.TYPE)
	public @interface Delegate {
	
	/**
	 * delegate class package
	 */
	String delegatePackage();
	
	/**
	 * delegate class name
	 */
	String delegateClassName();
	
	/**
	 * delegate simple name
	 */
	String delegateSimpleName();
	}
```

**SingleDelegate**

```java

	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.TYPE)
	public @interface SingleDelegate {
	
	/**
	 * impl class name
	 */
	String classNameImpl();
	
	/**
	 * delegate data
	 */
	Delegate delegate();
	}
```

**MultiDelegate**

```java

	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.TYPE)
	public @interface MultiDelegate {
	
	/**
	 * impl class name
	 */
	String classNameImpl();
	
	/**
	 * delegate list
	 */
	Delegate[] Delegates();
	}
```


处理自定义的注解、生成代码
-----


**AnnotationProcessor**

```java

	@AutoService(Processor.class) // javax.annotation.processing.IProcessor
	@SupportedSourceVersion(SourceVersion.RELEASE_7) //java
	@SupportedAnnotationTypes({ // 标注注解处理器支持的注解类型
	    "com.annotation.SingleDelegate",
	    "com.annotation.MultiDelegate"
	})
	public class AnnotationProcessor extends AbstractProcessor {
	
	public static final String PACKAGE = "com.poet.delegate";
	public static final String CLASS_DESC = "From poet compiler";
	
	public Filer filer; //文件相关的辅助类
	public Elements elements; //元素相关的辅助类
	public Messager messager; //日志相关的辅助类
	public Types types;
	
	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
	    filer = processingEnv.getFiler();
	    elements = processingEnv.getElementUtils();
	    messager = processingEnv.getMessager();
	    types = processingEnv.getTypeUtils();
	
	    new SingleDelegateProcessor().process(set, roundEnvironment, this);
	    new MultiDelegateProcessor().process(set, roundEnvironment, this);
	
	    return true;
	}
	}
```


资料参考
-----

[安卓AOP三剑客:APT,AspectJ,Javassist](http://www.jianshu.com/p/dca3e2c8608a)

[apt-delegate](https://github.com/ruzhan123/apt-delegate)

[T-MVP](https://github.com/north2016/T-MVP)

[javapoet](https://github.com/square/javapoet)

[butterknife](https://github.com/JakeWharton/butterknife)