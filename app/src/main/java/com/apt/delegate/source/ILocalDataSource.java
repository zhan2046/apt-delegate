package com.apt.delegate.source;

import com.annotation.Delegate;
import com.annotation.SingleDelegate;

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

    void setLocalUserToken();

    void setLocalUserName();

    void setLocalUserDesc();
}
