package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.Zval;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-8-9.
 */
public class ZendFunction extends Routine{
    protected boolean isRef;
    protected List<ArgItem> args = new ArrayList<>();
    protected boolean hasReturn = false;

    protected ZendFunction(boolean isRef){
        this.isRef = isRef;
    }

    protected void addArg(String vname,String typename,boolean isRef,Zval defaultv){
        ArgItem item = new ArgItem();
        item.vname = vname;
        item.typename = typename;
        item.isRef = isRef;
        item.defaultv = defaultv;
        args.add(item);
    }

    protected void addArg(String vname,String typename,boolean isRef){
        addArg(vname,typename,isRef,null);
    }

    @Override
    protected void dump_self(PrintStream ps) {
        ps.println(toString());
        super.dump_self(ps);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function (");
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
        return sb.toString();
    }

    class ArgItem{
        String vname;
        String typename;
        boolean isRef = false;
        Zval defaultv = null;
    }

}
