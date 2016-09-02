package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class EmptyIns implements Instruction {
    @Override
    public void exec(VM vm) {
        //TODO:empty()
        assert false;
    }

    @Override
    public String toString() {
        return "EMPTY";
    }
}
