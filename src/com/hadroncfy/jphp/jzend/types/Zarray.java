package com.hadroncfy.jphp.jzend.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cfy on 16-8-12.
 *
 */
public class Zarray extends Zval {
    private Map<Zval,Zval> valueMap = new HashMap<>();

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
        return valueMap.size();
    }

    public void addItem(Zval zval){
        int index = valueMap.size();
        valueMap.put(new Zint(index),zval);
    }

    public void addItem(Zval key,Zval value){
        if(key instanceof Zint){
            valueMap.put(key,value);
        }
        else if(key instanceof Zstring){
            int ret;
            try{
                ret = (int) Double.parseDouble(((Zstring) key).value);
                valueMap.put(new Zint(ret),value);
            }
            catch (NumberFormatException e){
                valueMap.put(key,value);
            }
        }
        else if(key instanceof Zfloat || key instanceof Zbool){
            valueMap.put(key.intCast(),value);
        }
        else if(key instanceof Znull){
            valueMap.put(key.stringCast(),value);
        }

    }

    @Override
    public String getTypeName() {

        return "array";
    }

    @Override
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("array(").append(size()).append(") { ");
        for(Map.Entry<Zval,Zval> entry : valueMap.entrySet()){
            sb.append(keyString(entry.getKey())).append(" => ").append(entry.getValue().dump()).append(",");
        }
        sb.append(" }");
        return sb.toString();
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
}
