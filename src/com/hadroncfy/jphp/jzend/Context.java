package com.hadroncfy.jphp.jzend;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Function;
import com.hadroncfy.jphp.jzend.types.Znull;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

import java.io.PrintStream;
import java.util.*;

/**
 * Created by cfy on 16-8-31.
 */
public class Context {
    private Scope scope = Scope.global();

    public final PrintStream out;

    private Map<String,Zval> consts;

    private boolean error = false;

    private boolean exit = false;

    private Zval finalResult = Znull.NULL;

    private String e_msg;

    public Context(PrintStream out){
        this.out = out;
    }

    public void loadConsts(Map<String,Zval> c){
        consts = c;
    }

    public void loadFunctions(Map<String,? extends Function> func){
        scope.func = func;
    }

    public void makeError(String msg){
        e_msg = msg;
        error = true;
    }

    public void makeNotice(String msg){

    }

    public void makeWarning(String msg){

    }

    public Zref getVar(String name){
        return scope.getVar(name);
    }

    public void addVar(String name,Zval zval){
        scope.addVar(name,zval);
    }

    public void declareGlobal(String name){
        scope.declareGlobal(name);
    }

    public Zval getConst(String name){
        return consts.get(name);
    }

    public Function getFunction(String name){
        return scope.getFunction(name);
    }

    public String getErrorMsg(){
        return e_msg;
    }

    public void exit(Zval val){
        finalResult = val;
        exit = true;
    }

    public boolean hasExit(){
        return exit;
    }

    public boolean isError(){
        return error;
    }

    public void enterScope(){
        scope = scope.subScope();
    }

    public void leaveScope(){
        Scope ret = scope.getParent();
        if(ret != null){
            scope = ret;
        }
    }


}
