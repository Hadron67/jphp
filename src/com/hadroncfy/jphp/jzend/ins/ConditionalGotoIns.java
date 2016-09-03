package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zbool;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class ConditionalGotoIns implements SingleJumpIns {
    public boolean inverted = false;
    public int line;

    public ConditionalGotoIns(int i,boolean inverted){
        line = i;
        this.inverted = inverted;
    }

    public ConditionalGotoIns(boolean i){
        inverted = i;
    }

    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        boolean v;
        if(val instanceof Zbool){
            v = ((Zbool) val).value;
        }
        else if(val instanceof Castable){
            v = ((Castable) val).boolCast().value;
        }
        else{
            vm.makeError("invalid boolean encountered");
            return;
        }
        if(v ^ inverted){
            vm.jump(line);
        }
    }

    @Override
    public String toString() {
        return (inverted ? "CONDITIONAL_NOT_GOTO " : "CONDITIONAL_GOTO ") + line;
    }

    @Override
    public void setLine(int i) {
        line = i;
    }

    @Override
    public int getLine() {
        return line;
    }
}
