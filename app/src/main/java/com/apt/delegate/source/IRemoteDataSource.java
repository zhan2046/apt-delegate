package com.apt.delegate.source;

import com.annotation.Delegate;
import com.annotation.SingleDelegate;

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
