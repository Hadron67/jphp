package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class StoreIns implements Instruction {
    public int index;
    public StoreIns(int index){
        this.index = index;
    }

    @Override
    public void exec(VM vm) {
        vm.store(vm.pop(),index);
    }

    @Override
    public String toString() {
        return "TSTORE " + index;
    }
}
