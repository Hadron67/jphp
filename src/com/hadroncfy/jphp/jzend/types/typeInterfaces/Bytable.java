package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-1.
 */
public interface Bytable {
    Zval bitAnd(Zval zval);
    Zval bitOr(Zval zval);
    Zval bitXor(Zval zval);
    Zval bitNot();
    Zval leftShift(Zval zval);
    Zval rightShift(Zval zval);
}
