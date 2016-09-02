package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class FindClassFunctionIns implements Instruction{
    public String fname;

    public FindClassFunctionIns(String fname){
        this.fname = fname;
    }

    @Override
    public void exec(VM vm) {
        Zval clazz = vm.pop();
        //TODO:find class method
        assert false;
    }

    @Override
    public String toString() {
        return "FIND_CLASS_FUNTCION " + fname;
    }
}
