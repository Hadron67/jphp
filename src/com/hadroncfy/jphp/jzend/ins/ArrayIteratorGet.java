package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.ArrayIterator;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class ArrayIteratorGet implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval it = Tool.fullyDeRef(vm.pop());
        if(it instanceof ArrayIterator){
            vm.push(((ArrayIterator) it).get());
        }
        else{
            vm.makeError(it.getTypeName() + " is not an array iterator");
        }
    }

    @Override
    public String toString() {
        return "ARRAY_ITERATOR_GET";
    }
}
