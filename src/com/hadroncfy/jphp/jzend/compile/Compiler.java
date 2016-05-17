package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-5-12.
 *
 */
public interface Compiler {
    void DoBinaryOptr(int token_type);
    void DoUnaryOptr(int token_type);
    void DoPostIncOrDec(boolean is_dec);
    void DoDup();
    void DoNull();
    void DoNewArray();
    void DoAddArrayItem();
    void DoDereference();
    void DoPop();
    void DoNombre(double n);
    void DoString(String s);
    void DoFindVariable(String name);
    void DoFindVariableByName();
    void DoToString();
    void DoConcat(int count);
    void DoRequestMember();
    void DoSubscript(boolean is_max);
    void CheckWritable() throws CompilationException;
    void DoFunctionCall();
    void DoFindConst(String name);
    void DoFindFunction(String name);
    void DoExit();
    void DoPrint();
    void DoEcho();
    void DoBeginAssign() throws CompilationException;
    void DoEndAssign();
    void DoReference()throws CompilationException;
    void DoNew();
    void DoFindClass();
    void DoPackArg(int count);
    String getCurrentNameSpace();
    void DoEnterNameSpace(String name);
    void DoLeaveNameSpace();
    void convertRvalueToLvalue()throws CompilationException;

    void DoBeginIfStatement();
    void DoEndIfBlock();
    void DoElseIfBlock();
    void DoEndIfStatement();

    void DoWhileStatement(int where);
    void DoDoWhileStatement(int where);
    void DoBreakOrContinue(int which,boolean has_expr);
    void finishCompiling();
}
