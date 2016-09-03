package com.hadroncfy.jphp.jzend.types.typeInterfaces;

import com.hadroncfy.jphp.jzend.types.Zint;

/**
 * Created by cfy on 16-9-1.
 */
public interface Comparable extends Zval{
    int compareTo(Zval zval);
    boolean identical(Zval val);
}
