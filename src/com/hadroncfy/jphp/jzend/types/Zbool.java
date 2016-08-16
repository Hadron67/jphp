package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-12.
 */
public class Zbool extends Zval {
    public static final Zbool TRUE = new Zbool(true);
    public static final Zbool FALSE = new Zbool(false);
    protected boolean value;

    private Zbool(boolean v){
        value = v;
    }

    public static Zbool asZbool(boolean value){
        return value ? TRUE : FALSE;
    }
    @Override
    public String getTypeName() {
        return "bool";
    }

    @Override
    public String dump() {
        return "bool(" + (value ? "true" : "false") + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Zval){
            return value == ((Zval) obj).boolCast().value;
        }
        else
            return super.equals(obj);
    }

    @Override
    public Zint intCast() {
        return new Zint(value ? 1 : 0);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring(value ? "1" : "0");
    }

    @Override
    public Zbool boolCast() {
        return asZbool(value);
    }
}
