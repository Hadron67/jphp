package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-1.
 */
public interface OperatableL2 {
    Zval multiply(Zval zval);
    Zval divide(Zval zval);
    Zval mod(Zval zval);
}
