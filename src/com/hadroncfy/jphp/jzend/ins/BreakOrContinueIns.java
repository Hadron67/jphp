package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-1.
 */
public class BreakOrContinueIns implements Instruction {

    public boolean isBreak;

    public int index;

    public BreakOrContinueIns(int index,boolean a){
        isBreak = a;
        this.index = index;
    }
    @Override
    public void exec(VM vm) {
        if(isBreak){
            vm.doBreak(index);
        }
        else{
            vm.doContinue(index);
        }
    }

    @Override
    public String toString() {
        return (isBreak ? "BREAK " : "CONTINUE ") + index;
    }
}
