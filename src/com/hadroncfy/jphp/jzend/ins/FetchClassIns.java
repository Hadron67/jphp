package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class FetchClassIns implements Instruction {
    public static final int SELF = 0;
    public static final int PARENT = 1;
    public static final int STATIC = 2;

    public int type;

    private static final String[] s = {"self","parent","static"};

    public FetchClassIns(int type){
        this.type = type;
    }

    @Override
    public void exec(VM vm) {
        //TODO:fetch class
        assert false;
    }

    @Override
    public String toString() {
        return "FETCH_CLASS " + s[type];
    }
}
