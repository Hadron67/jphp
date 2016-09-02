package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class ToStringIns implements Instruction {

    @Override
    public void exec(VM vm) {
        Zval op = Tool.fullyDeRef(vm.pop());
        if(op instanceof Castable){
            vm.push(((Castable) op).stringCast());
        }
        else
            vm.makeError("cannot convert this variable to string.");
    }

    @Override
    public String toString() {
        return "TOSTRING";
    }
}
