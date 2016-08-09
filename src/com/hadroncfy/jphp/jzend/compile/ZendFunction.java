package com.hadroncfy.jphp.jzend.compile;

import java.util.List;

/**
 * Created by cfy on 16-8-9.
 */
public class ZendFunction extends Routine{
    protected String fname;
    protected List<ArgItem> args;

    protected ZendFunction(String name){
        fname = name;

    }

    protected void addArg(String vname,String typename){
        ArgItem item = new ArgItem();
        item.vname = vname;
        item.typename = typename;
        args.add(item);
    }


    class ArgItem{
        String vname;
        String typename;
    }
}
