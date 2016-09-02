package com.hadroncfy.jphp.jzend.ins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by cfy on 16-9-1.
 */

@Documented
@Target({ElementType.TYPE})
public @interface ExplicitTypeInstruction {
    //this instruction need always to dereference of the objects
    //do the calculations
}
