package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zarray;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class AddArrayMapItemIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval value = vm.pop();
        Zval key = Tool.fullyDeRef(vm.pop());
        Zval array = Tool.fullyDeRef(vm.pop());
        if(!Zarray.checkKeyType(key)){
            vm.getEnv().makeWarning("Illegal offset type");
            return;
        }
        assert array instanceof Zarray;
        ((Zarray) array).addItem(key,value);
    }

    @Override
    public String toString() {
        return "ADD_ARRAY_MAP_ITEM";
    }
}
