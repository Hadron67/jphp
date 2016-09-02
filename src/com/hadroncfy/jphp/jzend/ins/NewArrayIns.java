package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zarray;

/**
 * Created by cfy on 16-9-1.
 */
public class NewArrayIns implements Instruction {
    @Override
    public void exec(VM vm) {
        vm.push(new Zarray());
    }

    @Override
    public String toString() {
        return "NEW_ARRAY";
    }
}
