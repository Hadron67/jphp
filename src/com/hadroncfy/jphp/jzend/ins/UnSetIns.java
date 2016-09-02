package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class UnSetIns implements Instruction {
    @Override
    public void exec(VM vm) {
        //TODO:unset()
        assert false;
    }

    @Override
    public String toString() {
        return "UNSET";
    }
}
