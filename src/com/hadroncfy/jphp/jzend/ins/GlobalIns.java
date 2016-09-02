package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class GlobalIns implements Instruction {

    public String name;

    public GlobalIns(String n){
        name = n;
    }
    @Override
    public void exec(VM vm) {
        vm.getEnv().declareGlobal(name);
    }

    @Override
    public String toString() {
        return "GLOBAL " + name;
    }
}
