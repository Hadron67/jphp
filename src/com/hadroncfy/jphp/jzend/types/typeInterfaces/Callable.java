package com.hadroncfy.jphp.jzend.types.typeInterfaces;

import com.hadroncfy.jphp.jzend.Context;

/**
 * Created by cfy on 16-9-1.
 */
public interface Callable {
    Zval call(Context env, Zval[] args);
}
