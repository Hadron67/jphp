package com.hadroncfy.jphp.jzend.types;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 *
 * This class is used internally in JZend,for passing function arguments.
 */
public class ZArgPack implements Zval {

    public Zval[] args;

    public ZArgPack(Zval[] args){
        this.args = args;
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return false;
    }

    @Override
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArgPack {");
        for(Zval val : args){
            sb.append(val.dump());
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String getTypeName() {
        return "argPack";
    }

    @Override
    public Zval clone() {
        return new ZArgPack(args.clone());
    }
}
