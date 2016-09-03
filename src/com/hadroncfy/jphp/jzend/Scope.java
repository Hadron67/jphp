package com.hadroncfy.jphp.jzend;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Function;
import com.hadroncfy.jphp.jzend.types.SimpleRef;
import com.hadroncfy.jphp.jzend.types.Znull;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cfy on 16-9-2.
 */
public class Scope {
    private Scope global;
    private Scope parent;

    private Map<String,Zref> variables = new HashMap<>();

    protected Map<String,? extends Function> func = null;

    private Scope(){}

    public static Scope global(){
        Scope ret = new Scope();
        ret.global = ret;
        return ret;
    }

    protected Scope subScope(){
        Scope ret = new Scope();
        ret.parent = this;
        ret.global = global;
        return ret;
    }

    public Function getFunction(String name){
        Function ret;
        Scope scope = this;
        while(scope != null){
            if((ret = scope.func.get(name)) != null){
                return ret;
            }
            scope = scope.parent;
        }
        return null;
    }

    public Zref getVar(String name){
        return variables.get(name);
    }

    public void addVar(String name,Zval val){
        variables.put(name,new SimpleRef(val));
    }

    public void declareGlobal(String name){
        if(variables.get(name) != null){
            Zref ref;
            if((ref = global.variables.get(name)) == null){
                ref = new SimpleRef(Znull.NULL);
                global.variables.put(name,ref);
            }
            variables.put(name,ref);
        }
    }

    public Scope getParent(){
        return parent;
    }
}
