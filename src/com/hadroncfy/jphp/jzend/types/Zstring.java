package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.*;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Number;

/**
 * Created by cfy on 16-8-12.
 *
 */
public class Zstring implements CompleteZval,Concatable {
    public String value;
    public Zstring(String s){
        value = s;
    }


    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("string");
    }

    @Override
    public String dump() {
        return new StringBuilder().append("string(").append(value.length()).append(") \"").append(value).append("\"").toString();
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    String getValue(){
        return value;
    }

    @Override
    public Zval plus(Zval zval) {
        return ((Number)this.toNumber()).plus(zval);
    }

    @Override
    public Zval minus(Zval zval) {
        return ((Number)this.toNumber()).minus(zval);
    }

    @Override
    public Zval inc() {
        String ret = value.substring(0, value.length() - 2) + (1 + value.charAt(value.length() - 1));
        return new Zstring(ret);
    }

    @Override
    public Zval dec() {
        String ret = value.substring(0, value.length() - 2) + (value.charAt(value.length() - 1) - 1);
        return new Zstring(ret);
    }

    @Override
    public Zbool boolCast() {
        if(value.equals("") || value.equals("0")){
            return Zbool.FALSE;
        }
        return Zbool.TRUE;
    }

    @Override
    public Zarray arrayCast() {
        Zarray ret = new Zarray();
        ret.addItem(this);
        return ret;
    }

    @Override
    public Zint intCast() {
        int ret = 0;
        try {
            ret = Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            ret = 0;
        }
        return new Zint(ret);
    }

    @Override
    public Zfloat floatCast() {
        double ret = 0;
        try {
            ret = Double.parseDouble(value);
        }
        catch (NumberFormatException e){
            ret = 0;
        }
        return new Zfloat(ret);
    }

    @Override
    public Zstring stringCast() {
        return new Zstring(value);
    }

    public Zval toNumber(){
        try{
            double ret = Double.parseDouble(value);
            if(ret % 1 == 0){
                return new Zint((int) ret);
            }
            else{
                return new Zfloat(ret);
            }
        }
        catch (NumberFormatException e){
            return new Zint(0);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Zstring){
            return value.equals(((Zstring) obj).value);
        }
        else if(obj instanceof Zint){
            int ret;
            try{
                ret = Integer.parseInt(value);
            }
            catch (NumberFormatException e){
                ret = 0;
            }
            return ret == ((Zint) obj).value;
        }
        else if(obj instanceof Zfloat){
            double ret;
            try{
                ret = Double.parseDouble(value);
            }
            catch (NumberFormatException e){
                ret = 0;
            }
            return ret == ((Zfloat) obj).value;
        }
        else if(obj instanceof Zbool){
            return value.equals("true") && ((Zbool) obj).value || value.equals("false") && !((Zbool) obj).value;
        }

        return super.equals(obj);
    }

    @Override
    public Zstring clone() {
        return new Zstring(value);
    }

    @Override
    public Zref subscript(Zval value) {
        return null;
    }

    @Override
    public int size() {
        return value.length();
    }

    @Override
    public Zval and(Zval zval) {
        return null;
    }

    @Override
    public Zval or(Zval zval) {
        return null;
    }

    @Override
    public Zval not(Zval zval) {
        return null;
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
        return ((Number)this.toNumber()).multiply(zval);
    }

    @Override
    public Zval divide(Zval zval) {
        return ((Number)this.toNumber()).divide(zval);
    }

    @Override
    public Zval mod(Zval zval) {
        return ((Number)this.toNumber()).mod(zval);
    }

    @Override
    public Zval pos() {
        return toNumber();
    }

    @Override
    public Zval neg() {
        return ((Number)toNumber()).neg();
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
