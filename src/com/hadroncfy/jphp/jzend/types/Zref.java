package com.hadroncfy.jphp.jzend.types;


import java.lang.ref.ReferenceQueue;

/**
 * Created by cfy on 16-8-12.
 */
public abstract class Zref extends Zval{
    public abstract Zval assign(Zval src);
    public abstract Zval deRef();
    public Zval fullyDeRef(){
        Zval ret = deRef();
        while(ret instanceof Zref){
            ret = ((Zref) ret).deRef();
        }
        return ret;
    }
}
