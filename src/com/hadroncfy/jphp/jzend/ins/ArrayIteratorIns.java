package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.ArrayIterable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class ArrayIteratorIns implements Instruction {
    public boolean isRef;

    public ArrayIteratorIns(boolean isref){
        isRef = isref;
    }

    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        if(val instanceof ArrayIterable){
            vm.push(((ArrayIterable) val).arrayIterator(isRef));
        }
        else{
            vm.makeError(val.getTypeName() + " is not iterable");
        }
    }

    @Override
    public String toString() {
        return "ARRAY_ITERATOR " + (isRef ? "&" : "");
    }
}
