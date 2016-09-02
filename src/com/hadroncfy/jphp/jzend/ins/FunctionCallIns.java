package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.ZArgPack;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Callable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class FunctionCallIns implements Instruction {
    @Override
    public void exec(VM vm) {
        Zval argPack = vm.pop();
        Zval func = Tool.fullyDeRef(vm.pop());
        Zval ret;
        assert argPack instanceof ZArgPack;

        if(func instanceof Callable){
            ret = ((Callable) func).call(vm.getEnv(),((ZArgPack) argPack).args);
            if(vm.getEnv().isError()){
                return;
            }
        }
        else{
            vm.makeError(func.getTypeName() + "is not a callable");
            return;
        }
        vm.push(ret);
    }

    @Override
    public String toString() {
        return "FUNCTION_CALL";
    }
}
