package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class Tool {
    public static Zval fullyDeRef(Zval zval){
        while(zval instanceof Zref){
            zval = ((Zref) zval).deRef();
        }
        return zval;
    }
}
