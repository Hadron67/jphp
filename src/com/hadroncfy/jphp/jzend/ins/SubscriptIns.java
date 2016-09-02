package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zint;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Array;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class SubscriptIns implements Instruction {

    public boolean isRef;

    public boolean isMax = false;

    public SubscriptIns(boolean isRef){
        this.isRef = isRef;
    }
    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        Zref ret;
        Zval sub;

        if (val instanceof Array) {
            if(!isMax) {
                sub = Tool.fullyDeRef(vm.pop());
            }
            else{
                sub = new Zint(((Array) val).size());
            }
            ret = ((Array) val).subscript(sub);
        } else {
            vm.makeError("unsupported operation \"[]\"");
            return;
        }


        vm.push(isRef ? ret : ret.deRef());
    }

    @Override
    public String toString() {
        return (isMax ? "MAX_SUBSCRIPT " : "SUBSCRIPT ") + (isRef ? "&" : "");
    }
}
