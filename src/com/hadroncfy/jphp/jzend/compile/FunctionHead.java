package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.Zval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-8-25.
 */
public class FunctionHead {
    protected boolean isRef;
    protected List<ArgItem> args = new ArrayList<>();
    protected List<UseItem> useVars = new ArrayList<>();
    protected String fname = "(anonymous)";
    protected String filename = "-";

    public FunctionHead(String fname,boolean isRef){
        this.isRef = isRef;
        this.fname = fname;
    }

    protected void addArg(String vname,String typename,boolean isRef,Zval defaultv){
        ArgItem item = new ArgItem();
        item.vname = vname;
        item.typename = typename;
        item.isRef = isRef;
        item.defaultv = defaultv;
        args.add(item);
    }

    protected void addUse(String vname,boolean isRef){
        UseItem item = new UseItem();
        item.vname = vname;
        item.isRef = isRef;
        useVars.add(item);
    }

    protected String getFullName(){
        return fname;
    }

    public int getArgCount(){
        return args.size();
    }

    protected void addArg(String vname,String typename,boolean isRef){
        addArg(vname,typename,isRef,null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function ").append(getFullName()).append("(");
        for(ArgItem item : args){
            if(item.typename.equals("")){
                sb.append("[any] ");
            }
            else{
                sb.append(item.typename).append(" ");
            }
            if(item.isRef){
                sb.append("&");
            }
            sb.append(item.vname);
            if(item.defaultv != null){
                sb.append(" = ").append(item.defaultv.dump());
            }
            sb.append(",");

        }
        sb.append(")");
        if(useVars.size() > 0){
            sb.append(" use (");
            for(UseItem item : useVars){
                if(item.isRef){
                    sb.append("&");
                }
                sb.append(item.vname).append(",");
            }
            sb.append(")");
        }
        return sb.toString();
    }

    protected class ArgItem{
        String vname;
        String typename;
        boolean isRef = false;
        Zval defaultv = null;
    }

    protected class UseItem{
        String vname;
        boolean isRef = false;
    }
}
