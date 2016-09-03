package com.hadroncfy.jphp.jzend.ins;

/**
 * Created by cfy on 16-9-3.
 */
public interface SingleJumpIns extends Instruction {
    void setLine(int i);
    int  getLine();
}
