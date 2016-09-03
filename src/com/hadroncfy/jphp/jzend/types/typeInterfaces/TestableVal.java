package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-3.
 */
public interface TestableVal extends Zval {
    void unSet();
    boolean isSet();
}
