package com.apt.delegate.source

import com.apt.delegate.source.delegate.LocalDelegate
import com.apt.delegate.source.delegate.OtherDelegate
import com.apt.delegate.source.delegate.RemoteDelegate
import com.poet.delegate.LocalDataSourceImpl
import com.poet.delegate.RemoteDataSourceImpl
import com.poet.delegate.RepositoryImpl

object Repository {

    private var INSTANCE: IRepository? = null

    fun get(): IRepository {
        if (INSTANCE == null) {
            INSTANCE = RepositoryImpl(
                    LocalDataSourceImpl(LocalDelegate()),
                    RemoteDataSourceImpl(RemoteDelegate(),
                            OtherDelegate()))
        }
        return INSTANCE!!
    }
}
