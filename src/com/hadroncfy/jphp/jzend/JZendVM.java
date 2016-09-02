package com.hadroncfy.jphp.jzend;

import com.hadroncfy.jphp.jzend.compile.ins.Instruction;
import com.hadroncfy.jphp.jzend.compile.ins.IntIns;
import com.hadroncfy.jphp.jzend.compile.ins.Opcode;
import com.hadroncfy.jphp.jzend.compile.ins.StringIns;
import com.hadroncfy.jphp.jzend.types.Zint;
import com.hadroncfy.jphp.jzend.types.Znull;
import com.hadroncfy.jphp.jzend.types.Zref;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Concatable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.OperatableL1;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

import java.util.Stack;

/**
 * Created by cfy on 16-8-2.
 */
public class JZendVM {

    private Stack<Zval> eval_stack = new Stack<>();

    private Program program;

    private int pc = 0;

    private Context env;

    private Zval result = Znull.NULL;

    public JZendVM(Context env, Program program){
        this.env = env;
        this.program = program;
    }

    public Zval getResult(){
        return result;
    }

    private static Zval fullyDeRef(Zval zval){
        while(zval instanceof Zref){
            zval = ((Zref) zval).deRef();
        }
        return zval;
    }


    public void exec(){
        int size = program.getSize();
        for(int i = 0;i < size;i++){
            Instruction ins = program.getIns(i);
            Zval op1,op2;
            switch(ins.opcode){
                case Opcode.POP:
                    eval_stack.pop();
                    break;
                case Opcode.PLUS://------------------------------------------------------------
                    op2 = fullyDeRef(eval_stack.pop());
                    op1 = fullyDeRef(eval_stack.pop());
                    if(op1 instanceof OperatableL1){
                        Zval ret = ((OperatableL1) op1).plus(op2);
                        if(ret != null){
                            eval_stack.push(ret);
                        }
                        else{
                            env.makeError("Unsupported operand types");
                        }
                    }
                    else {
                        env.makeError("Unsupported operand types");
                    }
                    break;
                case Opcode.STRING://-------------------------------------------------------------
                    if(ins instanceof StringIns){
                        eval_stack.push(new Zstring(((StringIns) ins).ins));
                    }
                    else{
                        throw new RuntimeException("this opcode should be an instance of StringIns,but it isn't!");
                    }
                    break;
                case Opcode.CONCAT://------------------------------------------------------------
                    int times = 0;
                    if(ins instanceof IntIns){
                        times = ((IntIns) ins).ins;
                    }
                    else
                        assert false;
                    op2 = fullyDeRef(eval_stack.pop());
                    for(int j = 0;j < times - 1;j++){
                        op1 = fullyDeRef(eval_stack.pop());
                        if(op1 instanceof Concatable){
                            op2 = ((Concatable) op1).concat(op2);
                            if(op2 == null){
                                env.makeError("Unsupported operand types");
                            }
                        }
                        else {
                            env.makeError("Unsupported operand types");
                        }
                    }
                    eval_stack.push(op2);
                    break;

                case Opcode.PRE_INC:
                    op1 = fullyDeRef(eval_stack.pop());

                case Opcode.PRE_DEC:
                case Opcode.POST_INC:
                case Opcode.POST_DEC:
                case Opcode.TOSTRING://-------------------------------------------------------------
                    op1 = fullyDeRef(eval_stack.pop());
                    if(op1 instanceof Castable){
                        Zval ret = ((Castable) op1).stringCast();
                        if(ret != null){
                            eval_stack.push(ret);
                        }
                        else{
                            env.makeError("Unsupported operand types");
                        }
                    }
                    else{
                        env.makeError("Unsupported operand types");
                    }
                    break;
                case Opcode.ECHO://--------------------------------------------------------------------
                    op1 = fullyDeRef(eval_stack.pop());
                    if(op1 instanceof Zstring){
                        env.out.print(((Zstring) op1).value);
                    }
                    else if(op1 instanceof Castable){
                        env.out.print(((Castable) op1).stringCast().value);
                    }
                    else {
                        env.makeError("invalid type for echo");
                    }
                    break;//----------------------------------------------------------------------------
                case Opcode.EXIT:
                    env.exit(eval_stack.pop());
                    break;
                case Opcode.RETURN:
                    result = eval_stack.pop();
                    break;
                case Opcode.INTEGER:
                    if(ins instanceof IntIns){
                        eval_stack.push(new Zint(((IntIns) ins).ins));
                    }
                    else
                        throw new IllegalArgumentException("this opcode should be an IntIns,but it isn't!");
                    break;

                default:throw new IllegalArgumentException("this opcode is not implemented yet:" + ins.opcode);
            }
        }
    }

}
