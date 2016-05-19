package com.hadroncfy.jphp.jzend.compile;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cfy on 16-5-12.
 *
 */
public class JZendCompiler implements Compiler{
    private Stack<String> ns_stack;
    private List<Instruction> op_array;
    private List<Instruction> current_array;
    private Stack<List<Instruction>> op_array_stack;
    private Stack<IfContext> ifstmt_stack;
    private Stack<Instruction> loop_entry_stack;
    private Stack<Instruction> while_loop_escape_stack;
    private Stack<Integer> do_while_loop_entry_stack;
    private Stack<Instruction> for_loop_escape_stack;
    private Stack<Instruction> foreach_loop_escape_stack;
    private int getLine(){
        return op_array.size();
    }
    protected void addIns(Instruction ins){
        current_array.add(ins);
    }

    public void printInstructionList(PrintStream ps){
        int line = 0;
        for(Instruction op : op_array){
            ps.println(line++ + "    " + op.toString());
        }
    }
    private Instruction getIns(int i){
        return current_array.get(current_array.size() - i - 1);
    }

    public JZendCompiler(){
        op_array = new ArrayList<>();
        current_array = op_array;
        ns_stack = new Stack<>();
        ns_stack.push("");
        op_array_stack = new Stack<>();
        op_array_stack.push(op_array);
        ifstmt_stack = new Stack<>();
        loop_entry_stack = new Stack<>();
        while_loop_escape_stack = new Stack<>();
        do_while_loop_entry_stack = new Stack<>();
        for_loop_escape_stack = new Stack<>();
        foreach_loop_escape_stack = new Stack<>();
    }

    private void switchToOpArray(){
        current_array = op_array;
    }
    private void switchToStack(){
        current_array = op_array_stack.peek();
    }
    private void pushOpStack(){
        op_array_stack.push(new ArrayList<>());
    }
    private void popOpStack(){
        List<Instruction> inslist = op_array_stack.pop();
        for(Instruction ins : inslist){
            op_array.add(ins);
        }
    }
    private void evalConcat(){
        if(getIns(0).opcode == Opcode.STRING){
            StringBuilder sb = new StringBuilder();
            Instruction ins;
            while((ins = getIns(0)).opcode == Opcode.STRING){
                sb.append(ins.sdata);

            }
        }
    }
    @Override
    public void DoBinaryOptr(int token_type) {
        int op = 0;
        switch(token_type){
            case JZendParserConstants.AND: { op = Opcode.AND;break; }
            case JZendParserConstants.OR: op = Opcode.OR;break;
            case JZendParserConstants.XOR: op = Opcode.XOR;break;
            case JZendParserConstants.BITAND: op = Opcode.BITAND;break;
            case JZendParserConstants.BITOR: op = Opcode.BITOR;break;
            case JZendParserConstants.BITXOR: op = Opcode.BITXOR;break;
            case JZendParserConstants.EQU: op = Opcode.EQU; break;
            case JZendParserConstants.NEQU: op = Opcode.NEQU; break;
            case JZendParserConstants.IDENTICAL: op = Opcode.IDENTICAL; break;
            case JZendParserConstants.NIDENTICAL: op = Opcode.NIDENTICAL; break;
            case JZendParserConstants.MT: op = Opcode.MT;break;
            case JZendParserConstants.MTOE: op = Opcode.MTOE;break;
            case JZendParserConstants.LT: op = Opcode.LT;break;
            case JZendParserConstants.LTOE: op = Opcode.LTOE;break;
            case JZendParserConstants.LSHIFT: op = Opcode.LSHIFT;break;
            case JZendParserConstants.RSHIFT: op = Opcode.RSHIFT;break;
            case JZendParserConstants.PLUS: op = Opcode.PLUS;break;
            case JZendParserConstants.MINUS: op = Opcode.MINUS;break;
            case JZendParserConstants.CONCAT: {
                addIns(new Instruction(Opcode.CONCAT,2));
                return;
            }
            case JZendParserConstants.TIMES: op = Opcode.TIMES;break;
            case JZendParserConstants.DIVIDE: op = Opcode.DIVIDE;break;
            case JZendParserConstants.MOD:op = Opcode.MOD;break;
            default:assert false;
        }
        addIns(new Instruction((op)));
    }

