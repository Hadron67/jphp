package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.Program;
import com.hadroncfy.jphp.jzend.Context;
import com.hadroncfy.jphp.jzend.ins.Instruction;
import com.hadroncfy.jphp.jzend.ins.NewFunctionIns;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Callable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

import java.io.PrintStream;
import java.util.*;


/**
 * Created by cfy on 16-8-8.
 */
public class Routine implements Callable,Program{

    protected List<Instruction> opcodes = new ArrayList<>();
    protected List<ExceptionItem> exceptionTable = new ArrayList<>();
    protected Routine parent;
    protected boolean hasReturn = false;

    protected Map<String,ZendFunction> functionTable = new HashMap<>();
    protected Map<String,ZendClass> classTable = new HashMap<>();
    protected LoopTable loopTable = new LoopTable();

    //all the routines of a program shares the same function array.
    protected GlobalScope globalScope;

    public static Routine newGlobalRoutine(){
        return new Routine(null);
    }

    public Routine(Routine parent){
        if(parent != null){
            this.parent = parent;
            this.globalScope = parent.globalScope;
        }
        else {
            this.parent = null;
            globalScope = new GlobalScope();
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


    protected Routine newSubRoutine(){
        Routine ret = new Routine();
        ret.parent = this;
        ret.globalScope = globalScope;
        return ret;
    }

    public ZendFunction getFunction(String name){
        ZendFunction ret = null;
        Routine r = this;
        while(r != null){
            if((ret = r.functionTable.get(name)) != null){
                return ret;
            }
            r = r.parent;
        }
        return null;
    }

    public ZendClass getZClass(String cname){
        ZendClass ret = null;
        Routine r = this;
        while(r != null){
            if((ret = r.classTable.get(cname)) != null){
                return ret;
            }
            r = r.parent;
        }
        return null;
    }

    protected void addFunction(ZendFunction func){
        functionTable.put(func.getName(),func);
    }

    protected void addClass(ZendClass clazz){
        classTable.put(clazz.getName(),clazz);
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

    protected void dump_self(Dumper dumper){
        PrintStream ps = dumper.ps;
        ps.println("------------------------------------");
        ps.println("codes:");
        int line = 0;
        for(Instruction ins : opcodes){
            if(ins instanceof NewFunctionIns){
                ((NewFunctionIns) ins).index = dumper.addFunction(((NewFunctionIns) ins).func);
            }
            ps.println(new StringBuilder().append(line++).append("    ").append(ins.toString()));
        }
        if(!functionTable.isEmpty()) {
            ps.println("function table:");
            for (Map.Entry<String, ZendFunction> s : functionTable.entrySet()) {
                ps.println(s.getKey() + " #" + dumper.addFunction(s.getValue()));
            }
        }
        if(!classTable.isEmpty()) {
            ps.println("class table:");
            for (Map.Entry<String, ZendClass> s : classTable.entrySet()) {
                ps.println(s.getKey() + " #" + dumper.addClass(s.getValue()));
            }
        }
        if(!loopTable.isEmpty()) {
            ps.println("loopTable:");
            loopTable.dump(ps);
        }
        if(!exceptionTable.isEmpty()) {
            ps.println("exception table:");
            for (ExceptionItem item : exceptionTable) {
                ps.println(item.from + " " + item.to + " " + item.target + " " + item.typename);
            }
        }
        ps.println("====================================");
    }

    public void dump(PrintStream ps){
        ps.println("main block:");
        Dumper dumper = new Dumper(ps);
        dump_self(dumper);

        if(!globalScope.constTable.isEmpty()) {
            ps.println("constants:");
            ps.println("----------------------------------");
            for (Map.Entry<String, Zval> entry : globalScope.constTable.entrySet()) {
                ps.println(entry.getKey() + " : " + entry.getValue().dump());

            }
        }
        ps.println();

        while(dumper.hasClass()){
            Dumper.IndexedItem<ZendClass> item = dumper.consumeOneClass();
            ps.println("#" + item.getIndex());
            item.getItem().dump_self(dumper);

            ps.println();
        }

        while(dumper.hasFunction()){
            Dumper.IndexedItem<ZendFunction> item = dumper.consumeOneFunc();
            ps.println("#" + item.getIndex());
            item.getItem().dump_self(dumper);

            ps.println();
        }
    }

    @Override
    public Zval call(Context env, Zval[] args) {
        //TODO:call a routine
        return null;
    }

    @Override
    public Instruction getIns(int i) {
        return opcodes.get(i);
    }

    @Override
    public int getSize() {
        return opcodes.size();
    }

    protected void setIns(int index,Instruction newIns){
        opcodes.set(index,newIns);
    }

    @Override
    public Map<String, Zval> getConsts() {
        return globalScope.constTable;
    }

    class ExceptionItem{
        int from;
        int to;
        int target;
        String typename;
        String vname;
    }

    class GlobalScope{
        public Map<String,Zval> constTable = new HashMap<>();

    }

}
