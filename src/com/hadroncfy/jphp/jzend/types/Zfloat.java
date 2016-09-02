package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Number;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-12.
 */
public class Zfloat implements Zval,Number,Concatable {
    protected double value;

    public Zfloat(double v){
        value = v;
    }

    @Override
    public String dump() {
        return "float(" + value + ")";
    }

    @Override
    public String getTypeName() {
        return "float";
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
    public Zval plus(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat(value + ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat(value + ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval minus(Zval zval) {
        return null;
    }

    @Override
    public Zval inc() {
        return new Zfloat(value + 1);
    }

    @Override
    public Zval dec() {
        return new Zfloat(value - 1);
    }

    @Override
    public Zbool boolCast() {
        return Zbool.asZbool(value != 0);
    }

    @Override
    public Zarray arrayCast() {
        return null;
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

    @Override
    public Zval bitAnd(Zval zval) {
        return null;
    }

    @Override
    public Zval bitOr(Zval zval) {
        return null;
    }

    @Override
    public Zval bitXor(Zval zval) {
        return null;
    }

    @Override
    public Zval bitNot() {
        return null;
    }

    @Override
    public Zval lessThan(Zval zval) {
        return null;
    }

    @Override
    public Zval moreThan(Zval zval) {
        return null;
    }

    @Override
    public Zval equal(Zval zval) {
        return null;
    }

    @Override
    public Zval identical(Zval zval) {
        return null;
    }

    @Override
    public Zval multiply(Zval zval) {
        return null;
    }

    @Override
    public Zval divide(Zval zval) {
        return null;
    }

    @Override
    public Zval mod(Zval zval) {
        return null;
    }

    @Override
    public Zval pos() {
        return null;
    }

    @Override
    public Zval neg() {
        return null;
    }

    @Override
    public Zval concat(Zval zval) {
        if(zval instanceof Zstring){
            return new Zstring(value + ((Zstring) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zstring(value + ((Castable) zval).stringCast().value);
        }
        return null;
    }
}
