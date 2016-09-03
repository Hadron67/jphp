package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zbool;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class OptionalGotoIns implements Instruction {
    public int line1;
    public int line2;
    public OptionalGotoIns(int l1,int l2){
        line1 = l1;
        line2 = l2;
    }

    public OptionalGotoIns(){

    }

    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        boolean b;
        if(val instanceof Zbool){
            b = ((Zbool) val).value;
        }
        else if(val instanceof Castable){
            b = ((Castable) val).boolCast().value;
        }
        else{
            vm.makeError(val.getTypeName() + " is not a boolean");
            return;
        }
        if(b){
            vm.jump(line1);
        }
        else{
            vm.jump(line2);
        }
    }

    @Override
    public String toString() {
        return "OPTIONAL_GOTO " + line1 + "," + line2;
    }
}
