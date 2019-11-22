package com.apt.delegate.source;

import com.apt.delegate.source.delegate.LocalDelegate;
import com.apt.delegate.source.delegate.OtherDelegate;
import com.apt.delegate.source.delegate.RemoteDelegate;
import com.poet.delegate.LocalDataSourceImpl;
import com.poet.delegate.RemoteDataSourceImpl;
import com.poet.delegate.RepositoryImpl;

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
