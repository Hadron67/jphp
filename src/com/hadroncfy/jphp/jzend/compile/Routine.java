package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.compile.ins.Instruction;
import com.hadroncfy.jphp.jzend.types.Zval;

import java.io.PrintStream;
import java.io.StringReader;
import java.util.*;


/**
 * Created by cfy on 16-8-8.
 */
public class Routine{

    protected List<Instruction> opcodes = new ArrayList<>();
    protected List<ExceptionItem> exceptionTable = new ArrayList<>();;
    protected Routine parent;

    protected Map<String,Integer> functionTable = new HashMap<>();;
    protected LoopTable loopTable = new LoopTable();

    //all the routines of a program shares the same function array.
    protected GlobalScope globalScope = new GlobalScope();

    public static Routine newGlobalRoutine(){
        return new Routine(null);
    }

    protected Routine(Routine parent){
        if(parent != null){
            this.parent = parent;
            this.globalScope = parent.globalScope;
        }
        else {
            this.parent = null;
        }
    }

    protected void addExceptionItem(int from,int to,int target,String typename,String vname){
        ExceptionItem item = new ExceptionItem();
        item.from = from;
        item.to = to;
        item.target = target;
        item.typename = typename;
        item.vname = vname;
        exceptionTable.add(item);
    }

    protected Routine(){
        this(null);
    }

    protected Routine parent(){
        return parent;
    }

    protected ZendFunction newFunction(String fname,boolean isRef){
        ZendFunction func = new ZendFunction(isRef);
        int index = globalScope.functions.size();
        globalScope.functions.add(func);
        functionTable.put(fname,index);
        func.parent = this;
        func.globalScope = this.globalScope;
        return func;
    }

    protected void addIns(Instruction ins){
        opcodes.add(ins);
    }

    protected void addConst(String name,Zval value){
        globalScope.constTable.put(name,value);
    }

    protected Zval getConst(String name){
        return globalScope.constTable.get(name);
    }

    protected int getLine(){
        return opcodes.size();
    }

    protected Instruction getLastIns(){
        return opcodes.get(opcodes.size() - 1);
    }

    protected void dump_self(PrintStream ps){
        ps.println("------------------------------------");
        ps.println("codes:");
        int line = 0;
        for(Instruction ins : opcodes){
            ps.println(new StringBuilder().append(line++).append("    ").append(ins.toString()));
        }
        ps.println("function table:");
        for(Map.Entry<String,Integer> s : functionTable.entrySet()){
            ps.println(s.getKey() + " " + s.getValue());
        }
        ps.println("loopTable:");
        loopTable.dump(ps);
        ps.println("exception table:");
        for(ExceptionItem item : exceptionTable){
            ps.println(item.from + " " + item.to + " " + item.target + " " + item.typename);
        }
        ps.println("====================================");
    }

    public void dump(PrintStream ps){
        int index = 0;
        for(ZendFunction func : globalScope.functions){
            ps.println("function " + index++ + ":");
            func.dump_self(ps);
            ps.println();
        }
        ps.println("main block:");
        dump_self(ps);
        ps.println("constants:");
        ps.println("----------------------------------");
        for(Map.Entry<String,Zval> entry : globalScope.constTable.entrySet()){
            ps.println(entry.getKey() + " : " + entry.getValue().dump());

        }
    }

    class ExceptionItem{
        int from;
        int to;
        int target;
        String typename;
        String vname;
    }

    class GlobalScope{
        public List<ZendFunction> functions = new ArrayList<>();
        public Map<String,Zval> constTable = new HashMap<>();

    }

}
