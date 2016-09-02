package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;

/**
 * Created by cfy on 16-9-1.
 */
public class StringIns implements Instruction {
    public String s;
    public StringIns(String s){
        this.s = s;
    }
    @Override
    public void exec(VM vm) {
        vm.push(new Zstring(s));
    }

    @Override
    public String toString() {
        return "STRING " + s;
    }
}
