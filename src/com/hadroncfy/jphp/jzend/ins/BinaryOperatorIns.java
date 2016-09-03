package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zbool;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.*;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Comparable;

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
        Zval ret = null;

        switch(optype){
            case Operator.BINARY_PLUS:
                if(op1 instanceof OperatableL1){
                    ret = ((OperatableL1) op1).plus(op2);
                }
                break;
            case Operator.BINARY_MINUS:
                if(op1 instanceof OperatableL1){
                    ret = ((OperatableL1) op1).minus(op2);
                }
                break;
            case Operator.BINARY_TIMES:
                if(op1 instanceof OperatableL2){
                    ret = ((OperatableL2) op1).multiply(op2);
                }
                break;
            case Operator.BINARY_DIVIDE:
                if(op1 instanceof OperatableL2){
                    ret = ((OperatableL2) op1).divide(op2);
                }
                break;
            case Operator.BINARY_MOD:
                if(op1 instanceof OperatableL2){
                    ret = ((OperatableL2) op1).mod(op2);
                }
                break;
            case Operator.BINARY_BIT_AND:
                if(op1 instanceof Bytable){
                    ret = ((Bytable) op1).bitAnd(op2);
                }
                break;
            case Operator.BINARY_BIT_OR:
                if(op1 instanceof Bytable){
                    ret = ((Bytable) op1).bitOr(op2);
                }
                break;
            case Operator.BINARY_BIT_XOR:
                if(op1 instanceof Bytable){
                    ret = ((Bytable) op1).bitXor(op2);
                }
                break;
            case Operator.BINARY_LEFT_SHIFT:
                if(op1 instanceof Bytable){
                    ret = ((Bytable) op1).leftShift(op2);
                }
                break;
            case Operator.BINARY_RIGHT_SHIFT:
                if(op1 instanceof Bytable){
                    ret = ((Bytable) op1).rightShift(op2);
                }
                break;
            case Operator.BINARY_MORE_THAN:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp == 1);
                }
                break;
            case Operator.BINARY_MORE_THAN_OR_EQUAL:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp == 1 || comp == 0);
                }
                break;
            case Operator.BINARY_LESS_THAN:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp == -1);
                }
                break;
            case Operator.BINARY_LESS_THAN_OR_EQUAL:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp == -1 || comp == 0);
                }
                break;
            case Operator.BINARY_EQUAL:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp == 0);
                }
                break;
            case Operator.BINARY_NOT_EQUAL:
                if(op1 instanceof Comparable){
                    int comp = ((Comparable) op1).compareTo(op2);
                    ret = Zbool.asZbool(comp != 0);
                }
                break;
            case Operator.BINARY_IDENTICAL:
                if(op1 instanceof Comparable){
                    ret = Zbool.asZbool(((Comparable) op1).identical(op2));
                }
                else{
                    ret = Zbool.FALSE;
                }
                break;
            case Operator.BINARY_AND:
                if(op1 instanceof Boolable){
                    ret = ((Boolable) op1).and(op2);
                }
                break;
            case Operator.BINARY_OR:
                if(op1 instanceof Boolable){
                    ret = ((Boolable) op1).or(op2);
                }
                break;
            case Operator.BINARY_NOT_IDENTICAL:
                if(op1 instanceof Comparable){
                    ret = Zbool.asZbool(!((Comparable) op1).identical(op2));
                }
                else{
                    ret = Zbool.TRUE;
                }
                break;
            default:throw new AssertionError("Unknown binary operator type");
        }

        if(ret == null){
            vm.makeError("The operator \"" + Operator.toBinaryOptrString(optype) + "\" cannot be applied to " + op1.getTypeName() + " and " + op2.getTypeName());
        }
        else{
            vm.push(ret);
        }
    }

    @Override
    public String toString() {
        return "BINARY " + Operator.toBinaryOptrString(optype);
    }
}
