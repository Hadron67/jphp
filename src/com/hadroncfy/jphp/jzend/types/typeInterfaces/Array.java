package com.hadroncfy.jphp.jzend.types.typeInterfaces;

import com.hadroncfy.jphp.jzend.types.Zref;

/**
 * Created by cfy on 16-9-1.
 */
public interface Array extends Zval{
    Zref subscript(Zval value);
    int size();
}
