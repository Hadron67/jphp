package com.hadroncfy.jphp.jzend.types.typeInterfaces;

/**
 * Created by cfy on 16-9-2.
 */
public interface MapIterator extends ZIterator{
    Zval getKey();
    Zval getValue();
}
