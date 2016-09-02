package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class DereferenceIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval val = vm.pop();
        if(val instanceof Zref){
            vm.push(((Zref) val).deRef());
        }
        else{
            vm.makeError("cannot dereference a non-reference");
        }
    }

    @Override
    public String toString() {
        return "DEREFERENCE";
    }
}
