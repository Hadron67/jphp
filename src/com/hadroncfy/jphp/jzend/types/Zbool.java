package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-12.
 */
public class Zbool implements Zval,Castable,Concatable {
    public static final Zbool TRUE = new Zbool(true);
    public static final Zbool FALSE = new Zbool(false);
    public boolean value;

    private Zbool(boolean v){
        value = v;
    }

    public static Zbool asZbool(boolean value){
        return value ? TRUE : FALSE;
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("bool") || typename.equals("boolean");
    }

    @Override
    public String dump() {
        return "bool(" + (value ? "true" : "false") + ")";
    }

    @Override
    public String getTypeName() {
        return "bool";
    }

    @Override
    public Zval clone() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Castable){
            return value == ((Castable) obj).boolCast().value;
        }
        else
            return super.equals(obj);
    }

    @Override
    public Zint intCast() {
        return new Zint(value ? 1 : 0);
    }

    @Override
    public Zfloat floatCast() {
        return new Zfloat(value ? 1 : 0);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring(value ? "1" : "0");
    }

    @Override
    public Zbool boolCast() {
        return asZbool(value);
    }

    @Override
    public Zarray arrayCast() {
        return null;
    }

    @Override
    public Zval concat(Zval zval) {
        if(zval instanceof Zstring){
            return new Zstring(value ? "1" : "0" + ((Zstring) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zstring(value ? "1" : "0" + ((Castable) zval).stringCast().value);
        }
        return null;
    }
}
