package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-1.
 */
public class CloneIns implements Instruction {
    @Override
    public void exec(VM vm) {
        vm.push(vm.pop().clone());
    }

    @Override
    public String toString() {
        return "CLONE";
    }
}
