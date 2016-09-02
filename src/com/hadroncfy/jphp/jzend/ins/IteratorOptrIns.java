package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zbool;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.ArrayIterator;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.ZIterator;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class IteratorOptrIns implements Instruction {
    public static final int OPTR_END = 1;
    public static final int OPTR_NEXT = 2;

    private static final String[] s = {"end","next"};

    public int optr;

    public IteratorOptrIns(int optr){
        this.optr = optr;
    }

    @Override
    public void exec(VM vm) {
        Zval iterator = Tool.fullyDeRef(vm.pop());
        if(iterator instanceof ZIterator){
            switch(optr){
                case OPTR_END:
                    vm.push(Zbool.asZbool(((ArrayIterator) iterator).end()));
                    break;
                case OPTR_NEXT:
                    ((ArrayIterator) iterator).next();
                    vm.push(iterator);
                    break;
                default:vm.makeError("unexpected operation number");
            }
        }
        else{
            vm.makeError(iterator.getTypeName() + " is not an iterator");
        }
    }

    @Override
    public String toString() {
        return "ITERATOR_OPTR " + s[optr];
    }
}
