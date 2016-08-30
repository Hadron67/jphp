package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-30.
 */
public class AbstractClass extends Class{
    protected AbstractClass(String name) {
        super(name);
    }

    @Override
    protected String getHeadName() {
        return "abstract class";
    }

    @Override
    protected boolean finishParsing()  {
        return true;
    }
}
