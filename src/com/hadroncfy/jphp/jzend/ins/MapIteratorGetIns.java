package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.MapIterator;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class MapIteratorGetIns implements Instruction {
    public static final int KEY = 0;
    public static final int VALUE = 1;

    private static final String[] s = {"key","value"};

    public int what;

    public MapIteratorGetIns(int what){
        this.what = what;
    }

    @Override
    public void exec(VM vm) {
        Zval it = Tool.fullyDeRef(vm.pop());
        if(it instanceof MapIterator){
            switch(what){
                case KEY:
                    vm.push(((MapIterator) it).getKey());
                    break;
                case VALUE:
                    vm.push(((MapIterator) it).getValue());
                    break;
                default:throw new IllegalArgumentException("Unknown type");
            }
        }
        else{
            vm.makeError(it.getTypeName() + " is not a map iterator");
        }
    }

    @Override
    public String toString() {
        return "MAP_ITERATOR_GET " + s[what];
    }
}
