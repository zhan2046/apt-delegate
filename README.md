
apt-delegate
===============



通过接口，自动生成代理实现类。 层与层之间代理模式自动化。



![](https://github.com/ruzhan123/apt-delegate/raw/master/gif/apt-delegate.png)




Usage
-----

**1, 声明接口**


**1-1,ILocalDataSource**

```java

	public interface ILocalDataSource {
	
	    Object getLocalUser();
	
	    Object getLocalUserList();
	
	    Object getLocalBook();
	}
```

**1-2,IRemoteDataSource**

```java

	public interface IRemoteDataSource extends IOtherDataSource {
	
	    void getRemoteUser();
	
	    void getRemoteUserList();
	
	    void getRemoteBook();
	}
```

**1-3,IRepository**

```java

	public interface IRepository extends ILocalDataSource, IRemoteDataSource {
	}
```


**2, 标记代理对象注解**


**2-1, ILocalDataSource**

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

**2-2, IRemoteDataSource**

```java

	@SingleDelegate(
	        classNameImpl = "RemoteDataSourceImpl",
	        delegate = @Delegate(
	                delegatePackage = "com.apt.delegate.source.delegate",
	                delegateClassName = "RemoteDelegate",
	                delegateSimpleName = "remoteDelegate"
	        ))
	public interface IRemoteDataSource extends IOtherDataSource {
	
	    void getRemoteUser();
	
	    void getRemoteUserList();
	
	    void getRemoteBook();
	}
```

**2-3, IRepository**

```java

	@MultiDelegate(
	        classNameImpl = "RepositoryImpl",
	        Delegates = {
	                @Delegate(
	                        delegatePackage = "com.apt.delegate.source",
	                        delegateClassName = "ILocalDataSource",
	                        delegateSimpleName = "localDataSource"
	                ),
	                @Delegate(
	                        delegatePackage = "com.apt.delegate.source",
	                        delegateClassName = "IRemoteDataSource",
	                        delegateSimpleName = "remoteDataSource"
	                )
	        })
	public interface IRepository extends ILocalDataSource, IRemoteDataSource {
	}
```

**3, 编译期，自动生成代理对象实现类**

**3-1, LocalDataSourceImpl**

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

**3-2, RemoteDataSourceImpl**

```java

	/**
	 * From poet compiler */
	public class RemoteDataSourceImpl implements IRemoteDataSource {
	  private RemoteDelegate remoteDelegate;
	
	  private OtherDelegate otherDelegate;
	
	  public RemoteDataSourceImpl(RemoteDelegate remoteDelegate, OtherDelegate otherDelegate) {
	    this.remoteDelegate = remoteDelegate;
	    this.otherDelegate = otherDelegate;
	  }
	
	  @Override
	  public void getRemoteUser() {
	     remoteDelegate.getRemoteUser();
	  }
	
	  @Override
	  public void getRemoteUserList() {
	     remoteDelegate.getRemoteUserList();
	  }
	
	  @Override
	  public void getRemoteBook() {
	     remoteDelegate.getRemoteBook();
	  }
	
	  @Override
	  public Object getOtherData() {
	    return otherDelegate.getOtherData();
	  }
	}
```

**3-3, RepositoryImpl**

```java

	/**
	 * From poet compiler */
	public class RepositoryImpl implements IRepository {
	  private ILocalDataSource localDataSource;
	
	  private IRemoteDataSource remoteDataSource;
	
	  public RepositoryImpl(ILocalDataSource localDataSource, IRemoteDataSource remoteDataSource) {
	    this.localDataSource = localDataSource;
	    this.remoteDataSource = remoteDataSource;
	  }
	
	  @Override
	  public Object getLocalUser() {
	    return localDataSource.getLocalUser();
	  }
	
	  @Override
	  public Object getLocalUserList() {
	    return localDataSource.getLocalUserList();
	  }
	
		......
	}
```

**4, 代码中使用自动生成的类（仓库包含：远程数据源与本地数据源）**

```java

	public class Repository {
	
	private static IRepository INSTANCE;
	
	private Repository() {
	}

	public static IRepository get() {
	    if (INSTANCE == null) {
	        synchronized (Repository.class) {
	            if (INSTANCE == null) {
	                INSTANCE = new RepositoryImpl(
	                        new LocalDataSourceImpl(new LocalDelegate()),
	                        new RemoteDataSourceImpl(new RemoteDelegate(),
	                                new OtherDelegate()));
	            }
	        }
	    }
	    return INSTANCE;
	}
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
