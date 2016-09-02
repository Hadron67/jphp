package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zfloat;
import com.hadroncfy.jphp.jzend.types.Zint;

/**
 * Created by cfy on 16-9-1.
 */
public class NumberIns implements Instruction {

    private double n;

    public NumberIns(int i){
        n = i;
    }

    @Override
    public void exec(VM vm) {
        vm.push(new Zfloat(n));
    }

    @Override
    public String toString() {
        return "NUMBER " + n;
    }
}
