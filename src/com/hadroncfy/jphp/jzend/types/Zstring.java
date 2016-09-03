package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.*;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Comparable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Number;

/**
 * Created by cfy on 16-8-12.
 *
 */
public class Zstring implements Prefixable,OperatableL1,OperatableL2,Comparable,Concatable,Zval,Castable,Array,Bytable,Boolable {
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

    private static String blank(int count){
        StringBuilder sb = new StringBuilder();
        while(count --> 0){
            sb.append(" ");
        }
        return sb.toString();
    }

    public void assign(int index,String s){
        if(index >= 0 && index < s.length()){
            value = value.substring(0 , index) + (s.length() == 0? "" : s.charAt(0)) + value.substring(index - 1,value.length() - 1);
        }
        else if(index >= s.length()){
            value += blank(index - s.length()) + (s.length() == 0? "" : s.charAt(0));
        }
        else {
            value = blank(1 + index) + (s.length() == 0? "" : s.charAt(0)) + value;
        }
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
        return this.toNumber().plus(zval);
    }

    @Override
    public Zval minus(Zval zval) {
        return this.toNumber().minus(zval);
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

    public Number toNumber(){
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
        else return false;

    }

    @Override
    public Zstring clone() {
        return new Zstring(value);
    }

    @Override
    public Zref subscript(Zval value) {
        int i;
        if(value instanceof Zint){
            i = ((Zint) value).value;
        }
        else if(value instanceof Zfloat){
            i = (int) ((Zfloat) value).value;
        }
        else if(value instanceof Castable){
            i = ((Castable) value).intCast().value;
        }
        else{
            i = 0;
        }
        return new StringSubRef(i);
    }

    @Override
    public int size() {
        return value.length();
    }

    @Override
    public Zval and(Zval zval) {

        return boolCast().and(zval);
    }

    @Override
    public Zval or(Zval zval) {
        return boolCast().or(zval);
    }

    @Override
    public Zval not() {
        return boolCast().not();
    }

    @Override
    public Zval bitAnd(Zval zval) {
        if(zval instanceof Zstring){
            char[] ret;
            char[] s;
            int l;
            if(value.length() > ((Zstring) zval).value.length()){
                ret = value.toCharArray();
                s = ((Zstring) zval).value.toCharArray();
                l = ((Zstring) zval).value.length();
            }
            else{
                ret = ((Zstring) zval).value.toCharArray();
                s = value.toCharArray();
                l = value.length();
            }
            while(l --> 0){
                ret[l] &= s[l];
            }
            return new Zstring(new String(ret));
        }
        else{
            return toNumber().bitAnd(zval);
        }
    }

    @Override
    public Zval bitOr(Zval zval) {
        if(zval instanceof Zstring){
            char[] ret;
            char[] s;
            int l;
            if(value.length() > ((Zstring) zval).value.length()){
                ret = value.toCharArray();
                s = ((Zstring) zval).value.toCharArray();
                l = ((Zstring) zval).value.length();
            }
            else{
                ret = ((Zstring) zval).value.toCharArray();
                s = value.toCharArray();
                l = value.length();
            }
            while(l --> 0){
                ret[l] |= s[l];
            }
            return new Zstring(new String(ret));
        }
        else{
            return toNumber().bitAnd(zval);
        }
    }

    @Override
    public Zval bitXor(Zval zval) {
        if(zval instanceof Zstring){
            char[] ret;
            char[] s;
            int l;
            if(value.length() > ((Zstring) zval).value.length()){
                ret = value.toCharArray();
                s = ((Zstring) zval).value.toCharArray();
                l = ((Zstring) zval).value.length();
            }
            else{
                ret = ((Zstring) zval).value.toCharArray();
                s = value.toCharArray();
                l = value.length();
            }
            while(l --> 0){
                ret[l] ^= s[l];
            }
            return new Zstring(new String(ret));
        }
        else{
            return toNumber().bitAnd(zval);
        }
    }

    @Override
    public Zval bitNot() {
        char[] ret = value.toCharArray();
        int l = value.length();
        while(l --> 0){
            ret[l] = (char)~ret[l];
        }
        return new Zstring(new String(ret));
    }

    @Override
    public Zval leftShift(Zval zval) {
        if(zval instanceof Zstring){
            char[] ret;
            char[] s;
            int l;
            if(value.length() > ((Zstring) zval).value.length()){
                ret = value.toCharArray();
                s = ((Zstring) zval).value.toCharArray();
                l = ((Zstring) zval).value.length();
            }
            else{
                ret = ((Zstring) zval).value.toCharArray();
                s = value.toCharArray();
                l = value.length();
            }
            while(l --> 0){
                ret[l] <<= s[l];
            }
            return new Zstring(new String(ret));
        }
        else{
            return toNumber().bitAnd(zval);
        }
    }

    @Override
    public Zval rightShift(Zval zval) {
        if(zval instanceof Zstring){
            char[] ret;
            char[] s;
            int l;
            if(value.length() > ((Zstring) zval).value.length()){
                ret = value.toCharArray();
                s = ((Zstring) zval).value.toCharArray();
                l = ((Zstring) zval).value.length();
            }
            else{
                ret = ((Zstring) zval).value.toCharArray();
                s = value.toCharArray();
                l = value.length();
            }
            while(l --> 0){
                ret[l] >>= s[l];
            }
            return new Zstring(new String(ret));
        }
        else{
            return toNumber().bitAnd(zval);
        }
    }

    @Override
    public int compareTo(Zval zval) {
        if(zval instanceof Number){
            return toNumber().compareTo(zval);
        }
        else if(zval instanceof Zstring){
            return value.compareTo(((Zstring) zval).value);
        }
        else if(zval instanceof Zbool){
            if(equals(zval)){
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(zval instanceof Zarray){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean identical(Zval val) {
        return val instanceof Zstring && value.equals(((Zstring) val).value);
    }

    @Override
    public Zval multiply(Zval zval) {
        return this.toNumber().multiply(zval);
    }

    @Override
    public Zval divide(Zval zval) {
        return this.toNumber().divide(zval);
    }

    @Override
    public Zval mod(Zval zval) {
        return this.toNumber().mod(zval);
    }

    @Override
    public Zval pos() {
        return toNumber();
    }

    @Override
    public Zval neg() {
        return toNumber().neg();
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

    public class StringSubRef implements Zref{

        private int index;

        private StringSubRef(int i){
            index = i;
        }

        @Override
        public Zval assign(Zval src) {
            String s;
            if(src instanceof Zstring){
                s = ((Zstring) src).value;
            }
            else if(src instanceof Castable){
                s = ((Castable) src).stringCast().value;
            }
            else{
                return src;
            }
            Zstring.this.assign(index,s);
            return src;
        }

        @Override
        public Zval deRef() {
            if(index >= value.length() || index < 0){
                return new Zstring(" ");
            }
            else {
                return new Zstring(value.charAt(index) + "");
            }
        }

        @Override
        public boolean doTypeCheck(String typename) {
            return Zstring.this.doTypeCheck(typename);
        }

        @Override
        public String dump() {
            return deRef().dump();
        }

        @Override
        public String getTypeName() {
            return "string";
        }

        @Override
        public Zval clone() {
            return null;
        }
    }
}
