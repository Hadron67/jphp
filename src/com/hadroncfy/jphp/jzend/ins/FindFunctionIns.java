package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Function;

/**
 * Created by cfy on 16-9-1.
 */
public class FindFunctionIns implements Instruction {

    public String fname;

    public FindFunctionIns(String s){
        fname = s;
    }
    @Override
    public void exec(VM vm) {
        Function val = vm.getEnv().getFunction(fname);
        if(val != null){
            vm.push(val);
        }
        else{
            vm.makeError("Call to undefined function " + fname + "()");
        }
    }

    @Override
    public String toString() {
        return "FIND_FUNCTION " + fname;
    }
}
