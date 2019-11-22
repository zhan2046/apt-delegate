package com.apt.delegate.source.delegate

import com.apt.delegate.source.ILocalDataSource

class LocalDelegate : ILocalDataSource {

    override//do something
    val localUser: Any
        get() = ""

    override//do something
    val localUserList: Any
        get() = ""

    override//do something
    val localBook: Any
        get() = ""

    override fun setLocalUserToken() {
        //do something
    }

    override fun setLocalUserName() {
        //do something
    }

    override fun setLocalUserDesc() {
        //do something
    }
}
