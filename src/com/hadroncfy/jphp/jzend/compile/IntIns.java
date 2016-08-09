package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-3.
 */
public class IntIns extends Instruction {
    public int ins;
    public IntIns(int opcode) {
        super(opcode);
        ins = 0;
    }
    public IntIns(int opcode,int ins){
        super(opcode);
        this.ins = ins;
    }

    @Override
    public String toString() {
        return super.toString() + " " + ins;
    }
}
