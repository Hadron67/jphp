package com.hadroncfy.jphp.jzend.types.typeInterfaces;

import com.hadroncfy.jphp.jzend.types.*;

/**
 * Created by cfy on 16-9-1.
 */
public interface Castable {
    Zint intCast();
    Zfloat floatCast();
    Zstring stringCast();
    Zbool boolCast();
    Zarray arrayCast();
}
