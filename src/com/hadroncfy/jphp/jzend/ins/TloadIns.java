package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class TloadIns implements Instruction {
    public int i;
    public TloadIns(int i){
        this.i = i;
    }

    @Override
    public void exec(VM vm) {
        vm.push(vm.load(i));
    }

    @Override
    public String toString() {
        return "TLOAD " + i;
    }
}
