package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-2.
 */
public class RegMgr {
    private int treg_ptr = 0;
    public RegMgr(){
        treg_ptr = 0;
    }
    public int requestTempReg(){
        return treg_ptr++;
    }
    public void freeTempReg(int t){
        if(t == treg_ptr - 1){
            treg_ptr--;
        }
        else
            throw new AssertionError("unexpected reg index");
    }
}
