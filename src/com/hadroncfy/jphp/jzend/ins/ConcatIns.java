package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class ConcatIns implements Instruction {

    public int count;

    public ConcatIns(int i){
        count = i;
    }
    @Override
    public void exec(VM vm) {
        int times = 0;
        Zval op1;
        Zval op2 = Tool.fullyDeRef(vm.pop());
        for(int j = 0;j < times - 1;j++){
            op1 = Tool.fullyDeRef(vm.pop());
            if(op1 instanceof Concatable){
                op2 = ((Concatable) op1).concat(op2);
                if(op2 == null){
                    vm.makeError("Unsupported operand types");
                    return;
                }
            }
            else {
                vm.makeError("Unsupported operand types");
                return;
            }
        }
        vm.push(op2);
    }

    @Override
    public String toString() {
        return "CONCAT " + count;
    }
}
