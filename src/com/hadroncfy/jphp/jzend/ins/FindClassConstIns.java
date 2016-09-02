package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class FindClassConstIns implements Instruction {
    public String cname;

    public FindClassConstIns(String name){
        cname = name;
    }

    @Override
    public void exec(VM vm) {
        Zval clazz = Tool.fullyDeRef(vm.pop());
        //TODO:find class const
        assert false;
    }

    @Override
    public String toString() {
        return "FIND_CLASS_CONST " + cname;
    }
}
