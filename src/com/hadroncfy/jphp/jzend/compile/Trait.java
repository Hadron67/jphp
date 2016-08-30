package com.hadroncfy.jphp.jzend.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cfy on 16-8-30.
 */
public class Trait extends Class {


    protected Trait(String name) {
        super(name);
    }

    @Override
    protected String getHeadName() {
        return "trait";
    }


    @Override
    protected boolean finishParsing()  {
        return true;
    }

}
