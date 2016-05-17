package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-5-12.
 */
public class Instruction implements Opcode{
    public int opcode;

    public String sdata;
    public double ddata;
    public double ddata2;

    public Instruction(int c){
        opcode = c;
    }

    public Instruction(int c,double da){
        opcode = c;
        ddata = da;
    }

    public Instruction(int c,String s){
        opcode = c;
        sdata = s;
    }

    @Override
    public String toString() {
        String s = "";
        if(opcode == NUMBER) s = " " + Double.toString(ddata);
        else if(opcode == GOTO) s = " " + Integer.toString((int)ddata);
        else if(opcode == STRING) s = "  '" + Tools.UnescapeString(sdata) + "'";
        else if(opcode == CONCAT || opcode == PACK_ARG || opcode == Opcode.GOTO || opcode == Opcode.CONDITIONAL_GOTO) s = " " + Integer.toString((int)ddata);
        else if(opcode == FIND_VARIABLE || opcode == FIND_VARIABLE_AS_REFERENCE) s = " " + sdata;
        else if(opcode == FIND_CONST) s = " " + sdata;
        else if(opcode == FIND_FUNCTION ) s = " " + sdata;
        else if(opcode == BEGIN_LOOP) s = " " + (int)ddata + "," + (int)ddata2;
        return ins_names[opcode] + s;
    }
}
