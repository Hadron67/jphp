package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class ExitIns implements Instruction {
    @Override
    public void exec(VM vm) {
        vm.getEnv().exit(Tool.fullyDeRef(vm.pop()));
    }

    @Override
    public String toString() {
        return "EXIT";
    }
}
