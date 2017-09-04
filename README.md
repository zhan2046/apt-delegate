
apt-delegate
===============



通过接口，自动生成代理实现类。 层与层之间代理模式自动化。



![](https://github.com/ruzhan123/apt-delegate/raw/master/gif/apt-delegate.png)




Usage
-----


**1, 声明接口**

```java

	public interface ILocalDataSource {
	
	    Object getLocalUser();
	
	    Object getLocalUserList();
	
	    Object getLocalBook();
	}
```

**2, 标记代理对象注解**

```java

	@SingleDelegate(
	        classNameImpl = "LocalDataSourceImpl",
	        delegate = @Delegate(
	                delegatePackage = "com.apt.delegate.source",
	                delegateClassName = "ILocalDataSource",
	                delegateSimpleName = "localDataSource"
	))
	public interface ILocalDataSource {
	
	    Object getLocalUser();
	
	    Object getLocalUserList();
	
	    Object getLocalBook();
	}
```

**3,  编译期，自动生成代理对象实现类**

```java

	/**
	 * From poet compiler */
	public class LocalDataSourceImpl implements ILocalDataSource {
	  private ILocalDataSource localDataSource;
	
	  public LocalDataSourceImpl(ILocalDataSource localDataSource) {
	    this.localDataSource = localDataSource;
	  }
	
	  @Override
	  public Object getLocalUser() {
	    return localDataSource.getLocalUser();
	  }
	
	  @Override
	  public Object getLocalUserList() {
	    return localDataSource.getLocalUserList();
	  }
	
	  @Override
	  public Object getLocalBook() {
	    return localDataSource.getLocalBook();
	  }
	}
```

Detail
------

**1, 使用squareup javapoet与AbstractProcessor编译期生成代码**

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

**2, 自定义注解**



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

Developed by
-------

 ruzhan - <a href='javascript:'>dev19921116@gmail.com</a>



License
-------

    Copyright 2017 ruzhan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
