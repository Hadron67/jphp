package com.hadroncfy.jphp.jzend;

import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */
public interface VM {
    void push(Zval val);
    Zval pop();
    Zval peek();
    void echo(String s);
    void exit(Zval ret);
    void retour(Zval zval);
    void doThrow(Zval zval);
    void makeError(String msg);
    void jump(int line);
    void doBreak(int index);
    void doContinue(int index);
    Context getEnv();
    void beginSilence();
    void endSilence();
    Zval load(int index);
    void store(Zval val,int index);
}
