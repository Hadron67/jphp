package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class EvalIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        //TODO: eval
    }

    @Override
    public String toString() {
        return "EVAL";
    }
}