    @Override
    public void DoUnaryOptr(int token_type) {
        int op = 0;
        switch(token_type){
            case JZendParserConstants.INC:op = Opcode.PRE_INC;break;
            case JZendParserConstants.DEC:op = Opcode.PRE_DEC;break;
            case JZendParserConstants.BITNOT:op = Opcode.BITNOT;break;
            case JZendParserConstants.NOT:op = Opcode.NOT;break;
            case JZendParserConstants.CLONE:op = Opcode.CLONE;break;
        }
        addIns(new Instruction(op));
    }

    @Override
    public void DoPostIncOrDec(boolean is_dec) {
        addIns(new Instruction(is_dec ? Opcode.POST_DEC : Opcode.POST_INC));
    }

    @Override
    public void DoDup() {
        addIns(new Instruction(Opcode.DUP));
    }

    @Override
    public void DoNull() {
        addIns(new Instruction(Opcode.NULL));
    }

    @Override
    public void DoSwitch() {
        addIns(new Instruction(Opcode.SWITCH));
    }

    @Override
    public void DoNewArray() {
        addIns(new Instruction(Opcode.NEW_ARRAY));
    }

    @Override
    public void DoAddArrayItem(boolean is_map) {
        if(!is_map)
            addIns(new Instruction(Opcode.ADD_ARRAY_ITEM));
        else
            addIns(new Instruction(Opcode.ADD_ARRAY_MAP_ITEM));
    }

    @Override
    public void DoRequestArrayPointerItem(boolean is_reference) {
        if(is_reference)
            addIns(new Instruction(Opcode.ARRAY_GET_POINTER_ITEM_AS_REFERENCE));
        else
            addIns(new Instruction(Opcode.ARRAY_GET_POINTER_ITEM));
    }

    @Override
    public void DoDereference() {
        addIns(new Instruction(Opcode.DEREFERENCE));
    }

    @Override
    public void DoPop() {
        addIns(new Instruction(Opcode.POP));
    }

    @Override
    public void DoNombre(double n) {
        addIns(new Instruction(Opcode.NUMBER,n));
    }

    @Override
    public void DoString(String s) {
        addIns(new Instruction(Opcode.STRING,s));
    }

    @Override
    public void DoToString() {
        if(getIns(0).opcode != Opcode.STRING)
            addIns(new Instruction(Opcode.TOSTRING));
    }

    @Override
    public void DoConcat(int count) {
        addIns(new Instruction(Opcode.CONCAT,count));
    }

    @Override
    public void DoRequestMember() {
        addIns(new Instruction(Opcode.REQUEST_MEMBER));
    }

    @Override
    public void DoSubscript(boolean is_max) {
        if(is_max)
            addIns(new Instruction(Opcode.MAX_SUBSCRIPT));
        else
            addIns(new Instruction(Opcode.SUBSCRIPT));
    }

    @Override
    public void DoFindVariable(String vname) {
        addIns(new Instruction(Opcode.FIND_VARIABLE,vname));
    }

