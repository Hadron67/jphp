package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.MapIterable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class MapIteratorIns implements Instruction {
    public boolean isRef;

    public MapIteratorIns(boolean isref){
        isRef = isref;
    }

    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        if(val instanceof MapIterable){
            vm.push(((MapIterable) val).mapIterator(isRef));
        }
        else{
            vm.makeError(val.getTypeName() + " is not a map-iterable");
        }
    }

    @Override
    public String toString() {
        return "MAP_ITERATOR " + (isRef ? "&" : "");
    }
}
