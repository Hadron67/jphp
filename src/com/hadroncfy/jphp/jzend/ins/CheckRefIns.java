package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class CheckRefIns implements Instruction {
    @Override
    public void exec(VM vm) {
        if(!(vm.peek() instanceof Zref)){
            vm.makeError("expected a reference here");
        }
    }

    @Override
    public String toString() {
        return "CHECK_REF";
    }
}
