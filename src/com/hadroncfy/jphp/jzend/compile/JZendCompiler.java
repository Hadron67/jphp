package com.hadroncfy.jphp.jzend.compile;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;

/** Created by cfy on 16-5-12. */
public class JZendCompiler {
    private Stack<String> ns_stack;
    private List<Instruction> op_array;
    private List<Instruction> current_array;

    private Stack<IfContext> ifstmt_stack;
    private Stack<SwitchContext> switchstmt_stack;
    private Stack<DoubleIntIns> loop_entry_stack;
    private Stack<IntIns> while_loop_escape_stack;
    private Stack<Integer> do_while_loop_entry_stack;
    private Stack<ForContext> for_loop_stack;
    private Stack<ForEachContext> foreach_context_stack;
    private Stack<IntIns> conditional_expr_stack;
    private Stack<TryCatchContext> try_catch_stack;
    private Stack<Instruction> func_stack;
    private Stack<ScopeContext> scope_stack;

    private RegMgr regmgr;

    //variales for function declaration
    private FunctionDeclareIns current_func;
    private IntIns function_skip_ins;
    private int func_default_values_temp_reg;



    private int getLine(){ return op_array.size(); }
    protected Instruction addIns(Instruction ins){ current_array.add(ins);return ins; }
    public void printInstructionList(PrintStream ps){
        int line = 0;
        for (Instruction op : op_array)
            ps.println(line++ + "    " + op.toString());
    }
    private Instruction getIns(int i){
        return current_array.get(current_array.size() - i - 1);
    }
    public JZendCompiler(){
        op_array = new ArrayList<>();
        current_array = op_array;

        ns_stack = new Stack<>();
        ns_stack.push("");

        ifstmt_stack = new Stack<>();
        loop_entry_stack = new Stack<>();
        while_loop_escape_stack = new Stack<>();
        do_while_loop_entry_stack = new Stack<>();
        for_loop_stack = new Stack<>();
        switchstmt_stack = new Stack<>();
        conditional_expr_stack = new Stack<>();
        try_catch_stack = new Stack<>();
        scope_stack = new Stack<>();
        foreach_context_stack = new Stack<>();
        regmgr = new RegMgr();

        enterScope();

    }

