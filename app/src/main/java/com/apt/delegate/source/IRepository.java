package com.apt.delegate.source;

import com.annotation.Delegate;
import com.annotation.MultiDelegate;

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
