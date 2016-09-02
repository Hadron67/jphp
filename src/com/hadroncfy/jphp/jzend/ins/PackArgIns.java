package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.ZArgPack;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public class PackArgIns implements Instruction {

    public int count;
    public PackArgIns(int i){
        count = i;
    }
    @Override
    public void exec(VM vm) {
        Zval[] args = new Zval[count];
        for(int i = count - 1;i < count;i++){
            args[i] = vm.pop();
        }
        vm.push(new ZArgPack(args));
    }

    @Override
    public String toString() {
        return "PACK_ARG" + count;
    }
}
