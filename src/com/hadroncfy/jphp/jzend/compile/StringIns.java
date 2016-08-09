package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-3.
 */
public class StringIns extends Instruction {
    public String ins;
    public StringIns(int opcode) {
        super(opcode);
        ins = null;
    }
    public StringIns(int opcode,String ins){
        super(opcode);
        this.ins = ins;
    }

    @Override
    public String toString() {
        return super.toString() + " '" + ins + "'";
    }
}
