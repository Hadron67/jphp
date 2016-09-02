package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 *
 * Created by cfy on 16-8-21.
 */
public abstract class ZendClass {

    public enum ClassType{
        CLASS,ABSTRACT_CLASS,TRAIT,FINAL_CLASS,INTERFACE
    }

    protected String cname;

    public String getName(){
        return cname;
    }

    protected static ZendClass createClassByType(String name,ClassType type){
        switch (type){
            case CLASS:return new Class(name);
            case ABSTRACT_CLASS:return new AbstractClass(name);
            case FINAL_CLASS:return new FinalClass(name);
            case INTERFACE:return new Interface(name);
            case TRAIT:return new Trait(name);
            default:throw new IllegalArgumentException("this class is not implemented yet.");
        }
    }

    protected abstract String getHeadName();

    protected abstract void addImplement(String cname) throws IllegalClassOperationException;

    protected abstract void addExtends(String cname) throws IllegalClassOperationException;

    protected abstract void addConst(String cname,Zval value) throws IllegalClassOperationException,RedeclareException;

    protected abstract void addVar(String name,ClassMember<Zval> var) throws IllegalClassOperationException,RedeclareException;

    protected abstract void addMethod(ClassMember<ZendMethod> method) throws RedeclareException,InvalidMethodException;

    protected abstract void addUse(String tname) throws IllegalClassOperationException;

    protected abstract void addAliasItem(TraitAlias alias) throws IllegalClassOperationException;

    protected abstract void addExcluder(TraitExcluder te) throws IllegalClassOperationException;

    protected abstract boolean finishParsing() throws IllegalModifierException;

    protected abstract void dump_self(Dumper dumper);


    public abstract boolean isBind();


}
