package com.hadroncfy.jphp.jzend.compile.ins;

/**
 * Created by cfy on 16-5-27.
 */
public class Instruction implements Opcode {
    public int opcode;

    public Instruction(int opcode){
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return ins_names[opcode];
    }
}
