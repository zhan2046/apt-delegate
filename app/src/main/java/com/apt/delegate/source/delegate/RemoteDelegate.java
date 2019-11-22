package com.apt.delegate.source.delegate;

import com.apt.delegate.source.IRemoteDataSource;

public class RemoteDelegate implements IRemoteDataSource {

    @Override
    public void getRemoteUser() {
        //do something
    }

    @Override
    public void getRemoteUserList() {
        //do something
    }

    @Override
    public void getRemoteBook() {
        //do something
    }

    @Override
    public Object getOtherData() {
        //do something
        return null;
    }
}