    public void DoBinaryOptr(int token_type) {
        int op = 0;
        switch (token_type){
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
                addIns(new IntIns(Opcode.CONCAT,2));
                return;
            }
            case JZendParserConstants.TIMES: op = Opcode.TIMES;break;
            case JZendParserConstants.DIVIDE: op = Opcode.DIVIDE;break;
            case JZendParserConstants.MOD:op = Opcode.MOD;break;
            default: assert false;
        }
        addIns(new Instruction((op)));
    }

    public void DoUnaryOptr(int token_type) {
        int op = 0;
        switch (token_type){
            case JZendParserConstants.INC:op = Opcode.PRE_INC;break;
            case JZendParserConstants.DEC:op = Opcode.PRE_DEC;break;
            case JZendParserConstants.BITNOT:op = Opcode.BITNOT;break;
            case JZendParserConstants.NOT:op = Opcode.NOT;break;
            case JZendParserConstants.CLONE:op = Opcode.CLONE;break;
            case JZendParserConstants.MINUS:op = Opcode.NEGTIVE;break;
            case JZendParserConstants.PLUS:break;
            default: throw new AssertionError("Unknown unary operator:" + token_type);
        }
        addIns(new Instruction(op));
    }

    public void DoPostIncOrDec(boolean is_dec) {
        addIns(new Instruction(is_dec ? Opcode.POST_DEC : Opcode.POST_INC));
    }

    public void DoDup() {
        addIns(new Instruction(Opcode.DUP));
    }


    public void DoNull() { addIns(new Instruction(Opcode.NULL)); }


    public void DoNewArray() { addIns(new Instruction(Opcode.NEW_ARRAY)); }


    public void DoAddArrayItem(boolean is_map) {
        if (!is_map)
            addIns(new Instruction(Opcode.ADD_ARRAY_ITEM));
        else
            addIns(new Instruction(Opcode.ADD_ARRAY_MAP_ITEM));
    }

    public void DoRequestArrayPointerItem(boolean is_reference) {
        if (is_reference)
            addIns(new Instruction(Opcode.ARRAY_GET_POINTER_ITEM_AS_REFERENCE));
        else addIns(new Instruction(Opcode.ARRAY_GET_POINTER_ITEM));
    }

    public void DoDereference() { addIns(new Instruction(Opcode.DEREFERENCE)); }


    public void DoPop() { addIns(new Instruction(Opcode.POP)); }


    public void DoNombre(double n,boolean is_int) {
        if(is_int)
            addIns(new IntIns(Opcode.INTEGER,(int)n));
        else
            addIns(new DoubleIns(Opcode.NUMBER,n));
    }


    public void DoString(String s) { addIns(new StringIns(Opcode.STRING,s)); }


    public void DoToString() {
        if (getIns(0).opcode != Opcode.STRING)
            addIns(new Instruction(Opcode.TOSTRING));
    }

    public void doFetchStatic(){
        addIns(new Instruction(Opcode.FETCH_CLASS_STATIC));
    }

    public void doFindClassVar(boolean isRef){
        addIns(new IntIns(Opcode.FIND_CLASS_VAR,isRef ? 1 : 0));
    }

    protected void doFindClassConst(String name){
        addIns(new StringIns(Opcode.FIND_CLASS_CONST,name));
    }

    protected void doFindClassFunction(String name){
        addIns(new StringIns(Opcode.FIND_CLASS_FUNCTION,name));
    }

    protected void doFetchClass(String name){
        if(name.equals("static")){
            addIns(new Instruction(Opcode.FETCH_CLASS_STATIC));
        }
        else if(name.equals("parent")){
            addIns(new Instruction(Opcode.FETCH_CLASS_PARENT));
        }
        else if(name.equals("self")){
            addIns(new Instruction(Opcode.FETCH_CLASS_SELF));
        }
        else{
            addIns(new StringIns(Opcode.FIND_CLASS,name));
        }
    }


    public void DoConcat(int count) { addIns(new IntIns(Opcode.CONCAT,count)); }


    public void DoRequestMember(boolean is_function) {
        if (is_function)
            addIns(new Instruction(Opcode.REQUEST_FUNCTION_MEMBER));
        else addIns(new Instruction(Opcode.REQUEST_MEMBER));
    }


    public void DoSubscript(boolean is_max) {
        if (is_max)
            addIns(new Instruction(Opcode.MAX_SUBSCRIPT));
        else
            addIns(new Instruction(Opcode.SUBSCRIPT));
    }

    public void DoFindVariable(String vname,boolean is_ref) {
        if (is_ref)
            addIns(new StringIns(Opcode.FIND_VARIABLE_AS_REFERENCE,vname));
        else
            addIns(new StringIns(Opcode.FIND_VARIABLE,vname));
    }


    public void DoFindVariableByName(boolean is_ref) {
        if (getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        if (is_ref)
            addIns(new Instruction(Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE));
        else
            addIns(new Instruction(Opcode.FIND_VARIABLE_BY_NAME));
    }


    public void CheckWritable() throws CompilationException {
        int op = getIns(0).opcode;
        if (op != Opcode.SUBSCRIPT &&
                op != Opcode.FIND_VARIABLE &&
                op != Opcode.REQUEST_MEMBER &&
                op != Opcode.FIND_VARIABLE_AS_REFERENCE &&
                op != Opcode.MAX_SUBSCRIPT &&
                op != Opcode.FUNCTION_CALL &&
                op != Opcode.PRE_DEC &&
                op != Opcode.PRE_INC &&
                op != Opcode.POST_INC &&
                op != Opcode.POST_DEC &&
                op != Opcode.FIND_CLASS_VAR)
            throw new CompilationException( " Invalid Right value.");
    }


    public void DoFunctionCall() { addIns(new Instruction(Opcode.FUNCTION_CALL)); }


    public void DoFindConst(String n) { addIns(new StringIns(Opcode.FIND_CONST,n)); }


    public void DoFindFunction(String name) { addIns(new StringIns(Opcode.FIND_FUNCTION,name)); }


    public void DoExit() { addIns(new Instruction(Opcode.EXIT)); }


    public void DoPrint() {
        if (getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.PRINT));
    }


    public void DoEcho() {
        if (getIns(0).opcode != Opcode.STRING && getIns(0).opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.ECHO));
    }


    public void convertRvalueToLvalue() throws CompilationException {
        CheckWritable();
        Instruction ins = getIns(0);
        switch (ins.opcode){
            case Opcode.FIND_VARIABLE:ins.opcode = Opcode.FIND_VARIABLE_AS_REFERENCE;break;
            case Opcode.SUBSCRIPT:ins.opcode = Opcode.SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.REQUEST_MEMBER:ins.opcode = Opcode.REQUEST_MEMBER_AS_REFERENCE;break;
            case Opcode.FIND_VARIABLE_BY_NAME:ins.opcode = Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE;break;
            case Opcode.MAX_SUBSCRIPT:ins.opcode = Opcode.MAX_SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.FUNCTION_CALL:addIns(new Instruction(Opcode.CHECK_REF));break;
            case Opcode.FIND_CLASS_VAR:((IntIns)ins).ins = 1;break;

        }
    }

    protected void doArgItem(){
        Instruction ins = getIns(0);
        switch (ins.opcode){
            case Opcode.FIND_VARIABLE:ins.opcode = Opcode.FIND_VARIABLE_AS_REFERENCE;break;
            case Opcode.SUBSCRIPT:ins.opcode = Opcode.SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.REQUEST_MEMBER:ins.opcode = Opcode.REQUEST_MEMBER_AS_REFERENCE;break;
            case Opcode.FIND_VARIABLE_BY_NAME:ins.opcode = Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE;break;
            case Opcode.MAX_SUBSCRIPT:ins.opcode = Opcode.MAX_SUBSCRIPT_AS_REFERENCE;break;
        }
    }


    public void DoBeginIfStatement() {
        IfContext con = new IfContext();
        IntIns ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
        addIns(ins);
        con.last_ins = ins;
        ifstmt_stack.push(con);
    }


    public void DoEndIfBlock() {
        IntIns ins = new IntIns(Opcode.GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins.ins = getLine();
        ifstmt_stack.peek().last_ins = null;
        ifstmt_stack.peek().pending_exit_ins.add(ins);
    }


    public void DoElseIfBlock() {
        IntIns ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins = ins;
    }


    public void DoEndIfStatement() {
        IfContext con = ifstmt_stack.pop();
        if (con.last_ins != null) /*if not true,there isn't an else block.*/
            con.last_ins.ins = getLine();
        con.finish(getLine());
    }


    public void DoConditionalExpr(int where) {
        switch (where){
            case 0:/*begin of first expression*/
                IntIns ins = new IntIns(Opcode.CONDITIONAL_GOTO);
                IntIns ins2 = new IntIns(Opcode.GOTO);
                conditional_expr_stack.push(ins2);
                addIns(ins);
                addIns(ins2);
                ins.ins = getLine();
                break;
            case 1:/*begin of second expression*/
                ins = new IntIns(Opcode.GOTO);
                addIns(ins);
                conditional_expr_stack.pop().ins = getLine();
                conditional_expr_stack.push(ins);
                break;
            case 2:/*end of expression*/
                conditional_expr_stack.pop().ins = getLine();
                break;
            default:throw new AssertionError("Unknown location in conditional expression:"+ where);
        }
    }


    public void DoWhileStatement(int where) {
        switch (where){
            case 0:/*begin of while*/
                DoubleIntIns ins = new DoubleIntIns(Opcode.BEGIN_LOOP);
                addIns(ins);
                ins.ins1 = getLine();
                loop_entry_stack.push(ins);
                break;
            case 1://end of expression,begin of block
                IntIns ins2 = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
                addIns(ins2);
                while_loop_escape_stack.push(ins2);
                break;
            case 2://end if the while statement
                DoubleIntIns entry_ins = loop_entry_stack.pop();
                IntIns ins3 = new IntIns(Opcode.GOTO,entry_ins.ins1);
                addIns(ins3);
                while_loop_escape_stack.pop().ins = getLine();
                entry_ins.ins2 = getLine();
                break;
            default:throw new AssertionError("Unknown location:" + where);
        }
    }

    public void DoDoWhileStatement(int where) {
        switch(where){
            case 0://begin of code block
                DoubleIntIns ins = new DoubleIntIns(Opcode.BEGIN_LOOP);
                addIns(ins);
                do_while_loop_entry_stack.push(getLine());
                loop_entry_stack.push(ins);
                break;
            case 1://end of code block,begin of expression
                loop_entry_stack.peek().ins1 = getLine();
                break;
            case 2://end of expression
                addIns(new IntIns(Opcode.CONDITIONAL_GOTO,do_while_loop_entry_stack.pop()));
                loop_entry_stack.pop().ins2 = getLine();
                break;
            default:throw new AssertionError("Unknown location in do-while:" + where);
        }
    }

    public void DoForStatement(int where) {
        switch(where){
            case 0://end of the first expression,begin of second expression
                addIns(new Instruction(Opcode.POP));
                DoubleIntIns ins = new DoubleIntIns(Opcode.BEGIN_LOOP);
                addIns(ins);
                ins.ins1 = getLine();
                loop_entry_stack.push(ins);
                for_loop_stack.push(new ForContext());
                break;
            case 1://end of second expression,begin of third expression
                DoubleIntIns ins2 = new DoubleIntIns(Opcode.OPTIONAL_GOTO);
                addIns(ins2);
                for_loop_stack.peek().cond_ins = ins2;
                for_loop_stack.peek().loop_line = getLine();
                break;
            case 2://end of third expression,begin of code block
                IntIns ins_goto = new IntIns(Opcode.GOTO,loop_entry_stack.peek().ins1);
                addIns(ins_goto);
                for_loop_stack.peek().cond_ins.ins1 = getLine();
                break;
            case 3://end of code block
                DoubleIntIns loopbegin = loop_entry_stack.pop();
                addIns(new IntIns(Opcode.GOTO,for_loop_stack.peek().loop_line));
                loopbegin.ins2 = getLine();
                for_loop_stack.peek().cond_ins.ins2 = getLine();
                for_loop_stack.pop();
                break;
            default:throw new AssertionError("Unknown location in for-statement:" + where);
        }
    }

    public void doForEachStatement(int where) {
        switch(where){
            case 0://end of the foreach_expr
                ForEachContext fctx = new ForEachContext();
                DoubleIntIns loop_entry = new DoubleIntIns(Opcode.BEGIN_LOOP);
                loop_entry_stack.push(loop_entry);
                foreach_context_stack.push(fctx);
                DoubleIntIns iterator_ins = new DoubleIntIns(Opcode.ARRAY_ITERATOR,0,0);
                addIns(iterator_ins);
                addIns(new IntIns(Opcode.TSTORE,fctx.getTempRegister()));
                addIns(loop_entry);
                loop_entry.ins1 = getLine();
                fctx.iterator_ins = iterator_ins;
                addIns(new IntIns(Opcode.TLOAD,fctx.getTempRegister()));
                addIns(new Instruction(Opcode.ITERATOR_END));
                fctx.escape_ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
                addIns(fctx.escape_ins);
                break;
            case 1://end of the whole statement
                fctx = foreach_context_stack.peek();
                addIns(new IntIns(Opcode.TLOAD,fctx.getTempRegister()));
                addIns(new Instruction(Opcode.ITERATOR_NEXT));
                addIns(new IntIns(Opcode.TSTORE,fctx.getTempRegister()));
                addIns(new IntIns(Opcode.GOTO,loop_entry_stack.peek().ins1));
                loop_entry_stack.pop().ins2 = getLine();
                fctx.escape_ins.ins = getLine();
                foreach_context_stack.pop().finish();
                break;
            default:throw new AssertionError("unknown position in foreach");
        }
    }

    protected void doForEachFirstExpr(boolean is_ref) throws CompilationException {
        convertRvalueToLvalue();
        ForEachContext fctx = foreach_context_stack.peek();
        fctx.iterator_ins.ins1 = is_ref ? 1 : 0;
        addIns(new IntIns(Opcode.TLOAD,fctx.getTempRegister()));
        fctx.first_expr_ins = new Instruction(Opcode.ARRAY_ITERATOR_GET);
        addIns(fctx.first_expr_ins);
        addIns(new Instruction(Opcode.ASSIGN));
        addIns(new Instruction(Opcode.POP));
    }

    protected void doForEachSecondExpr(boolean is_ref) throws CompilationException {
        convertRvalueToLvalue();
        ForEachContext fctx = foreach_context_stack.peek();
        fctx.iterator_ins.ins2 = is_ref ? 1 : 0;
        fctx.first_expr_ins.opcode = Opcode.MAP_ITERATOR_KEY;
        fctx.iterator_ins.opcode = Opcode.MAP_ITERATOR;
        addIns(new IntIns(Opcode.TLOAD,fctx.getTempRegister()));
        addIns(new Instruction(Opcode.MAP_ITERATOR_VALUE));
        addIns(new Instruction(Opcode.ASSIGN));
        addIns(new Instruction(Opcode.POP));
    }

    public void DoSwitchStatement(int where) {
        switch(where){
            case 0://begin of switch statement
                SwitchContext sctx = new SwitchContext();
                switchstmt_stack.push(sctx);
                DoubleIntIns ins = new DoubleIntIns(Opcode.BEGIN_LOOP);
                ins.ins1 = -1;
                addIns(ins);
                loop_entry_stack.push(ins);
                break;
            case 1://end of expression,begin of code block
                addIns(new IntIns(Opcode.TSTORE,switchstmt_stack.peek().treg_index));
                IntIns ins1 = new IntIns(Opcode.GOTO);
                addIns(ins1);
                switchstmt_stack.peek().last_ins = ins1;
                break;
            case 2://end of the statement
                SwitchContext ctx = switchstmt_stack.peek();
                if(ctx.first_default_line != -1){
                    IntIns skip_ins = new IntIns(Opcode.GOTO);
                    addIns(skip_ins);
                    ctx.last_ins.ins = getLine();
                    addIns(new IntIns(Opcode.GOTO,ctx.first_default_line));
                    skip_ins.ins = getLine();
                }
                else{
                    ctx.last_ins.ins = getLine();
                }
                loop_entry_stack.pop().ins2 = getLine();
                switchstmt_stack.pop().finish();
                break;
            default:throw new AssertionError("Unknown location in switch:" + where);
        }
    }

    public void DoSwitchLabel(int which) {
        SwitchContext sctx = switchstmt_stack.peek();
        switch(which){
            case 0://case label begin
                IntIns ins = new IntIns(Opcode.GOTO);
                addIns(ins);
                sctx.last_ins.ins = getLine();
                addIns(new IntIns(Opcode.TLOAD,sctx.treg_index));
                sctx.case_skip_ins = ins;
                break;
            case 1://case label end
                addIns(new Instruction(Opcode.NEQU));
                ins = new IntIns(Opcode.CONDITIONAL_GOTO);
                addIns(ins);
                sctx.last_ins = ins;
                sctx.case_skip_ins.ins = getLine();
                break;
            case 2://default label
                SwitchContext ctx = switchstmt_stack.peek();
                if(ctx.first_default_line == -1){
                    ctx.first_default_line = getLine();
                }
                break;
        }
    }

    public void DoBreakOrContinue(int which,boolean has_expr) {
        if(!has_expr)
            addIns(new IntIns(Opcode.INTEGER,1));
        switch(which){
            case 0://break
                addIns(new Instruction(Opcode.BREAK));
                break;
            case 1://continue
                addIns(new Instruction(Opcode.CONTINUE));
                break;
            default:throw new AssertionError("Unknown statement:" + which);
        }
    }

    public void DoBeginTry() {
        TryCatchContext ctx = new TryCatchContext();
        try_catch_stack.push(ctx);
        ctx.entry = new IntIns(Opcode.BEGIN_TRY);
        addIns(ctx.entry);

    }

    public void DoCatchBlock(boolean is_first,String vname,String typename) {
        TryCatchContext ctx = try_catch_stack.peek();

        IntIns ins = new IntIns(Opcode.GOTO);
        addIns(ins);
        ctx.escape_ins.add(ins);
        if(is_first) {
            addIns(new IntIns(Opcode.TSTORE,ctx.getTempReg()));
            ctx.entry.ins = getLine();
        }
        if((ins = ctx.last_ins) != null){
            ins.ins = getLine();
        }
        if(!typename.equals("")){
            addIns(new IntIns(Opcode.TLOAD,ctx.getTempReg()));
            DoFindClass(typename);
            DoInstanceOf();
            ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
            addIns(ins);
            ctx.last_ins = ins;
        }
        else{
            ctx.last_ins = null;
        }
        addIns(new IntIns(Opcode.TLOAD,ctx.getTempReg()));
        DoFindVariable(vname,true);
        doAssign(false);

    }

    public void DoEndTryCatchBlock() {
        TryCatchContext ctx = try_catch_stack.peek();
        IntIns tins = new IntIns(Opcode.GOTO);
        addIns(tins);
        if(ctx.last_ins != null){
            ctx.last_ins.ins = getLine();
        }
        int index = ctx.getTempReg();
        addIns(new IntIns(Opcode.TLOAD,index));
        addIns(new Instruction(Opcode.THROW));
        ctx.finish(getLine());
        tins.ins = getLine();

    }

    public void DoReturnOrThrow(int which) {
        switch(which){
            case 0://return
                addIns(new Instruction(Opcode.RETURN));
                break;
            case 1://throw
                addIns(new Instruction(Opcode.THROW));
                break;
            default:throw new AssertionError("not return nor throw:" + which);
        }
    }

    public void DoUnset() throws CompilationException {
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.UNSET));
    }

    public void DoDeclareConst(String name) {
        addIns(new StringIns(Opcode.DECLARE_CONST,name));
    }

    public void DoGlobal(String name) {
        addIns(new StringIns(Opcode.GLOBAL,name));
    }

    public void finishCompiling() {
        leaveScope();
        addIns(new IntIns(Opcode.INTEGER,0));
        addIns(new Instruction(Opcode.EXIT));
    }

    public void doBeginFunctionDeclaration(String fname, boolean is_ref) {
        current_func = new FunctionDeclareIns(fname);
        addIns(function_skip_ins = new IntIns(Opcode.GOTO));
        current_func.entry_line = getLine();
        func_default_values_temp_reg = regmgr.requestTempReg();
    }

    public void doFunctionParamItem(String vname, String typename,boolean is_ref,boolean hasDefault) {
        current_func.addArg(vname,typename,is_ref);
        if(hasDefault){
            addIns(new IntIns(Opcode.TSTORE,func_default_values_temp_reg));
            addIns(new StringIns(Opcode.FIND_VARIABLE,vname));
            addIns(new Instruction(Opcode.IS_NULL));
            IntIns gotoassign = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
            addIns(gotoassign);
            addIns(new StringIns(Opcode.FIND_VARIABLE_AS_REFERENCE,vname));
            addIns(new IntIns(Opcode.TLOAD,func_default_values_temp_reg));
            addIns(new Instruction(Opcode.ASSIGN));
            addIns(new Instruction(Opcode.POP));
            gotoassign.ins = getLine();
        }
    }

    public void doFunctionUse(String vname,boolean is_ref){
        current_func.use(vname,is_ref);
    }

    public void doEndFunction() {
        if(getIns(0).opcode != Opcode.RETURN){
            addIns(new Instruction(Opcode.NULL));
            addIns(new Instruction(Opcode.RETURN));
        }
        function_skip_ins.ins = getLine();
        regmgr.freeTempReg(func_default_values_temp_reg);
        scope_stack.peek().addFunction(current_func);
        current_func = null;
    }

    public void enterScope() {
        scope_stack.push(new ScopeContext());
    }

    public void leaveScope() {
        scope_stack.pop().leave();
    }

    public void doAssign(boolean is_array) {
        if(!is_array){
            addIns(new Instruction(Opcode.ASSIGN));
        }
        else{
            addIns(new Instruction(Opcode.ARRAY_ASSIGN));
        }

    }

    public void DoReference() throws CompilationException {
        convertRvalueToLvalue();
    }

    public void DoNew() {
        if(getIns(0).opcode == Opcode.FUNCTION_CALL)
            getIns(0).opcode = Opcode.NEW;
        else
            addIns(new Instruction(Opcode.NEW));
    }

    public void DoFindClass(String name) {
        addIns(new StringIns(Opcode.FIND_CLASS,name));
    }

    public void DoInstanceOf() {
        addIns(new Instruction(Opcode.INSTANCEOF));
    }

    public void DoPackArg(int count) {
        addIns(new IntIns(Opcode.PACK_ARG,count));
    }

    public String getCurrentNameSpace() {
        return ns_stack.peek();
    }

    public void DoEnterNameSpace(String name) {
        ns_stack.push(ns_stack.peek() + name + "\\");
    }

    public void DoLeaveNameSpace() {
        ns_stack.pop();
    }

    class IfContext{
        public List<IntIns> pending_exit_ins;
        public IntIns last_ins;
        public IfContext(){
            pending_exit_ins = new ArrayList<>();
        }
        public void finish(int line){
            for(IntIns ins : pending_exit_ins){
                ins.ins = line;
            }
        }
    }
    class SwitchContext{
        public IntIns last_ins;
        public int first_default_line = -1;
        public int treg_index;
        public IntIns case_skip_ins;

        public SwitchContext(){
            treg_index = regmgr.requestTempReg();
        }
        public void finish(){
            regmgr.freeTempReg(treg_index);
        }
    }
    class TryCatchContext{
        public IntIns entry;
        public List<IntIns> escape_ins;
        public IntIns last_ins;
        private int treg_index;
        public TryCatchContext(){
            escape_ins = new ArrayList<>();
            treg_index = regmgr.requestTempReg();
        }
        public void finish(int line){
            for(IntIns ins : escape_ins){
                ins.ins = line;
            }
            regmgr.freeTempReg(treg_index);
        }
        public int getTempReg(){
            return treg_index;
        }
    }
    class ForContext{
        DoubleIntIns cond_ins;
        int loop_line;
    }
    class ForEachContext{
        private int treg_index;
        IntIns escape_ins;
        DoubleIntIns iterator_ins;
        Instruction first_expr_ins;
        public ForEachContext(){
            treg_index = regmgr.requestTempReg();
        }
        public void finish(){
            regmgr.freeTempReg(treg_index);
        }
        public int getTempRegister(){
            return treg_index;
        }
    }
    class ScopeContext{
        public int startline;
        public IntIns ins_goto;
        private List<FunctionDeclareIns> functions;

        public ScopeContext(){
            ins_goto = new IntIns(Opcode.GOTO);
            addIns(ins_goto);
            startline = getLine();
            functions = new ArrayList<>();
        }

        public void addFunction(FunctionDeclareIns ins){
            functions.add(ins);
        }

        public void leave(){
            IntIns skip_ins = new IntIns(Opcode.GOTO);
            addIns(skip_ins);
            ins_goto.ins = getLine();
            for(Instruction ins : functions){
                addIns(ins);
            }
            addIns(new IntIns(Opcode.GOTO,startline));
            skip_ins.ins = getLine();
        }
    }

}
