package com.apt.delegate.source

import com.annotation.Delegate
import com.annotation.SingleDelegate

@SingleDelegate(
        classNameImpl = "RemoteDataSourceImpl",
        delegate = Delegate(
                delegatePackage = "com.apt.delegate.source.delegate",
                delegateClassName = "RemoteDelegate",
                delegateSimpleName = "remoteDelegate"))
interface IRemoteDataSource : IOtherDataSource {

    fun getRemoteUser()

    fun getRemoteUserList()

    fun getRemoteBook()
}
