package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-26.
 */
public class ClassMember<T> {
    protected  T member;


    protected Access access = Access.PUBLIC;
    protected boolean isFinal = false;
    protected boolean isStatic = false;

    public String getHead(){
        return access.toString() + (isStatic ? " static" : " ") + (isFinal ? " final " : " ");
    }
}
