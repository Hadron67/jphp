package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 *
 */

@ExplicitTypeInstruction
public class EchoIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval v = Tool.fullyDeRef(vm.pop());
        if(v instanceof Zstring){
            vm.getEnv().out.print(((Zstring) v).value);
        }
        else{
            vm.makeError(v.getTypeName() + " is not a string");
        }
    }

    @Override
    public String toString() {
        return "ECHO";
    }
}
