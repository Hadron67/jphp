package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-1.
 */
public class GotoIns implements Instruction {
    public int line;

    public GotoIns(int i){
        line = i;
    }

    @Override
    public void exec(VM vm) {
        vm.jump(line);
    }

    @Override
    public String toString() {
        return "GOTO " + line;
    }
}
