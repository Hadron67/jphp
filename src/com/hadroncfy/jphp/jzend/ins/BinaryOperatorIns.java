package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.OperatableL1;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class BinaryOperatorIns implements Instruction {
    private int optype;
    public BinaryOperatorIns(int optype){
        this.optype = optype;
    }
    @Override
    public void exec(VM vm) {
        Zval op2 = Tool.fullyDeRef(vm.pop());
        Zval op1 = Tool.fullyDeRef(vm.pop());

        switch(optype){
            case Operator.BINARY_PLUS:
                if(op1 instanceof OperatableL1){
                    Zval ret = ((OperatableL1) op1).plus(op2);
                    if(ret != null){
                        vm.push(ret);
                    }
                    else{
                        vm.makeError("Unsupported operand types");
                    }
                }
                else {
                    vm.makeError("Unsupported operand types");
                }
                break;
            default:throw new AssertionError("Unknown binary operator type");
        }
    }

    @Override
    public String toString() {
        return "BINARY " + Operator.toBinaryOptrString(optype);
    }
}