    @Override
    public void DoFindVariableByName() {
        if(getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.FIND_VARIABLE_BY_NAME));
    }

    @Override
    public void CheckWritable() throws CompilationException {
        int op = getIns(0).opcode;
        if(op != Opcode.SUBSCRIPT
                && op != Opcode.FIND_VARIABLE
                && op != Opcode.REQUEST_MEMBER
                && op != Opcode.FIND_VARIABLE_AS_REFERENCE
                && op != Opcode.MAX_SUBSCRIPT
                && op != Opcode.FUNCTION_CALL
                && op != Opcode.PRE_DEC
                && op != Opcode.PRE_INC
                && op != Opcode.POST_INC
                && op != Opcode.POST_DEC){
            throw new CompilationException( " Invalid Right value.");
        }
    }

    @Override
    public void DoFunctionCall() {
        addIns(new Instruction(Opcode.FUNCTION_CALL));
    }

    @Override
    public void DoFindConst(String n) {
        addIns(new Instruction(Opcode.FIND_CONST,n));
    }

    @Override
    public void DoFindFunction(String name) {
        addIns(new Instruction(Opcode.FIND_FUNCTION,name));
    }

    @Override
    public void DoExit() {
        addIns(new Instruction(Opcode.EXIT));
    }

    @Override
    public void DoPrint() {
        if(getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.PRINT));
    }

    @Override
    public void DoEcho() {
        if(getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.ECHO));
    }

    @Override
    public void convertRvalueToLvalue() throws CompilationException {
        CheckWritable();
        Instruction ins = getIns(0);
        switch (ins.opcode){
            case Opcode.FIND_VARIABLE:ins.opcode = Opcode.FIND_VARIABLE_AS_REFERENCE;break;
            case Opcode.SUBSCRIPT:ins.opcode = Opcode.SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.REQUEST_MEMBER:ins.opcode = Opcode.REQUEST_MEMBER_AS_REFERENCE;break;
            case Opcode.FIND_VARIABLE_BY_NAME:ins.opcode = Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE;break;
            case Opcode.MAX_SUBSCRIPT:ins.opcode = Opcode.MAX_SUBSCRIPT_AS_REFERENCE;break;
        }
    }

    @Override
    public void DoBeginIfStatement() {
        IfContext con = new IfContext();
        addIns(new Instruction(Opcode.NOT));
        Instruction ins = new Instruction(Opcode.CONDITIONAL_GOTO);
        addIns(ins);
        con.last_ins = ins;
        ifstmt_stack.push(con);
    }

    @Override
    public void DoEndIfBlock() {
        Instruction ins = new Instruction(Opcode.GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins.ddata = getLine();
        ifstmt_stack.peek().last_ins = null;
        ifstmt_stack.peek().pending_exit_ins.add(ins);
    }

    @Override
    public void DoElseIfBlock() {
        addIns(new Instruction(Opcode.NOT));
        Instruction ins = new Instruction(Opcode.CONDITIONAL_GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins = ins;
    }

    @Override
    public void DoEndIfStatement() {
        IfContext con = ifstmt_stack.pop();
        if(con.last_ins != null) //if not true,there isn't an else block.
            con.last_ins.ddata = getLine();
        con.finish(getLine());
    }

    @Override
    public void DoWhileStatement(int where) {
        switch(where){
            case 0://begin of while
                Instruction ins = new Instruction(Opcode.BEGIN_LOOP);
                addIns(ins);
                ins.ddata = getLine();
                loop_entry_stack.push(ins);
                break;
            case 1://end of expression,begin of block
                addIns(new Instruction(Opcode.NOT));
                Instruction ins2 = new Instruction(Opcode.CONDITIONAL_GOTO);
                addIns(ins2);
                while_loop_escape_stack.push(ins2);
                break;
            case 2://end if the while statement
                Instruction entry_ins = loop_entry_stack.pop();
                Instruction ins3 = new Instruction(Opcode.GOTO,entry_ins.ddata);
                addIns(ins3);
                while_loop_escape_stack.pop().ddata = getLine();
                entry_ins.ddata2 = getLine();
                break;
            default:throw new RuntimeException("Unknown location:" + where);
        }
    }

    @Override
    public void DoDoWhileStatement(int where) {
        switch(where){
            case 0://begin of code block
                Instruction ins = new Instruction(Opcode.BEGIN_LOOP);
                addIns(ins);
                do_while_loop_entry_stack.push(getLine());
                loop_entry_stack.push(ins);
                break;
            case 1://end of code block,begin of expression
                loop_entry_stack.peek().ddata = getLine();
                break;
            case 2://end of expression
                addIns(new Instruction(Opcode.CONDITIONAL_GOTO,do_while_loop_entry_stack.pop()));
                loop_entry_stack.pop().ddata2 = getLine();
                break;
            default:throw new RuntimeException("Unknown location in do-while:" + where);
        }
    }

    @Override
    public void DoForStatement(int where) {
        switch(where){
            case 0://end of the first expression,begin of second expression
                addIns(new Instruction(Opcode.POP));
                Instruction ins = new Instruction(Opcode.BEGIN_LOOP);
                addIns(ins);
                ins.ddata = getLine();
                loop_entry_stack.push(ins);
                break;
            case 1://end of second expression,begin of third expression
                addIns(new Instruction(Opcode.NOT));
                Instruction ins2 = new Instruction(Opcode.CONDITIONAL_GOTO);
                addIns(ins2);
                for_loop_escape_stack.push(ins2);
                pushOpStack();
                switchToStack();
                break;
            case 2://end of third expression,begin of code block
                switchToOpArray();
                break;
            case 3://end of code block
                popOpStack();
                Instruction loopbegin = loop_entry_stack.pop();
                addIns(new Instruction(Opcode.GOTO,loopbegin.ddata));
                loopbegin.ddata2 = getLine();
                for_loop_escape_stack.pop().ddata = getLine();
                break;
            default:throw new RuntimeException("Unknown location in for-statement:" + where);
        }
    }

    @Override
    public void DoForEachStatement(int where) {
        switch(where){
            case 0://end of first expression,begin of second expression
                Instruction ins = new Instruction(Opcode.BEGIN_LOOP);
                addIns(ins);
                ins.ddata = getLine();
                loop_entry_stack.push(ins);
                addIns(new Instruction(Opcode.DUP));
                addIns(new Instruction(Opcode.ARRAY_POINTER_END));
                ins = new Instruction(Opcode.CONDITIONAL_GOTO);
                addIns(ins);
                foreach_loop_escape_stack.push(ins);
                break;
            case 1://end of second expression,begin of code block

                break;
            case 2://end of code block
                ins = loop_entry_stack.pop();
                addIns(new Instruction(Opcode.DUP));
                addIns(new Instruction(Opcode.ARRAY_POINTER_INC));
                addIns(new Instruction(Opcode.POP));
                addIns(new Instruction(Opcode.GOTO,ins.ddata));
                foreach_loop_escape_stack.pop().ddata = getLine();
                ins.ddata2 = getLine();
                addIns(new Instruction(Opcode.ARRAY_POINTER_RESET));
                addIns(new Instruction(Opcode.POP));
                break;
        }
    }

    @Override
    public void DoSwitchOrCase(int where) {
        switch(where){
            case 0://begin of switch statement
                Instruction ins = new Instruction(Opcode.BEGIN_LOOP);
                ins.ddata = -1;
                addIns(ins);
                addIns(new Instruction(Opcode.BEGIN_SWITCH));
                loop_entry_stack.push(ins);
                break;
            case 1://case expression begin
                addIns(new Instruction(Opcode.BEGIN_CASE));
                break;
            case 2://case label
                addIns(new Instruction(Opcode.CASE));
                break;
            case 3:
                addIns(new Instruction(Opcode.DEFAUL));
                break;
            case 4://end of the statement
                if(getIns(0).opcode != Opcode.DEFAUL)
                    addIns(new Instruction(Opcode.DEFAUL));
                loop_entry_stack.pop().ddata2 = getLine();
                break;
            default:throw new RuntimeException("Unknown location in switch:" + where);
        }
    }

    @Override
    public void DoBreakOrContinue(int which,boolean has_expr) {
        if(!has_expr)
            addIns(new Instruction(Opcode.NUMBER,1));
        switch(which){
            case 0://break
                addIns(new Instruction(Opcode.BREAK));
                break;
            case 1://continue
                addIns(new Instruction(Opcode.CONTINUE));
                break;
            default:throw new RuntimeException("Unknown statement:" + which);
        }
    }

    @Override
    public void DoReturnOrThrow(int which) {
        switch(which){
            case 0://return
                addIns(new Instruction(Opcode.RETURN));
                break;
            case 1://throw
                addIns(new Instruction(Opcode.THROW));
                break;
            default:throw new RuntimeException("not return nor throw:" + which);
        }
    }

    @Override
    public void DoUnset() throws CompilationException {
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.UNSET));
    }

    @Override
    public void finishCompiling() {
        addIns(new Instruction(Opcode.NUMBER,0));
        addIns(new Instruction(Opcode.EXIT));
    }

    @Override
    public void DoAssign(boolean is_array, boolean is_rev) {
        if(is_rev)
            addIns(new Instruction(Opcode.SWITCH));
        if(!is_array){
            addIns(new Instruction(Opcode.ASSIGN));
        }
        else{
            addIns(new Instruction(Opcode.ARRAY_ASSIGN));
        }

    }

    @Override
    public void DoReference() throws CompilationException {
        convertRvalueToLvalue();
    }

    @Override
    public void DoNew() {
        if(getIns(0).opcode == Opcode.FUNCTION_CALL)
            getIns(0).opcode = Opcode.NEW;
        else
            addIns(new Instruction(Opcode.NEW));
    }

    @Override
    public void DoFindClass() {

    }

    @Override
    public void DoPackArg(int count) {
        addIns(new Instruction(Opcode.PACK_ARG,count));
    }

    @Override
    public String getCurrentNameSpace() {
        return ns_stack.peek();
    }

    @Override
    public void DoEnterNameSpace(String name) {
        ns_stack.push(ns_stack.peek() + name + "\\");
    }

    @Override
    public void DoLeaveNameSpace() {
        ns_stack.pop();
    }

    class IfContext{
        public List<Instruction> pending_exit_ins;
        public Instruction last_ins;
        public IfContext(){
            pending_exit_ins = new ArrayList<>();
        }
        public void finish(int line){
            for(Instruction ins : pending_exit_ins){
                ins.ddata = line;
            }
        }
    }
}
