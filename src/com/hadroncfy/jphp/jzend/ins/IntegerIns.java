package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zint;

/**
 * Created by cfy on 16-9-2.
 */
public class IntegerIns implements Instruction {
    public int i;

    public IntegerIns(int num){
        i = num;
    }

    @Override
    public void exec(VM vm) {
        vm.push(new Zint(i));
    }

    @Override
    public String toString() {
        return "INTEGER " + i;
    }
}
