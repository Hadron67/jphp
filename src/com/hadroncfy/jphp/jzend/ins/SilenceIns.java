package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;

/**
 * Created by cfy on 16-9-2.
 */
public class SilenceIns implements Instruction {
    public boolean isBegin;

    public SilenceIns(boolean b){
        isBegin = b;
    }

    @Override
    public void exec(VM vm) {
        if(isBegin){
            vm.beginSilence();
        }
        else{
            vm.endSilence();
        }
    }

    @Override
    public String toString() {
        return isBegin ? "BEGIN_SILNCE" : "END_SILENCE";
    }
}
