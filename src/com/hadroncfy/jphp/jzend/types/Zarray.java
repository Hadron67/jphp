package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cfy on 16-8-12.
 *
 */
public class Zarray implements Zval,Castable,Array,ArrayIterable,MapIterable {

    private Map<Zval,Zref> vars = new HashMap<>();

    public Zarray(){

    }

    public static boolean checkKeyType(Zval zval){
        return zval instanceof Zint
                || zval instanceof Zfloat
                || zval instanceof Zbool
                || zval instanceof Zstring
                || zval instanceof Znull;
    }

    public int size(){
        return vars.size();
    }

    public void addItem(Zval zval){
        int index = vars.size();
        vars.put(new Zint(index),new SimpleRef(zval));
    }

    public void addItem(Zval key, Zval value){
        if(key instanceof Zint){
            vars.put(key,new SimpleRef(value));
        }
        else if(key instanceof Zstring){
            int ret;
            try{
                ret = (int) Double.parseDouble(((Zstring) key).value);
                vars.put(new Zint(ret),new SimpleRef(value));
            }
            catch (NumberFormatException e){
                vars.put(key,new SimpleRef(value));
            }
        }
        else if(key instanceof Castable) {
            if (key instanceof Zfloat || key instanceof Zbool) {
                vars.put(((Castable)key).intCast(), new SimpleRef(value));
            } else if (key instanceof Znull) {
                vars.put(((Castable)key).stringCast(), new SimpleRef(value));
            } else {
                throw new RuntimeException("invalid array offset type,this shouldn't happen!");
            }
        }
        else
            throw new RuntimeException("invalid array offset type,this shouldn't happen!");

    }


    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("array");
    }

    @Override
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("array(").append(size()).append(") { ");
        for(Map.Entry<Zval,Zref> entry : vars.entrySet()){
            sb.append(keyString(entry.getKey())).append(" => ").append(entry.getValue().dump()).append(",");
        }
        sb.append(" }");
        return sb.toString();
    }

    @Override
    public String getTypeName() {
        return "array";
    }

    @Override
    public Zval clone() {
        Zarray ret = new Zarray();
        for(Map.Entry<Zval,Zref> entry : vars.entrySet()){
            ret.addItem(entry.getKey(),entry.getValue().deRef());
        }
        return ret;
    }

    public TestableVal getTestVal(Zval key){
        return new TestValImpl(key);
    }

    private static String keyString(Zval key){
        if(key instanceof Zint){
            return "[" + ((Zint) key).value + "]";
        }
        else if(key instanceof Zstring){
            return "[\"" + ((Zstring) key).value + "\"]";
        }
        else
            throw new AssertionError("illegal key type.");
    }

    @Override
    public Zref subscript(Zval zval) {
        return vars.get(zval);
    }

    @Override
    public Zint intCast() {
        return null;
    }

    @Override
    public Zfloat floatCast() {
        return null;
    }

    @Override
    public Zstring stringCast() {
        return new Zstring("Array");
    }

    @Override
    public Zbool boolCast() {
        return null;
    }

    @Override
    public Zarray arrayCast() {
        return null;
    }

    @Override
    public ArrayIterator arrayIterator(boolean isRef) {
        return new ArrayIteratorImpl(this,isRef);
    }

    @Override
    public MapIterator mapIterator(boolean valueIsRef) {
        return new MapIteratorImpl(this,valueIsRef);
    }


    public class ArrayIteratorImpl implements ArrayIterator{

        private Iterator<Map.Entry<Zval,Zref>> it;

        private Map.Entry<Zval,Zref> c;

        private boolean isRef;

        public ArrayIteratorImpl(Zarray array,boolean ref){
            isRef = ref;
            it = array.vars.entrySet().iterator();
            c = it.next();
        }

        @Override
        public void next() {
            c = it.next();
        }

        @Override
        public Zval get() {
            return isRef ? c.getValue() : c.getValue().deRef();
        }

        @Override
        public boolean end() {
            return it.hasNext();
        }

        @Override
        public boolean doTypeCheck(String typename) {
            return false;
        }

        @Override
        public String dump() {
            return "ArrayIterator";
        }

        @Override
        public String getTypeName() {
            return "ArrayIterator";
        }

        @Override
        public Zval clone() {
            return null;
        }
    }

    public class MapIteratorImpl implements MapIterator{

        private Iterator<Map.Entry<Zval,Zref>> it;

        private Map.Entry<Zval,Zref> c;

        private boolean isRef;

        public MapIteratorImpl(Zarray array,boolean ref){
            isRef = ref;
            it = array.vars.entrySet().iterator();
            c = it.next();
        }

        @Override
        public void next() {
            c = it.next();
        }

        @Override
        public Zval getKey() {
            return c.getKey();
        }

        @Override
        public Zval getValue() {
            return isRef ? c.getValue() : c.getValue().deRef();
        }


        @Override
        public boolean end() {
            return it.hasNext();
        }

        @Override
        public boolean doTypeCheck(String typename) {
            return false;
        }

        @Override
        public String dump() {
            return "MapIterator {}";
        }

        @Override
        public String getTypeName() {
            return "MapIterator";
        }

        @Override
        public Zval clone() {
            return null;
        }
    }

    public class TestValImpl implements TestableVal{

        private Zval key;

        private TestValImpl(Zval key){
            this.key = key;
        }

        @Override
        public void unSet() {
            vars.remove(key);
        }

        @Override
        public boolean isSet() {
            return vars.get(key) != null;
        }

        @Override
        public boolean doTypeCheck(String typename) {
            return false;
        }

        @Override
        public String dump() {
            return "ArrayTester (" + key + ")";
        }

        @Override
        public String getTypeName() {
            return "ArrayTester";
        }

        @Override
        public Zval clone() {
            return null;
        }
    }
}
