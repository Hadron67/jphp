package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */
public class ReturnOrThrowIns implements Instruction {
    public boolean isThrow;

    public ReturnOrThrowIns(boolean isThrow){
        this.isThrow = isThrow;
    }

    @Override
    public void exec(VM vm) {
        Zval val = vm.pop();
        if(isThrow){
            vm.doThrow(val);
        }
        else{
            vm.retour(val);
        }
    }

    @Override
    public String toString() {
        return (isThrow ? "THROW" : "RETURN");
    }
}
