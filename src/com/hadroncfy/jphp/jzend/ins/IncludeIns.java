package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class IncludeIns implements Instruction {


    public boolean once;
    public boolean require;

    public IncludeIns(boolean once,boolean req){
        this.once = once;
        require = req;
    }

    @Override
    public void exec(VM vm) {
        Zval string = Tool.fullyDeRef(vm.pop());
        String s;
        if(string instanceof Zstring){
            s = ((Zstring) string).value;
        }
        else if(string instanceof Castable){
            s = ((Castable) string).stringCast().value;
        }
        else{
            vm.makeError(string.getTypeName() + " is not a string");
        }
        //TODO:include or require instruction
        assert false;
    }

    @Override
    public String toString() {
        return (require ? "REQUIRE" : "INCLUDE") + (once ? "_ONCE" : "");
    }
}
