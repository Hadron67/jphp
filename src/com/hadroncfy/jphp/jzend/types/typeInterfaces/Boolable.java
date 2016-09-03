package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-1.
 */
public interface Boolable {
    Zval and(Zval zval);
    Zval or(Zval zval);
    Zval not();
}
