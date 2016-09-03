package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-13.
 */
public class Znull implements Zval,Castable,Concatable {
    public static final Znull NULL = new Znull();

    private Znull(){
    }


    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("null");
    }

    @Override
    public String dump() {
        return "NULL";
    }

    @Override
    public String getTypeName() {
        return "null";
    }

    @Override
    public Zval clone() {
        return this;
    }

    @Override
    public Zbool boolCast() {
        return Zbool.FALSE;
    }

    @Override
    public Zint intCast() {
        return new Zint(0);
    }

    @Override
    public Zfloat floatCast() {
        return new Zfloat(0);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring("");
    }

    @Override
    public Zarray arrayCast() {
        return new Zarray();
    }

    @Override
    public Zval concat(Zval zval) {
        if(zval instanceof Zstring){
            return zval;
        }
        else if(zval instanceof Castable){
            return ((Castable) zval).stringCast();
        }
        return null;
    }
}
