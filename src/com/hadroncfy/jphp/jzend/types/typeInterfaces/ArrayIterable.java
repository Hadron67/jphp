package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-2.
 */
public interface ArrayIterable extends Zval {
    ArrayIterator arrayIterator(boolean isRef);
}
