package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class FindConstByNameIns implements Instruction {


    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        String s;
        if(val instanceof Zstring){
            s = ((Zstring) val).value;
        }
        else if(val instanceof Castable){
            s = ((Castable) val).stringCast().value;
        }
        else{
            vm.makeError(val.getTypeName() + " is not a string.");
            return;
        }

        Zval cons = vm.getEnv().getConst(s);
        if(cons == null){
            vm.getEnv().makeNotice("Use of undefined const " + s + ",assumed \"" + s + "\"");
            cons = new Zstring(s);
        }
        vm.push(cons);

    }
}
