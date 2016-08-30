package com.hadroncfy.jphp.jzend.types;

/**
 * Created by cfy on 16-8-10.
 */
public abstract class Zval {
    public abstract String getTypeName();
    public abstract String dump();

    public boolean doTypeCheck(String typename){
        return typename.equals(getTypeName());
    }

    public Zval fullyDeRef(){
        Zval ret = this;
        while(ret instanceof Zref){
            ret = ((Zref) ret).deRef();
        }
        return ret;
    }

    public Zval plus(Zval zval){
        return null;
    }
    public Zval minus(Zval zval){
        return null;
    }
    public Zval multiply(Zval zval){
        return null;
    }
    public Zval divide(Zval zval){
        return null;
    }
    public Zval mod(Zval zval){
        return null;
    }
    public Zval lessThan(Zval zval){
        return null;
    }
    public Zval moreThan(Zval zval){
        return null;
    }
    public Zval equal(Zval zval){
        return Zbool.asZbool(equals(zval));
    }
    public Zval identical(Zval zval){
        return null;
    }
    public Zref subscript(Zval zval){
        return null;
    }
    public Zval bitAnd(Zval zval){
        return null;
    }
    public Zval bitOr(Zval zval){
        return null;
    }
    public Zval bitXor(Zval zval){
        return null;
    }
    public Zval pos(){
        return intCast();
    }
    public Zval neg(){
        return intCast().neg();
    }
    public Zint intCast(){
        return null;
    }
    public Zfloat floatCast(){
        return null;
    }
    public Zstring stringCast(){
        return null;
    }
    public Zbool boolCast(){
        return null;
    }
    public Zarray arrayCast(){
        return null;
    }
    public Zval clone(){
        try {
            return (Zval)super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
