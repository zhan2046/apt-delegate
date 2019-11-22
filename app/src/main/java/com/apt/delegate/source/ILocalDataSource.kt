package com.apt.delegate.source

import com.annotation.Delegate
import com.annotation.SingleDelegate

@SingleDelegate(
        classNameImpl = "LocalDataSourceImpl",
        delegate = Delegate(
                delegatePackage = "com.apt.delegate.source",
                delegateClassName = "ILocalDataSource",
                delegateSimpleName = "localDataSource"))
interface ILocalDataSource {

    val localUser: Any

    val localUserList: Any

    val localBook: Any

    fun setLocalUserToken()

    fun setLocalUserName()

    fun setLocalUserDesc()
}
