package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Znull;

/**
 * Created by cfy on 16-9-1.
 */
public class NullIns implements Instruction {
    @Override
    public void exec(VM vm) {
        vm.push(Znull.NULL);
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
