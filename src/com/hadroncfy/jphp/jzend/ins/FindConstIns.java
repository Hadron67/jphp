package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class FindConstIns implements Instruction {

    public String cname;

    public FindConstIns(String cname){
        this.cname = cname;
    }

    @Override
    public void exec(VM vm) {
        Zval ret = vm.getEnv().getConst(cname);
        if(ret != null){
            vm.push(ret);
        }
        else{
            vm.getEnv().makeNotice("Use of undefined const " + cname + ",assumed \"" + cname + "\"");
            vm.push(new Zstring(cname));
        }
    }
}
