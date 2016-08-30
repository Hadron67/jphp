package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-26.
 */
public class IllegalClassOperationException extends Exception {
    protected ZendClass clazz;

    public IllegalClassOperationException(String msg,ZendClass clas){
        super(msg + " in class \"" + clas.getName() + "\"");
        clazz = clas;
    }
}
