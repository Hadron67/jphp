package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-5-13.
 *
 */
public class CompilationException extends Exception {

    public CompilationException(int line,int column,String fname,String msg){
        super(msg + " in " + fname + " on line " + line + " column " + column);
    }
}
