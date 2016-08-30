package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.Zval;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cfy on 16-8-30.
 */
public class Interface extends ZendClass {

    private PreBindings preBindings = new PreBindings();

    private Map<String,Zval> consts = new HashMap<>();

    private Map<String,ClassMember<ZendMethod>> methods = new HashMap<>();

    public Interface(String name){
        cname = name;
    }

    @Override
    protected String getHeadName() {
        return "interface";
    }

    @Override
    protected void addImplement(String cname) throws IllegalClassOperationException {
        throw new IllegalClassOperationException("interfaces cannot implement.",this);
    }

    @Override
    protected void addExtends(String cname) throws IllegalClassOperationException {
        preBindings.parents.add(cname);
    }

    @Override
    protected void addConst(String cname, Zval value) throws IllegalClassOperationException, RedeclareException {
        consts.put(cname,value);
    }

    @Override
    protected void addVar(String name, ClassMember<Zval> var) throws IllegalClassOperationException, RedeclareException {
        throw new IllegalClassOperationException("Interfaces may not include member variables",this);
    }

    @Override
    protected void addMethod(ClassMember<ZendMethod> method) throws RedeclareException, InvalidMethodException {

        methods.put(method.member.getName(),method);

    }

    @Override
    protected void addUse(String tname) throws IllegalClassOperationException {
        throw new IllegalClassOperationException("Cannot use traits inside of interfaces. " + tname + " is used in " + this.cname,this);
    }

    @Override
    protected void addAliasItem(TraitAlias alias) throws IllegalClassOperationException {
        throw new IllegalClassOperationException("interfaces cannot use.",this);
    }

    @Override
    protected void addExcluder(TraitExcluder te) throws IllegalClassOperationException {
        throw new IllegalClassOperationException("interfaces cannot use.",this);
    }

    @Override
    protected boolean finishParsing() throws IllegalModifierException {
        return true;
    }

    @Override
    protected void dump_self(Dumper dumper) {
        PrintStream ps = dumper.ps;
        ps.print(getHeadName() + " ");
        ps.print(cname);
        if(!preBindings.parents.isEmpty()){
            ps.print(" extends ");
            for(String parent : preBindings.parents){
                ps.print(parent);
                ps.print(",");
            }
        }
        ps.println();
        for(Map.Entry<String,Zval> entry : consts.entrySet()){
            ps.print("const ");
            ps.print(entry.getKey());
            ps.print(": ");
            ps.print(entry.getValue().dump());
            ps.println();
        }
        for(Map.Entry<String,ClassMember<ZendMethod>> method : methods.entrySet()){
            ps.print(method.getValue().getHead());
            method.getValue().member.dump_self(dumper);
        }
        ps.println("===================================================");
    }

    @Override
    public boolean isBind() {
        return false;
    }

    private class PreBindings{
        List<String> parents = new ArrayList<>();
    }
}
