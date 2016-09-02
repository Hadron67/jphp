package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class UnaryIns implements Instruction {

    private int optype;

    public UnaryIns(int i){
        optype = i;
    }
    @Override
    public void exec(VM vm) {
        Zval op = Tool.fullyDeRef(vm.pop());

        switch(optype){
            case Operator.UNARY_ARRAY_CAST:
                if(op instanceof Castable){
                    vm.push(((Castable) op).arrayCast());
                }
                else{
                    vm.makeError("cannot cast");
                }
                break;
        }
    }

    @Override
    public String toString() {
        return "UNARY " + Operator.toUnaryOptrString(optype);
    }
}
