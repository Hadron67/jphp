package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class FindVarByNameIns implements Instruction {

    public boolean isRef = false;
    public FindVarByNameIns(boolean r){
        isRef = r;
    }
    @Override
    public void exec(VM vm) {
        Zval v = vm.pop();
        Zref ret;
        if(v instanceof Zstring){
            ret = vm.getEnv().getVar(((Zstring) v).value);
        }
        else if(v instanceof Castable){
            ret = vm.getEnv().getVar(((Castable) v).stringCast().value);
        }
        else{
            vm.makeError("cannot convert this object to string");
            return;
        }

        vm.push(isRef ? ret : ret.deRef());
    }

    @Override
    public String toString() {
        return "FIND_VAR_BY_NAME " + (isRef ? "&" : "");
    }
}
