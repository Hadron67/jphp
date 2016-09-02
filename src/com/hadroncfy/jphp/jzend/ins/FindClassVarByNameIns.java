package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class FindClassVarByNameIns implements Instruction {
    public boolean isRef;

    public FindClassVarByNameIns(boolean ref){
        isRef = ref;
    }

    @Override
    public void exec(VM vm) {
        Zval name = Tool.fullyDeRef(vm.pop());
        Zval clazz = Tool.fullyDeRef(vm.pop());
        String vname;
        if(name instanceof Zstring){
            vname = ((Zstring) name).value;
        }
        else if(name instanceof Castable){
            vname = ((Castable) name).stringCast().value;
        }
        else{
            vm.makeError("cannot convert " + name.getTypeName() + " to string");
        }

        //TODO:find class var by name
        assert false;
    }

    @Override
    public String toString() {
        return "FIND_CLASS_VAR_BY_NAME " + (isRef ? "&" : "");
    }
}
