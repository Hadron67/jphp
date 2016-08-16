package com.hadroncfy.jphp.jzend.compile.ins;

/**
 * Created by cfy on 16-8-3.
 */
public class DoubleIntIns extends Instruction {
    public int ins1;
    public int ins2;

    public DoubleIntIns(int opcode) {
        this(opcode,0,0);
    }
    public DoubleIntIns(int opcode,int ins1,int ins2){
        super(opcode);
        this.ins1 = ins1;
        this.ins2 = ins2;
    }

    @Override
    public String toString() {
        return super.toString() + " " + ins1 + "," + ins2;
    }
}
