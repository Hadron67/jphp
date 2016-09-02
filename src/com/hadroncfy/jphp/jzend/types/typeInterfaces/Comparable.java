package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-1.
 */
public interface Comparable {
    Zval lessThan(Zval zval);
    Zval moreThan(Zval zval);
    Zval equal(Zval zval);
    Zval identical(Zval zval);
}
