package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-1.
 */
public interface OperatableL1 {
    Zval plus(Zval zval);
    Zval minus(Zval zval);
    Zval inc();
    Zval dec();
}
