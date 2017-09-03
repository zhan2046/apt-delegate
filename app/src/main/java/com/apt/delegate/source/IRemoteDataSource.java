package com.apt.delegate.source;

import com.annotation.Delegate;
import com.annotation.SingleDelegate;

/**
 * Created by ruzhan on 2017/9/3.
 */

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
