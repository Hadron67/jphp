package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.compile.ZendFunction;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Function;

/**
 * Created by cfy on 16-9-2.
 */
public class NewFunctionIns implements Instruction {
    public int index;
    public ZendFunction func;

    public NewFunctionIns(ZendFunction func){
        this.func = func;
    }

    @Override
    public void exec(VM vm) {
        vm.push(func);
    }

    @Override
    public String toString() {
        return "NEW_FUNCTION " + index;
    }
}
