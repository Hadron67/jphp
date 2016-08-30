package com.hadroncfy.jphp.jzend.compile;
import com.hadroncfy.jphp.jzend.compile.ins.*;
import com.hadroncfy.jphp.jzend.types.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/** Created by cfy on 16-5-12. */
public class JZendCompiler implements WarningReporter {
    public static final int MODIFIER_PUBLIC = 0;
    public static final int MODIFIER_PRIVATE = 1;
    public static final int MODIFIER_PROTECTED = 2;
    public static final int MODIFIER_STATIC = 3;
    public static final int MODIFIER_FINAL = 4;
    public static final int MODIFIER_ABSTRACT = 5;

    private String fname;
    private LineGetter lineGetter;
    private Routine top_routine;

    private Routine current_routine;
    private CompilerContext ctx;

    private Stack<String> ns_stack = new Stack<>();
    private List<Warning> warnings = new LinkedList<>();
    private Stack<IfContext> ifstmt_stack = new Stack<>();
    private Stack<SwitchContext> switchstmt_stack = new Stack<>();
    private Stack<IntIns> while_loop_escape_stack = new Stack<>();
    private Stack<Integer> do_while_loop_entry_stack = new Stack<>();
    private Stack<ForContext> for_loop_stack = new Stack<>();
    private Stack<ForEachContext> foreach_context_stack = new Stack<>();
    private Stack<IntIns> conditional_expr_stack = new Stack<>();
    //private Stack<ZendFunction> func_stack = new Stack<>();
    private Stack<ZendClass> class_stack = new Stack<>();
    private Stack<Zval> const_eval_stack = new Stack<>();
    private MemberBuilder mbuilder = new MemberBuilder(this);
    private RegMgr regmgr = new RegMgr();


    private FunctionHead currentHead;


    private int getLine(){ return current_routine.getLine(); }

    protected Instruction addIns(Instruction ins){
        ins.line = lineGetter.getLine();
        current_routine.addIns(ins);
        return ins;
    }

    private Instruction getLastIns(){
        return current_routine.getLastIns();
    }

    protected CompilationException generateException(String msg){
        return new CompilationException(lineGetter.getLine(),lineGetter.getColumn(),fname,msg);
    }

    public void addWarning(String msg){
        warnings.add(new Warning(Warning.WARN,msg,lineGetter.getLine(),lineGetter.getColumn(),fname));
    }

    public void addNotice(String msg){
        warnings.add(new Warning(Warning.NOTICE,msg,lineGetter.getLine(),lineGetter.getColumn(),fname));
    }

    public void printWarnings(PrintStream ps){
        for(Warning w : warnings){
            ps.println(w.toString());
        }
    }

    public boolean hasWarning(){
        return !warnings.isEmpty();
    }

    public JZendCompiler(){
        this(Routine.newGlobalRoutine());
    }

    public JZendCompiler(Routine routine){
        ns_stack.push("");

        ctx = CompilerContext.newGlobalContext();

        top_routine = current_routine = routine;
    }

    public Routine compile(InputStream stream,String fname) throws CompilationException, ParseException {
        this.fname = fname;
        JZendParser parser = new JZendParser(stream);
        lineGetter = parser;
        parser.setCompiler(this);
        parser.Parse();
        finishCompiling();
        return top_routine;
    }

    public Routine compile(InputStream stream) throws ParseException, CompilationException {
        return compile(stream, "-");
    }

