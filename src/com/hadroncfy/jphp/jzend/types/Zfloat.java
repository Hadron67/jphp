package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-12.
 */
public class Zfloat extends Zval {
    protected double value;

    public Zfloat(double v){
        value = v;
    }
    @Override
    public String getTypeName() {
        return "float";
    }

    @Override
    public String dump() {
        return "float(" + value + ")";
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("real") || typename.equals("float") || typename.equals("double");
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(this.value);
        return (int)(bits ^ bits >>> 32);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Zfloat){
            return value == ((Zfloat) obj).value;
        }
        else if(obj instanceof Zint){
            return value == ((Zint) obj).value;
        }
        return super.equals(obj);
    }

    @Override
    public Zbool boolCast() {
        if(value == 0){
            return Zbool.FALSE;
        }
        return Zbool.TRUE;
    }

    @Override
    public Zint intCast() {
        return new Zint((int)value);
    }

    @Override
    public Zfloat floatCast() {
        return new Zfloat(value);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring(Double.toString(value));
    }
}
