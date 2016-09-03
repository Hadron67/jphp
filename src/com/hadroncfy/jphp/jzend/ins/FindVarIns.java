package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zref;

/**
 * Created by cfy on 16-9-1.
 */
public class FindVarIns implements ReferencableIns {
    public String vname;
    public boolean isRef = false;
    public FindVarIns(String vname,boolean isRef){
        this.vname = vname;
        this.isRef = isRef;
    }
    public FindVarIns(String vname){
        this(vname,false);
    }
    @Override
    public void exec(VM vm) {
        Zref val = vm.getEnv().getVar(vname);
        if(val != null){

            vm.push(isRef ? val : val.deRef());
        }
        else if(!isRef){
            vm.getEnv().makeNotice("Undefined variable: " + vname);
        }
    }

    @Override
    public String toString() {
        return "FIND_VAR " + (isRef ? "&" : "") + vname;
    }

    @Override
    public void convertToLvalue() {
        isRef = true;
    }

    @Override
    public void convertToRvalue() {
        isRef = false;
    }
}