    protected void DoBinaryOptr(int token_type) {
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
            default:throw new AssertionError("Unknown binary operator:" + token_type);
        }
        addIns(new Instruction((op)));
    }

    protected void DoUnaryOptr(int token_type) {
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

    protected void DoPostIncOrDec(boolean is_dec) {
        addIns(new Instruction(is_dec ? Opcode.POST_DEC : Opcode.POST_INC));
    }

    protected void DoDup() {
        addIns(new Instruction(Opcode.DUP));
    }


    protected void DoNull() { addIns(new Instruction(Opcode.NULL)); }


    protected void DoNewArray() { addIns(new Instruction(Opcode.NEW_ARRAY)); }


    protected void DoAddArrayItem(boolean is_map) {
        if (!is_map)
            addIns(new Instruction(Opcode.ADD_ARRAY_ITEM));
        else
            addIns(new Instruction(Opcode.ADD_ARRAY_MAP_ITEM));
    }


    protected void DoDereference() { addIns(new Instruction(Opcode.DEREFERENCE)); }


    protected void DoPop() { addIns(new Instruction(Opcode.POP)); }


    protected void DoNombre(double n,boolean is_int) {
        if(is_int)
            addIns(new IntIns(Opcode.INTEGER,(int)n));
        else
            addIns(new DoubleIns(Opcode.NUMBER,n));
    }


    protected void DoString(String s) { addIns(new StringIns(Opcode.STRING,s)); }


    protected void DoToString() {
        if (getLastIns().opcode != Opcode.STRING)
            addIns(new Instruction(Opcode.TOSTRING));
    }

    protected void doCast(int what){
        switch(what){
            case 0:
                addIns(new Instruction(Opcode.INT_CAST));
                break;
            case 1:
                addIns(new Instruction(Opcode.FLOAT_CAST));
                break;
            case 2:
                addIns(new Instruction(Opcode.STRING_CAST));
                break;
            case 3:
                addIns(new Instruction(Opcode.ARRAY_CAST));
                break;
            case 4:
                addIns(new Instruction(Opcode.OBJECT_CAST));
                break;
            case 5:
                addIns(new Instruction(Opcode.BOOL_CAST));
                break;
            case 6:
                addIns(new Instruction(Opcode.UNSET_CAST));
                break;
            default:throw new AssertionError("unknown cast type:" + what);
        }
    }

    protected void doBeginSilent(){
        addIns(new Instruction(Opcode.BEGIN_SILENT));
    }

    protected void doEndSilent(){
        addIns(new Instruction(Opcode.END_SILENT));
    }

    protected void doIncludeOrEval(int type){
        addIns(new IntIns(Opcode.INCLUDE_OR_EVAL,type));
    }

    protected void doIsSet() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.IS_SET));
    }

    protected void doAdditionalIsSet() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.IS_SET));
        addIns(new Instruction(Opcode.AND));
    }

    protected void doIsEmpty() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.IS_EMPTY));
    }

    protected void doFetchStatic(){
        addIns(new Instruction(Opcode.FETCH_CLASS_STATIC));
    }

    protected void doFindClassVar(boolean isRef){
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


    protected void doStaticConstInt(int value){
        const_eval_stack.push(new Zint(value));
    }

    protected void doStaticConstFloat(double f){
        const_eval_stack.push(new Zfloat(f));
    }

    protected void doStaticConstString(String s){
        const_eval_stack.push(new Zstring(s));
    }

    protected void doStaticConstPosOptr() throws CompilationException{
        Zval result = const_eval_stack.pop().pos();
        if(result == null){
            throw generateException("'+' is not supported");
        }
        const_eval_stack.push(result);
    }

    protected void doStaticConstNegOptr() throws CompilationException{
        Zval result = const_eval_stack.pop().neg();
        if(result == null){
            throw generateException("'+' is not supported");
        }
        const_eval_stack.push(result);
    }

    protected void doStaticConstArray(){
        const_eval_stack.push(new Zarray());
    }

    protected void doStaticConstArrayAddItem(){
        Zval zval = const_eval_stack.pop();
        Zarray array = (Zarray) const_eval_stack.pop();
        array.addItem(zval);
        const_eval_stack.push(array);
    }

    protected void doStaticConstArrayAddMapItem(){
        Zval value = const_eval_stack.pop();
        Zval key = const_eval_stack.pop();
        Zarray array = (Zarray) const_eval_stack.pop();
        if(!Zarray.checkKeyType(key)){
            addWarning("illegal offset type \"" + key.getTypeName() + "\",this element will be ignored");
        }
        array.addItem(key,value);
        const_eval_stack.push(array);
    }

    protected void doStaticConstFindConst(String cname){
        Zval val = resolveConst(cname);
        if(val == null){
            addNotice("Use of undefined constant " + cname + " - assumed '" + cname + "' in");
            val = new Zstring(cname);
        }
        const_eval_stack.push(val);
    }

    protected Zval resolveConst(String cname){
        Zval val = top_routine.getConst(cname);
        if(val == null){
            if(cname.equals("NULL")){
                return Znull.NULL;
            }
            else if(cname.equals("true")){
                return Zbool.TRUE;
            }
            else if(cname.equals("false")){
                return Zbool.FALSE;
            }
            else
                return null;
        }
        return val;
    }

    protected String buildNameSpaceName(String s){
        if(!s.equals("NULL")){
            return getCurrentNameSpace() + s;
        }
        return s;
    }

    protected void doBeginClass(ZendClass.ClassType type,String cname) throws CompilationException {
        if(current_routine.getZClass(cname) != null){
            throw generateException("Cannot redeclare class " + cname);
        }
        class_stack.push(ZendClass.createClassByType(cname,type));
    }

    protected void doClassExtends(String cname) throws CompilationException{
        try {
            class_stack.peek().addExtends(cname);
        } catch (IllegalClassOperationException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doClassImplement(String cname) throws CompilationException{
        try {
            class_stack.peek().addImplement(cname);
        } catch (IllegalClassOperationException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doClassConst(String cname) throws CompilationException{
        Zval value = const_eval_stack.pop();
        try {
            class_stack.peek().addConst(cname,value);
        } catch (IllegalClassOperationException | RedeclareException  e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doPrepareModifierParse(){
        mbuilder.reInit();
    }

    protected void doModifier(int m) throws CompilationException{

        try {
            switch (m) {
                case MODIFIER_PUBLIC:
                    mbuilder.addAccess(Access.PUBLIC);
                    break;
                case MODIFIER_PROTECTED:
                    mbuilder.addAccess(Access.PROTECTED);
                    break;
                case MODIFIER_PRIVATE:
                    mbuilder.addAccess(Access.PRIVATE);
                    break;
                case MODIFIER_FINAL:
                    mbuilder.addFinal();
                    break;
                case MODIFIER_STATIC:
                    mbuilder.addStatic();
                    break;
                case MODIFIER_ABSTRACT:
                    mbuilder.addAbstract();
                    break;
                default:
                    assert false;
            }
        }
        catch (IllegalModifierException e){
            throw generateException(e.getMessage());
        }
    }

    protected void doAddVarMember(String vname,boolean hasDefault) throws CompilationException {
        Zval value = hasDefault ? const_eval_stack.pop() : Znull.NULL;
        assert const_eval_stack.isEmpty();
        try {
            class_stack.peek().addVar(vname,mbuilder.buildVar(value));
        } catch (IllegalClassOperationException | IllegalModifierException | RedeclareException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doPrepareMethod(String name,boolean isRef){
        currentHead = new MethodHead(name,isRef,class_stack.peek());


    }

    private boolean inInterface(){
        return !class_stack.isEmpty() && (class_stack.peek() instanceof Interface);
    }

    protected void doBeginMethodBody(boolean hasBody) throws CompilationException {
        ClassMember<ZendMethod> method;
        try {
            method = mbuilder.buildMethod((MethodHead) currentHead,inInterface());
        } catch (IllegalModifierException e) {
            throw generateException(e.getMessage());
        }

        if(!inInterface()) {
            if (hasBody && method.member.isAbstract()) {
                throw generateException("Abstract function " + currentHead.getFullName() + "() cannot contain body");
            }
            if (!hasBody && !method.member.isAbstract()) {
                throw generateException("Non-abstract method " + currentHead.getFullName() + "() must contain body");
            }
        }
        else{
            if(hasBody){
                throw generateException("Interface function " + currentHead.getFullName() + "() cannot contain body");
            }
        }

        try {
            class_stack.peek().addMethod(method);
        } catch (RedeclareException e) {
            throw generateException(e.getMessage());
        } catch (InvalidMethodException e) {
            addWarning(e.getMessage());
        }


        if(hasBody) {
            ZendFunction body = method.member.getBody();
            body.setBody(current_routine.newSubRoutine());

            current_routine = body.getBody();
            //func_stack.push(method.member.body);
            ctx = ctx.newSubContext();
        }
    }

    protected void doEndMethod(){
        doEndFunction();
    }


    protected void doEndClass() throws CompilationException {
        ZendClass clazz = class_stack.pop();
        try {
            clazz.finishParsing();
        } catch (IllegalModifierException e) {
            throw generateException(e.getMessage());
        }
        top_routine.addClass(clazz);
    }

    protected void doAddUse(String s) throws CompilationException {
        try {
            class_stack.peek().addUse(s);
        } catch (IllegalClassOperationException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doAddTraitAlias(TraitAlias alias) throws CompilationException {
        try {
            class_stack.peek().addAliasItem(alias);
        } catch (IllegalClassOperationException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void doAddTraitExclude(TraitExcluder te) throws CompilationException {
        try {
            class_stack.peek().addExcluder(te);
        } catch (IllegalClassOperationException e) {
            throw generateException(e.getMessage());
        }
    }

    protected void DoConcat(int count) { addIns(new IntIns(Opcode.CONCAT,count)); }


    protected void DoRequestMember(boolean is_function) {
        if (is_function)
            addIns(new Instruction(Opcode.REQUEST_FUNCTION_MEMBER));
        else addIns(new Instruction(Opcode.REQUEST_MEMBER));
    }


    protected void DoSubscript(boolean is_max) {
        if (is_max)
            addIns(new Instruction(Opcode.MAX_SUBSCRIPT));
        else
            addIns(new Instruction(Opcode.SUBSCRIPT));
    }

    protected void DoFindVariable(String vname,boolean is_ref) {
        if (is_ref)
            addIns(new StringIns(Opcode.FIND_VARIABLE_AS_REFERENCE,vname));
        else
            addIns(new StringIns(Opcode.FIND_VARIABLE,vname));
    }


    protected void DoFindVariableByName(boolean is_ref) {
        if (getLastIns().opcode != Opcode.STRING && getLastIns().opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        if (is_ref)
            addIns(new Instruction(Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE));
        else
            addIns(new Instruction(Opcode.FIND_VARIABLE_BY_NAME));
    }


    protected void CheckWritable() throws CompilationException {
        int op = getLastIns().opcode;
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
            throw generateException("Invalid right-hand value");
    }


    protected void DoFunctionCall() { addIns(new Instruction(Opcode.FUNCTION_CALL)); }


    protected void doFindConst(String n,boolean byname) {
        if(n.equals("NULL")){
            addIns(new Instruction(Opcode.NULL));
        }
        else {
            if(byname){
                addIns(new Instruction(Opcode.FIND_CONST_BY_NAME));
            }
            else
                addIns(new StringIns(Opcode.FIND_CONST, n));
        }
    }


    protected void DoFindFunction(String name) { addIns(new StringIns(Opcode.FIND_FUNCTION,name)); }


    protected void DoExit() { addIns(new Instruction(Opcode.EXIT)); }


    protected void DoPrint() {
        if (getLastIns().opcode != Opcode.STRING && getLastIns().opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.PRINT));
    }


    protected void DoEcho() {
        if (getLastIns().opcode != Opcode.STRING && getLastIns().opcode != Opcode.CONCAT)
            addIns(new Instruction(Opcode.TOSTRING));
        addIns(new Instruction(Opcode.ECHO));
    }


    protected void convertRvalueToLvalue() throws CompilationException {
        CheckWritable();
        Instruction ins = getLastIns();
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
        Instruction ins = getLastIns();
        switch (ins.opcode){
            case Opcode.FIND_VARIABLE:ins.opcode = Opcode.FIND_VARIABLE_AS_REFERENCE;break;
            case Opcode.SUBSCRIPT:ins.opcode = Opcode.SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.REQUEST_MEMBER:ins.opcode = Opcode.REQUEST_MEMBER_AS_REFERENCE;break;
            case Opcode.FIND_VARIABLE_BY_NAME:ins.opcode = Opcode.FIND_VARIABLE_BY_NAME_AS_REFERENCE;break;
            case Opcode.MAX_SUBSCRIPT:ins.opcode = Opcode.MAX_SUBSCRIPT_AS_REFERENCE;break;
            case Opcode.FIND_CLASS_VAR:((IntIns)ins).ins = 1;
        }
    }


    protected void DoBeginIfStatement() {
        IfContext con = new IfContext();
        IntIns ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
        addIns(ins);
        con.last_ins = ins;
        ifstmt_stack.push(con);
    }


    protected void DoEndIfBlock() {
        IntIns ins = new IntIns(Opcode.GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins.ins = getLine();
        ifstmt_stack.peek().last_ins = null;
        ifstmt_stack.peek().pending_exit_ins.add(ins);
    }


    protected void DoElseIfBlock() {
        IntIns ins = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
        addIns(ins);
        ifstmt_stack.peek().last_ins = ins;
    }


    protected void DoEndIfStatement() {
        IfContext con = ifstmt_stack.pop();
        if (con.last_ins != null) /*if not true,there isn't an else block.*/
            con.last_ins.ins = getLine();
        con.finish(getLine());
    }


    protected void DoConditionalExpr(int where) {
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


    protected void DoWhileStatement(int where) {
        switch (where){
            case 0://begin of while
//                DoubleIntIns ins = new DoubleIntIns(Opcode.BEGIN_LOOP);
//                addIns(ins);
//                ins.ins1 = getLine();
//                loop_entry_stack.push(ins);
                ctx.enterLoop(getLine());
                break;
            case 1://end of expression,begin of block
                IntIns ins2 = new IntIns(Opcode.CONDITIONAL_NOT_GOTO);
                addIns(ins2);
                while_loop_escape_stack.push(ins2);
                break;
            case 2://end if the while statement
                //DoubleIntIns entry_ins = loop_entry_stack.pop();
                IntIns ins3 = new IntIns(Opcode.GOTO,ctx.getLoopStartLine());
                addIns(ins3);
                while_loop_escape_stack.pop().ins = getLine();
                //entry_ins.ins2 = getLine();
                ctx.leaveLoop(getLine());
                break;
            default:throw new AssertionError("Unknown location:" + where);
        }
    }

    protected void DoDoWhileStatement(int where) {
        switch(where){
            case 0://begin of code block
                do_while_loop_entry_stack.push(getLine());
                break;
            case 1://end of code block,begin of expression
                ctx.enterLoop(getLine());
                break;
            case 2://end of expression
                addIns(new IntIns(Opcode.CONDITIONAL_GOTO,do_while_loop_entry_stack.pop()));
                ctx.leaveLoop(getLine());
                break;
            default:throw new AssertionError("Unknown location in do-while:" + where);
        }
    }

    protected void DoForStatement(int where) {
        switch(where){
            case 0://end of the first expression,begin of second expression
                addIns(new Instruction(Opcode.POP));
                ctx.enterLoop(getLine());
                for_loop_stack.push(new ForContext());
                break;
            case 1://end of second expression,begin of third expression
                DoubleIntIns ins2 = new DoubleIntIns(Opcode.OPTIONAL_GOTO);
                addIns(ins2);
                for_loop_stack.peek().cond_ins = ins2;
                for_loop_stack.peek().loop_line = getLine();
                break;
            case 2://end of third expression,begin of code block
                IntIns ins_goto = new IntIns(Opcode.GOTO,ctx.getLoopStartLine());
                addIns(ins_goto);
                for_loop_stack.peek().cond_ins.ins1 = getLine();
                break;
            case 3://end of code block
                addIns(new IntIns(Opcode.GOTO,for_loop_stack.peek().loop_line));
                for_loop_stack.peek().cond_ins.ins2 = getLine();
                ctx.leaveLoop(getLine());
                for_loop_stack.pop();
                break;
            default:throw new AssertionError("Unknown location in for-statement:" + where);
        }
    }

    protected void doForEachStatement(int where) {
        switch(where){
            case 0://end of the foreach_expr
                ForEachContext fctx = new ForEachContext();
                foreach_context_stack.push(fctx);
                DoubleIntIns iterator_ins = new DoubleIntIns(Opcode.ARRAY_ITERATOR,0,0);
                addIns(iterator_ins);
                addIns(new IntIns(Opcode.TSTORE,fctx.getTempRegister()));
                ctx.enterLoop(getLine());
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
                addIns(new IntIns(Opcode.GOTO,ctx.getLoopStartLine()));
                ctx.leaveLoop(getLine());
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

    protected void DoSwitchStatement(int where) {
        switch(where){
            case 0://begin of switch statement
                SwitchContext sctx = new SwitchContext();
                switchstmt_stack.push(sctx);
                ctx.enterSwitch();
                break;
            case 1://end of expression,begin of code block
                addIns(new IntIns(Opcode.TSTORE,switchstmt_stack.peek().treg_index));
                IntIns ins1 = new IntIns(Opcode.GOTO);
                addIns(ins1);
                switchstmt_stack.peek().last_ins = ins1;
                break;
            case 2://end of the statement
                SwitchContext sctx2 = switchstmt_stack.peek();
                if(sctx2.first_default_line != -1){
                    IntIns skip_ins = new IntIns(Opcode.GOTO);
                    addIns(skip_ins);
                    sctx2.last_ins.ins = getLine();
                    addIns(new IntIns(Opcode.GOTO,sctx2.first_default_line));
                    skip_ins.ins = getLine();
                }
                else{
                    sctx2.last_ins.ins = getLine();
                }
                ctx.leaveSwitch(getLine());
                switchstmt_stack.pop().finish();
                break;
            default:throw new AssertionError("Unknown location in switch:" + where);
        }
    }

    protected void DoSwitchLabel(int which) {
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

    protected void DoBreakOrContinue(int which,boolean has_expr) throws CompilationException {
        if(!has_expr)
            addIns(new IntIns(Opcode.INTEGER,1));
        switch(which){
            case 0://break
                if(!ctx.isBreakAvailable()){
                    throw generateException("cannot break here.");
                }
                addIns(ctx.newBreakInstruction());
                break;
            case 1://continue
                if(!ctx.isContinueAvailable()){
                    throw generateException("cannot continue here.");
                }
                addIns(ctx.newContinueInstruction());
                break;
            default:throw new AssertionError("Unknown statement:" + which);
        }
    }

    protected void DoBeginTry() {
        this.ctx.enterTryBlock(getLine());
    }

    protected void DoCatchBlock(boolean is_first,String vname,String typename) {

        if(is_first) {
            ctx.leaveTryBlock(getLine());
        }

        int from = ctx.getTryStart();
        int to = ctx.getTryEnd();

        current_routine.addExceptionItem(from,to,getLine(),typename,vname);
    }

    protected void DoEndTryCatchBlock() {
        ctx.endTryCatch();

    }

    protected void DoReturnOrThrow(int which) {
        switch(which){
            case 0://return
                current_routine.hasReturn = true;
                addIns(new Instruction(Opcode.RETURN));
                break;
            case 1://throw
                addIns(new Instruction(Opcode.THROW));
                break;
            default:throw new AssertionError("not return nor throw:" + which);
        }
    }

    protected void DoUnset() throws CompilationException {
        convertRvalueToLvalue();
        addIns(new Instruction(Opcode.UNSET));
    }

    protected void doDeclareConst(String name) {
        if(current_routine.getConst(name) != null){
            addNotice("Constant " + name + " already defined");
        }
        else {
            Zval var = const_eval_stack.pop();
            if (!const_eval_stack.isEmpty()) {
                throw new AssertionError("this stack should be empty now.");
            }
            top_routine.addConst(name, var);
        }
    }

    protected void DoGlobal(String name) {
        addIns(new StringIns(Opcode.GLOBAL,name));
    }

    private void finishCompiling() {
        addIns(new IntIns(Opcode.INTEGER,0));
        addIns(new Instruction(Opcode.EXIT));
        top_routine.loopTable = ctx.getLoopTable();
    }

    protected void doBeginFunctionDeclaration(String fname, boolean is_ref) throws CompilationException {
        ZendFunction f;
        if((f = current_routine.getFunction(fname)) != null){
            throw generateException("Cannot redeclare " + fname + "() (previously declared in " + f.getHead().filename +":" + f.getStartLine() + ")");
        }
        currentHead = new FunctionHead(fname,is_ref);
        currentHead.filename = this.fname;
    }

    protected void doBeginFunctionBody(boolean isAnonymous){
        ZendFunction func = new ZendFunction(currentHead,current_routine.newSubRoutine());
        if(!isAnonymous){
            current_routine.addFunction(func);
        }
        else{
            addIns(new NewFuncIns(func));
        }
        currentHead = null;
        current_routine = func.getBody();
        ctx = ctx.newSubContext();
        //func_stack.push(func);
    }

    protected void doFunctionParamItem(String vname, String typename,boolean is_ref,boolean hasDefault) throws CompilationException {
        if(hasDefault){
            Zval val = const_eval_stack.pop();
            if(!typename.equals("") && !(val instanceof Znull)){
                throw generateException("Default value for parameters with a class type hint can only be NULL");
            }
            currentHead.addArg(vname,typename,is_ref,val);
            assert const_eval_stack.isEmpty();
        }
        else{
            currentHead.addArg(vname,typename,is_ref);
        }
    }

    protected void doFunctionUse(String vname,boolean is_ref){
        currentHead.addUse(vname,is_ref);
    }

    protected void doEndFunction() {

        if(!current_routine.hasReturn){
            addIns(new Instruction(Opcode.NULL));
            addIns(new Instruction(Opcode.RETURN));
        }

        current_routine.loopTable = ctx.getLoopTable();
        current_routine = current_routine.parent();
        ctx = ctx.getParent();

    }


    protected void doAssign(boolean is_array) {
        if(!is_array){
            addIns(new Instruction(Opcode.ASSIGN));
        }
        else{
            addIns(new Instruction(Opcode.ARRAY_ASSIGN));
        }
    }

    protected void DoReference() throws CompilationException {
        convertRvalueToLvalue();
    }

    protected void DoNew() {
        if(getLastIns().opcode == Opcode.FUNCTION_CALL)
            getLastIns().opcode = Opcode.NEW;
        else
            addIns(new Instruction(Opcode.NEW));
    }

    protected void DoFindClass(String name) {
        addIns(new StringIns(Opcode.FIND_CLASS,name));
    }

    protected void DoInstanceOf() {
        addIns(new Instruction(Opcode.INSTANCEOF));
    }

    protected void DoPackArg(int count) {
        addIns(new IntIns(Opcode.PACK_ARG,count));
    }

    protected String getCurrentNameSpace() {
        return ns_stack.peek();
    }

    protected void DoEnterNameSpace(String name) {
        ns_stack.push(ns_stack.peek() + name + "\\");
    }

    protected void DoLeaveNameSpace() {
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

}
