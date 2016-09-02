package com.hadroncfy.jphp.jzend;

import com.hadroncfy.jphp.jzend.compile.ins.Instruction;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

import java.util.Map;

/**
 * Created by cfy on 16-9-1.
 */
public interface Program {
    Instruction getIns(int i);
    int getSize();
    Map<String,Zval> getConsts();
}
