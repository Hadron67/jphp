package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Number;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-12.
 */
public class Zint implements Zval,Number,Concatable {
    protected int value;

    public Zint(int value){
        this.value = value;
    }



    @Override
    public String dump() {
        return "int(" + value + ")";
    }

    @Override
    public String getTypeName() {
        return "int";
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
    public Zval plus(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).toNumber();
        }
        if(zval instanceof Zfloat){
            return new Zfloat(((Zfloat) zval).value + value);
        }
        else if(zval instanceof Castable){
            return new Zint(value + ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval minus(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).toNumber();
        }
        if(zval instanceof Zfloat){
            return new Zfloat(((Zfloat) zval).value - value);
        }
        else if(zval instanceof Castable){
            return new Zint(value - ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval inc() {
        return new Zint(value + 1);
    }

    @Override
    public Zval dec() {
        return new Zint(value - 1);
    }

    @Override
    public Zval multiply(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).toNumber();
        }
        if(zval instanceof Zfloat){
            return new Zfloat(((Zfloat) zval).value * value);
        }
        else if(zval instanceof Castable){
            return new Zint(value * ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval divide(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).toNumber();
        }
        if(zval instanceof Zfloat){
            return new Zfloat(((Zfloat) zval).value / value);
        }
        else if(zval instanceof Castable){
            return new Zint(value / ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval mod(Zval zval) {
        if(zval instanceof Castable){
            return new Zint(value % ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval lessThan(Zval zval) {
        if(zval instanceof Zint){
            return Zbool.asZbool(value < ((Zint) zval).value);
        }
        else if(zval instanceof Zfloat){
            return Zbool.asZbool(value < ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){
            return Zbool.asZbool(value < ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval moreThan(Zval zval) {
        if(zval instanceof Zint){
            return Zbool.asZbool(value > ((Zint) zval).value);
        }
        else if(zval instanceof Zfloat){
            return Zbool.asZbool(value > ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){
            return Zbool.asZbool(value > ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval equal(Zval zval) {
        return null;
    }

    @Override
    public Zval identical(Zval zval) {
        return Zbool.asZbool((zval instanceof Zint) && equals(zval));
    }

    @Override
    public Zval bitAnd(Zval zval) {
        if(zval instanceof Zint){
            return new Zint(value & ((Zint) zval).value);
        }
        else if(zval instanceof Zfloat){
            return new Zfloat(value & (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zint(value & ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval bitOr(Zval zval) {
        if(zval instanceof Zint){
            return new Zint(value | ((Zint) zval).value);
        }
        else if(zval instanceof Zfloat){
            return new Zfloat(value | (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zint(value | ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval bitXor(Zval zval) {
        if(zval instanceof Zint){
            return new Zint(value ^ ((Zint) zval).value);
        }
        else if(zval instanceof Zfloat){
            return new Zfloat(value ^ (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zint(value ^ ((Castable) zval).intCast().value);
        }
        return null;
    }

    @Override
    public Zval bitNot() {
        return new Zint(~value);
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
        return Zbool.asZbool(value != 0);
    }

    @Override
    public Zarray arrayCast() {
        return null;
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
    public Zval concat(Zval zval) {
        if(zval instanceof Zstring){
            return new Zstring(value + ((Zstring) zval).value);
        }
        else if(zval instanceof Castable){
            return new Zstring(value + ((Castable) zval).stringCast().value);
        }
        return null;
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
