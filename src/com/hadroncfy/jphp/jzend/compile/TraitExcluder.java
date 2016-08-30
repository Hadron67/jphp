package com.hadroncfy.jphp.jzend.compile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-8-31.
 */
public class TraitExcluder {
    protected String traitName;
    protected String methodName;
    protected List<String> excluded = new ArrayList<>();

    public TraitExcluder(String traitName,String methodName){
        this.traitName = traitName;
        this.methodName = methodName;
    }

    public void addExcluded(String s){
        excluded.add(s);
    }

    @Override
    public String toString() {
        String ret = traitName + "::" + methodName + " insteadof ";
        for(String s : excluded){
            ret += s + ",";
        }
        return ret;
    }
}
