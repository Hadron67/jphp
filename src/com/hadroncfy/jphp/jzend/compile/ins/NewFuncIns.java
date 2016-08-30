package com.hadroncfy.jphp.jzend.compile.ins;

import com.hadroncfy.jphp.jzend.compile.ZendFunction;

/**
 * Created by cfy on 16-8-23.
 */
public class NewFuncIns extends Instruction {

    public int index;
    public ZendFunction func;

    public NewFuncIns(ZendFunction func) {
        super(Opcode.NEW_FUNCTION);
        this.func = func;
    }

    @Override
    public String toString() {
        return super.toString() + " #" + index;
    }
}
