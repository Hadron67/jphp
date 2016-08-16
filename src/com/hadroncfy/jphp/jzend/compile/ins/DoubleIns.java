package com.hadroncfy.jphp.jzend.compile.ins;

/**
 * Created by cfy on 16-8-3.
 */
public class DoubleIns extends Instruction {
    public double ins;
    public DoubleIns(int opcode) {
        super(opcode);
        ins = 0;
    }
    public DoubleIns(int opcode,double ins){
        super(opcode);
        this.ins = ins;
    }

    @Override
    public String toString() {
        return super.toString() + " " + ins;
    }
}
