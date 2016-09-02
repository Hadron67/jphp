package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class FindClassVarIns implements Instruction {
    public boolean isRef;
    public String vname;

    public FindClassVarIns(String name,boolean ref){
        vname = name;
        isRef = ref;
    }

    @Override
    public void exec(VM vm) {
        Zval clazz = Tool.fullyDeRef(vm.pop());
        //TODO:find class variable
        assert false;
    }

    @Override
    public String toString() {
        return "FIND_CLASS_VAR " + (isRef ? "&" : "") + vname;
    }
}
