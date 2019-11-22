package com.apt.delegate.source.delegate

import com.apt.delegate.source.IRemoteDataSource

class RemoteDelegate : IRemoteDataSource {

    override fun getRemoteUser() {
        //do something
    }

    override fun getRemoteUserList() {
        //do something
    }

    override fun getRemoteBook() {
        //do something
    }

    override fun getOtherData(): Any? {
        //do something
        return null
    }
}
