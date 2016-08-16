package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-12.
 */
public class Zint extends Zval {
    protected int value;

    public Zint(int value){
        this.value = value;
    }

    @Override
    public String getTypeName() {
        return "int";
    }

    @Override
    public String dump() {
        return "int(" + value + ")";
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("int") || typename.equals("integer");
    }

    @Override
    public int hashCode() {
        return new Integer(value).hashCode();
    }

    @Override
    public Zval pos() {
        return new Zint(value);
    }

    @Override
    public Zval neg() {
        return new Zint(-value);
    }

    @Override
    public Zbool boolCast() {
        return super.boolCast();
    }

    @Override
    public Zint intCast() {
        return new Zint(value);
    }

    @Override
    public Zfloat floatCast() {
        return new Zfloat(value);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring(Integer.toString(value));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Zint){
            return value == ((Zint) obj).value;
        }
        else if(obj instanceof Zfloat){
            return value == ((Zfloat) obj).value;
        }
        else if(obj instanceof Zstring){
            return Integer.toString(value).equals(((Zstring) obj).value);
        }
        else if(obj instanceof Zbool){
            return value != 0 && ((Zbool) obj).value || value == 0 && !((Zbool) obj).value;
        }
        return super.equals(obj);
    }
}
