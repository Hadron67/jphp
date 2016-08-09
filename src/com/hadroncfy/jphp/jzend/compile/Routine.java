package com.hadroncfy.jphp.jzend.compile;

import java.io.PrintStream;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by cfy on 16-8-8.
 */
public class Routine{

    protected List<Instruction> opcodes;
    protected List<ExceptionItem> exceptionTable;

    protected Map<String,ZendFunction> functions;


    protected Routine(){
        opcodes = new ArrayList<>();
        exceptionTable = new ArrayList<>();
    }

    protected void addIns(Instruction ins){
        opcodes.add(ins);
    }

    protected int getLine(){
        return opcodes.size();
    }


    private void dump_self(int id,PrintStream ps){
        ps.println("block id #" + id);
        ps.println(" codes:");
        int line = 0;
        for(Instruction ins : opcodes){
            ps.println("  " + line + " " + ins.toString());
        }
        ps.println();

    }

    public void dump(PrintStream ps){
        Queue<Routine> rq = new LinkedList<>();
        rq.offer(this);
        int id = 0;
        while(!rq.isEmpty()){
            Routine rt = rq.poll();
            rt.dump_self(id++,ps);

        }
    }

    class ExceptionItem{
        int from;
        int to;
        String typename;
    }

}
