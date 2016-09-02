package com.hadroncfy.jphp.jzend.types.typeInterfaces;

import com.hadroncfy.jphp.jzend.types.Zbool;
import com.hadroncfy.jphp.jzend.types.Zref;

/**
 * Created by cfy on 16-9-1.
 */
public interface ArrayIterator extends ZIterator {
    Zval get();
}
