package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-31.
 */
public class SimpleRef implements Zref {
    private Zval zval;

    public SimpleRef(Zval zval){
        this.zval = zval;
    }

    @Override
    public Zval assign(Zval src) {
        return zval = src;
    }

    @Override
    public Zval deRef() {
        return zval;
    }


    @Override
    public String dump() {
        return zval.dump();
    }

    @Override
    public String getTypeName() {
        return zval.getTypeName();
    }

    @Override
    public Zval clone() {
        return zval.clone();
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return zval.doTypeCheck(typename);
    }
}
