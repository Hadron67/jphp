package com.hadroncfy.jphp.jzend.ins;

/**
 * Created by cfy on 16-9-2.
 */
public interface ReferencableIns extends Instruction {
    void convertToLvalue();
    void convertToRvalue();
}
