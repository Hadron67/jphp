package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zarray;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class AddArrayItemIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval val = vm.pop();
        Zval array = Tool.fullyDeRef(vm.pop());
        if(array instanceof Zarray){
            ((Zarray) array).addItem(val);
            vm.push(val);
        }
        else{
            vm.makeError("not an array");
        }
    }

    @Override
    public String toString() {
        return "ADD_ARRAY_ITEM";
    }
}
