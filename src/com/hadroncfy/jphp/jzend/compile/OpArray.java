package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.compile.ins.Instruction;

/**
 * Created by cfy on 16-8-9.
 */
interface OpArray {
    void addIns(Instruction ins);
    int getLine();
}
