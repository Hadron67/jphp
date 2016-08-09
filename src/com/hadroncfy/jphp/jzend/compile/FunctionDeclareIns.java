package com.hadroncfy.jphp.jzend.compile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-5-27.
 */
public class FunctionDeclareIns extends Instruction {
    public String fname;
    public List<ArgItem> args;
    public List<UseItem> uses;
    public int entry_line;

    public FunctionDeclareIns(String fname){
        super(Opcode.DEFINE_FUNCTION);
        this.fname = fname;
        args = new ArrayList<>();
        uses = new ArrayList<>();
    }

    public void addArg(String name,String typename,boolean is_ref){
        ArgItem t = new ArgItem();
        t.vname = name;
        t.typename =  typename;
        t.is_ref = is_ref;
        args.add(t);
    }

    public void use(String vname,boolean is_ref){
        UseItem item = new UseItem();
        item.is_ref = is_ref;
        item.vname = vname;
        uses.add(item);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(super.toString());
        b.append(" fname:\"").append(fname).append("\",line:").append(entry_line).append(",args:[");
        for(ArgItem item : args){
            b.append("{").append(item.toString()).append("},");
        }
        b.append("],useList:[");
        for(UseItem item : uses){
            b.append("{").append(item.toString()).append("},");
        }
        b.append("]");

        return b.toString();
    }

    class ArgItem{
        public String vname;
        public String typename;
        public boolean is_ref;

        @Override
        public String toString() {
            return "name:\"" + vname + "\",type:\"" + (typename.equals("") ? "any" : typename) + "\"" + (is_ref ? ",Ref" : "");
        }
    }

    class UseItem{
        public String vname;
        boolean is_ref;

        @Override
        public String toString() {
            return "name:\"" + vname + "\"" + (is_ref ? ",Ref" : "");
        }
    }
}
