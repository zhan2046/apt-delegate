package com.apt.delegate.source;

import com.annotation.Delegate;
import com.annotation.SingleDelegate;

@SingleDelegate(
        classNameImpl = "",
        delegate = @Delegate(
                delegatePackage = "com.apt.delegate.source.delegate",
                delegateClassName = "OtherDelegate",
                delegateSimpleName = "otherDelegate"
        ))
public interface IOtherDataSource {

    Object getOtherData();
}
