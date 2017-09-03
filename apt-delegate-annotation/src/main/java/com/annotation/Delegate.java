package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Delegate {

    /**
     * delegate class package
     */
    String delegatePackage();

    /**
     * delegate class name
     */
    String delegateClassName();

    /**
     * delegate simple name
     */
    String delegateSimpleName();
}
