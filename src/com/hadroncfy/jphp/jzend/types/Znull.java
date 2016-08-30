package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-13.
 */
public class Znull extends Zval{
    public static final Znull NULL = new Znull();
    private Znull(){

    }
    @Override
    public String getTypeName() {
        return "null";
    }

    @Override
    public String dump() {
        return "NULL";
    }

    @Override
    public Zbool boolCast() {
        return Zbool.FALSE;
    }

    @Override
    public Zstring stringCast() {
        return new Zstring("");
    }

    @Override
    public Zarray arrayCast() {
        return new Zarray();
    }
}
