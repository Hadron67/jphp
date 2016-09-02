package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-1.
 */
public class FindClassIns  implements Instruction {

    public String cname;

    public FindClassIns(String s){
        cname = s;
    }
    @Override
    public void exec(VM vm) {
        //TODO: find class
        assert false;
    }

    @Override
    public String toString() {
        return "FIND_CLASS " + cname;
    }
}
