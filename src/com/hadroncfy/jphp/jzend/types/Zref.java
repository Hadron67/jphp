package com.hadroncfy.jphp.jzend.types;


import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-12.
 *
 */
public interface Zref extends Zval {
    Zval assign(Zval src);
    Zval deRef();
}
