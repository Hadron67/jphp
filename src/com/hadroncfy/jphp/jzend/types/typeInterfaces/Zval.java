package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-8-31.
 */
public interface Zval {
    boolean doTypeCheck(String typename);
    String dump();
    String getTypeName();
    Zval clone();
}
