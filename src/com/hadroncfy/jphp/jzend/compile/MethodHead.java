package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-25.
 *
 */
public class MethodHead extends FunctionHead {

    protected ZendClass clazz;

    public MethodHead(String fname, boolean isRef,ZendClass clazz) {
        super(fname, isRef);
        this.clazz = clazz;
    }

    @Override
    protected String getFullName() {
        return clazz.getName() + "::" + fname;
    }
}
