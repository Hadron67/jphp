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
    public Zval clone() {
        return new Zfloat(value);
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
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat(value - ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat(value - ((Castable) zval).floatCast().value);
        }
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
        Zarray ret = new Zarray();
        ret.addItem(new Zfloat(value));
        return ret;
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
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat((int)value & (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat((int)value & (int)((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval bitOr(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat((int)value | (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat((int)value | (int)((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval bitXor(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat((int)value ^ (int)((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat((int)value ^ (int)((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval bitNot() {
        return new Zfloat(~(int)value);
    }

    @Override
    public Zval leftShift(Zval zval) {
        int i = 0;
        if(zval instanceof Zint){
            i = ((Zint) zval).value;
        }
        else if(zval instanceof Zfloat){
            i =(int) ((Zfloat) zval).value;
        }
        else if(zval instanceof Castable){
            i = ((Castable) zval).intCast().value;
        }

        return new Zint((int)value << i);
    }

    @Override
    public Zval rightShift(Zval zval) {
        int i = 0;
        if(zval instanceof Zint){
            i = ((Zint) zval).value;
        }
        else if(zval instanceof Zfloat){
            i =(int) ((Zfloat) zval).value;
        }
        else if(zval instanceof Castable){
            i = ((Castable) zval).intCast().value;
        }

        return new Zint((int)value >> i);
    }

    @Override
    public Zval multiply(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat(value * ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat(value * ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval divide(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat(value / ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat(value / ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval mod(Zval zval) {
        if(zval instanceof Zstring){
            zval = ((Zstring) zval).floatCast();
        }

        if(zval instanceof Zfloat){
            return new Zfloat(value % ((Zfloat) zval).value);
        }
        else if(zval instanceof Castable){

            return new Zfloat(value % ((Castable) zval).floatCast().value);
        }
        return null;
    }

    @Override
    public Zval pos() {
        return new Zfloat(value);
    }

    @Override
    public Zval neg() {
        return new Zfloat(-value);
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

    private static int balance(double i,double j){
        return i != j ? i < j ? -1 : 1 : 0;
    }

    @Override
    public int compareTo(Zval zval) {

        if(zval instanceof Zfloat){
            return balance(value,((Zfloat) zval).value);
        }
        else if(zval instanceof Zint){
            return balance(value,((Zint) zval).value);
        }
        else if(zval instanceof Castable){
            return balance(value,((Castable) zval).floatCast().value);
        }
        return -1;
    }

    @Override
    public boolean identical(Zval val) {
        return val instanceof Zfloat && value == ((Zfloat) val).value;
    }
}
