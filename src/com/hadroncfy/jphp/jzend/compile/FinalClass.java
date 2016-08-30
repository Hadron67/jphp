package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-30.
 */
public class FinalClass extends Class {
    protected FinalClass(String name) {
        super(name);
    }

    @Override
    protected String getHeadName() {
        return "final class";
    }
}
