package com.hadroncfy.jphp.jzend.compile;
import com.hadroncfy.jphp.jzend.compile.ins.*;
import com.hadroncfy.jphp.jzend.ins.*;
import com.hadroncfy.jphp.jzend.ins.Instruction;
import com.hadroncfy.jphp.jzend.ins.StringIns;
import com.hadroncfy.jphp.jzend.types.*;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.ArrayIterator;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Prefixable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

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
    private Stack<ConditionalGotoIns> while_loop_escape_stack = new Stack<>();
    private Stack<Integer> do_while_loop_entry_stack = new Stack<>();
    private Stack<ForContext> for_loop_stack = new Stack<>();
    private Stack<ForEachContext> foreach_context_stack = new Stack<>();
    private Stack<ConditionalExprContext> conditional_expr_stack = new Stack<>();
    private Stack<ZendClass> class_stack = new Stack<>();
    private Stack<Zval> const_eval_stack = new Stack<>();
    private MemberBuilder mbuilder = new MemberBuilder(this);
    private RegMgr regmgr = new RegMgr();

    private Stack<String> function_name_stack = new Stack<>();


    private FunctionHead currentHead;


    protected int getLine(){ return current_routine.getLine(); }

    protected String getFileName(){
        return fname;
    }

    protected Instruction addIns(Instruction ins){
        //ins.line = lineGetter.getLine();
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

    protected Instruction getIns(int i){
        return current_routine.getIns(i);
    }

    protected void setIns(int i,Instruction ins){
        current_routine.setIns(i,ins);
    }

    protected void setLastIns(Instruction ins){
        current_routine.setIns(getLine() - 1,ins);
    }

    protected String getFunctionName(){
        return function_name_stack.isEmpty() ? "" : function_name_stack.peek();
    }

    protected String getMethodName(){
        return getClassName() + (function_name_stack.isEmpty() ? "" : "::" + function_name_stack.peek());
    }

    protected String getClassName(){
        return class_stack.isEmpty() ? "" : class_stack.peek().getName();
    }



    protected void DoBinaryOptr(int token_type) {
        int op = 0;
        Instruction ins;
        switch (token_type){
            case JZendParserConstants.AND: { ins = new BinaryOperatorIns(Operator.BINARY_AND);break; }
            case JZendParserConstants.OR: { ins = new BinaryOperatorIns(Operator.BINARY_OR);break; }
            case JZendParserConstants.XOR: { ins = new BinaryOperatorIns(Operator.BINARY_BIT_XOR);break; }
            case JZendParserConstants.BITAND: { ins = new BinaryOperatorIns(Operator.BINARY_BIT_AND);break; }
            case JZendParserConstants.BITOR: { ins = new BinaryOperatorIns(Operator.BINARY_BIT_OR);break; }
            case JZendParserConstants.BITXOR: { ins = new BinaryOperatorIns(Operator.BINARY_BIT_XOR);break; }
            case JZendParserConstants.EQU: { ins = new BinaryOperatorIns(Operator.BINARY_EQUAL);break; }
            case JZendParserConstants.NEQU: { ins = new BinaryOperatorIns(Operator.BINARY_NOT_EQUAL);break; }
            case JZendParserConstants.IDENTICAL: { ins = new BinaryOperatorIns(Operator.BINARY_IDENTICAL);break; }
            case JZendParserConstants.NIDENTICAL: { ins = new BinaryOperatorIns(Operator.BINARY_NOT_IDENTICAL);break; }
            case JZendParserConstants.MT: { ins = new BinaryOperatorIns(Operator.BINARY_MORE_THAN);break; }
            case JZendParserConstants.MTOE: { ins = new BinaryOperatorIns(Operator.BINARY_MORE_THAN_OR_EQUAL);break; }
            case JZendParserConstants.LT: { ins = new BinaryOperatorIns(Operator.BINARY_LESS_THAN);break; }
            case JZendParserConstants.LTOE: { ins = new BinaryOperatorIns(Operator.BINARY_LESS_THAN_OR_EQUAL);break; }
            case JZendParserConstants.LSHIFT: { ins = new BinaryOperatorIns(Operator.BINARY_LEFT_SHIFT);break; }
            case JZendParserConstants.RSHIFT: { ins = new BinaryOperatorIns(Operator.BINARY_RIGHT_SHIFT);break; }
            case JZendParserConstants.PLUS: { ins = new BinaryOperatorIns(Operator.BINARY_PLUS);break; }
            case JZendParserConstants.MINUS: { ins = new BinaryOperatorIns(Operator.BINARY_MINUS);break; }
            case JZendParserConstants.CONCAT: {
                addIns(new ConcatIns(2));
                return;
            }
            case JZendParserConstants.TIMES: { ins = new BinaryOperatorIns(Operator.BINARY_TIMES);break; }
            case JZendParserConstants.DIVIDE: { ins = new BinaryOperatorIns(Operator.BINARY_DIVIDE);break; }
            case JZendParserConstants.MOD: { ins = new BinaryOperatorIns(Operator.BINARY_MORE_THAN);break; }
            default:throw new AssertionError("Unknown binary operator:" + token_type);
        }
        addIns(ins);
    }

    protected void DoUnaryOptr(int token_type) {
        int op = 0;
        Instruction ins;
        switch (token_type){
            case JZendParserConstants.INC:ins = new UnaryIns(Operator.UNARY_PRE_INC);break;
            case JZendParserConstants.DEC:ins = new UnaryIns(Operator.UNARY_PRE_DEC);break;
            case JZendParserConstants.BITNOT:ins = new UnaryIns(Operator.UNARY_BIT_NOT);break;
            case JZendParserConstants.NOT:ins = new UnaryIns(Operator.UNARY_NOT);break;
            case JZendParserConstants.CLONE:ins = new CloneIns();break;
            case JZendParserConstants.MINUS:ins = new UnaryIns(Operator.UNARY_NEG);break;
            case JZendParserConstants.PLUS:ins = new UnaryIns(Operator.UNARY_POS);break;
            default: throw new AssertionError("Unknown unary operator:" + token_type);
        }
        addIns(ins);
    }

    protected void DoPostIncOrDec(boolean is_dec) {
        addIns(new UnaryIns(is_dec ? Operator.UNARY_POST_DEC : Operator.UNARY_POST_INC));
    }

    protected void DoDup() {
        addIns(new DupIns());
    }


    protected void DoNull() { addIns(new NullIns()); }


    protected void DoNewArray() { addIns(new NewArrayIns()); }


    protected void DoAddArrayItem(boolean is_map) {
        if (!is_map)
            addIns(new AddArrayItemIns());
        else
            addIns(new AddArrayMapItemIns());
    }


    protected void DoDereference() { addIns(new DereferenceIns()); }


    protected void DoPop() { addIns(new PopIns()); }


    protected void DoNombre(double n,boolean is_int) {
        if(is_int)
            addIns(new IntegerIns((int) n));
        else
            addIns(new NumberIns(n));
    }


    protected void DoString(String s) { addIns(new StringIns(s)); }


    protected void DoToString() {
        Instruction ins = getLastIns();
        if (!(ins instanceof StringIns || ins instanceof ToStringIns))
            addIns(new ToStringIns());
    }

    protected void doCast(int what){
        switch(what){
            case 0:
                addIns(new UnaryIns(Operator.UNARY_INT_CAST));
                break;
            case 1:
                addIns(new UnaryIns(Operator.UNARY_FLOAT_CAST));
                break;
            case 2:
                addIns(new UnaryIns(Operator.UNARY_STRING_CAST));
                break;
            case 3:
                addIns(new UnaryIns(Operator.UNARY_ARRAY_CAST));
                break;
            case 4:
                addIns(new UnaryIns(Operator.UNARY_OBJECT_CAST));
                break;
            case 5:
                addIns(new UnaryIns(Operator.UNARY_BOOL_CAST));
                break;
            case 6:
                addIns(new UnaryIns(Operator.UNARY_UNSET_CAST));
                break;
            default:throw new AssertionError("unknown cast type:" + what);
        }
    }

    protected void doBeginSilent(){
        addIns(new SilenceIns(true));
    }

    protected void doEndSilent(){
        addIns(new SilenceIns(true));
    }

    protected void doInclude(boolean once,boolean req){
        addIns(new IncludeIns(once,req));
    }

    protected void doEval(){
        addIns(new EvalIns());
    }

    protected void doIsSet() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new IsSetIns());
    }

    protected void doAdditionalIsSet() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new IsSetIns());
        addIns(new BinaryOperatorIns(Operator.BINARY_AND));
    }

    protected void doIsEmpty() throws CompilationException{
        convertRvalueToLvalue();
        addIns(new EmptyIns());
    }

    protected void doFetchStatic(){
        addIns(new FetchClassIns(FetchClassIns.STATIC));
    }

    protected void doFindClassVar(String vname,boolean isRef){
        addIns(new FindClassVarIns(vname,isRef));
    }

    protected void doFindClassConst(String name){
        addIns(new FindClassConstIns(name));
    }

    protected void doFindClassFunction(String name){
        addIns(new FindClassFunctionIns(name));
    }

    protected void doFetchClass(String name){
        if(name.equals("static")){
            addIns(new FetchClassIns(FetchClassIns.STATIC));
        }
        else if(name.equals("parent")){
            addIns(new FetchClassIns(FetchClassIns.PARENT));
        }
        else if(name.equals("self")){
            addIns(new FetchClassIns(FetchClassIns.SELF));
        }
        else{
            addIns(new FindClassIns(name));
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
        Zval result = const_eval_stack.pop();
        if(result instanceof Prefixable){
            result = ((Prefixable) result).pos();
            const_eval_stack.push(result);
        }
        else {
            throw generateException("'+' is not supported");
        }
    }

    protected void doStaticConstNegOptr() throws CompilationException{
        Zval result = const_eval_stack.pop();
        if(result instanceof Prefixable){
            result = ((Prefixable) result).neg();
            const_eval_stack.push(result);
        }
        else {
            throw generateException("'+' is not supported");
        }
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
            addWarning("illegal offset type ,this element will be ignored");
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

        function_name_stack.push(name);

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

    protected void DoConcat(int count) { addIns(new ConcatIns(count)); }


    protected void doRequestMember(String name,boolean is_function) {
        addIns(new RequestMemberIns(name,false,is_function));
    }

    protected void doRequestMemberByName(boolean isFunc){
        addIns(new RequestMemberByNameIns(false,isFunc));
    }


    protected void DoSubscript(boolean is_max) {
        addIns(new SubscriptIns(false,is_max));
    }

    protected void DoFindVariable(String vname,boolean is_ref) {
        addIns(new FindVarIns(vname,is_ref));
    }


    protected void DoFindVariableByName(boolean is_ref) {
        Instruction ins = getLastIns();
        if (!(ins instanceof StringIns || ins instanceof ToStringIns))
            addIns(new ToStringIns());
        addIns(new FindVarByNameIns(is_ref));
    }


    protected void CheckWritable() throws CompilationException {
        if(!(getLastIns() instanceof ReferencableIns)){
            throw generateException("invalid left value");
        }
    }


    protected void DoFunctionCall() { addIns(new FunctionCallIns()); }


    protected void doFindConst(String n,boolean byname) {
        if(n.equals("NULL")){
            addIns(new NullIns());
        }
        else {
            if(byname){
                addIns(new FindConstByNameIns());
            }
            else
                addIns(new FindConstIns(n));
        }
    }


    protected void DoFindFunction(String name) { addIns(new FindFunctionIns(name)); }


    protected void DoExit() { addIns(new ExitIns()); }


    protected void DoPrint() {
        Instruction ins = getLastIns();
        if (!(ins instanceof StringIns || ins instanceof ToStringIns))
            addIns(new ToStringIns());
        addIns(new PrintIns());
    }


    protected void DoEcho() {
        Instruction ins = getLastIns();
        if (!(ins instanceof StringIns || ins instanceof ToStringIns))
            addIns(new ToStringIns());
        addIns(new EchoIns());
    }


    protected void convertRvalueToLvalue() throws CompilationException {
        CheckWritable();
        Instruction ins = getLastIns();
        assert ins instanceof ReferencableIns;
        ((ReferencableIns) ins).convertToLvalue();
    }

    protected void doArgItem(){
        Instruction ins = getLastIns();
        if(ins instanceof ReferencableIns){
            ((ReferencableIns) ins).convertToLvalue();
        }
    }


    protected void DoBeginIfStatement() {
        IfContext con = new IfContext();
        ConditionalGotoIns ins = new ConditionalGotoIns(true);
        addIns(ins);
        con.last_ins = ins;
        ifstmt_stack.push(con);
    }


    protected void DoEndIfBlock() {
        GotoIns ins = new GotoIns();
        addIns(ins);
        ifstmt_stack.peek().last_ins.line = getLine();
        ifstmt_stack.peek().last_ins = null;
        ifstmt_stack.peek().pending_exit_ins.add(ins);
    }


    protected void DoElseIfBlock() {
        ConditionalGotoIns ins = new ConditionalGotoIns(true);
        addIns(ins);
        ifstmt_stack.peek().last_ins = ins;
    }


    protected void DoEndIfStatement() {
        IfContext con = ifstmt_stack.pop();
        if (con.last_ins != null) /*if not true,there isn't an else block.*/
            con.last_ins.line = getLine();
        con.finish(getLine());
    }


    protected void DoConditionalExpr(int where) {
        switch (where){
            case 0:/*begin of first expression*/
                ConditionalGotoIns ins1 = new ConditionalGotoIns(true);
                ConditionalExprContext ctx = new ConditionalExprContext();
                ctx.mainIns = ins1;
                conditional_expr_stack.push(ctx);
                addIns(ins1);
                break;
            case 1:/*begin of second expression*/
                ctx = conditional_expr_stack.peek();
                ctx.skipIns = new GotoIns();
                addIns(ctx.skipIns);
                ctx.mainIns.line = getLine();
                break;
            case 2:/*end of expression*/
                conditional_expr_stack.pop().skipIns.line = getLine();
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
                ConditionalGotoIns ins2 = new ConditionalGotoIns(true);
                addIns(ins2);
                while_loop_escape_stack.push(ins2);
                break;
            case 2://end if the while statement
                //DoubleIntIns entry_ins = loop_entry_stack.pop();
                GotoIns ins3 = new GotoIns(ctx.getLoopStartLine());
                addIns(ins3);
                while_loop_escape_stack.pop().line = getLine();
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
                addIns(new ConditionalGotoIns(do_while_loop_entry_stack.pop(),false));
                ctx.leaveLoop(getLine());
                break;
            default:throw new AssertionError("Unknown location in do-while:" + where);
        }
    }

    protected void DoForStatement(int where) {
        switch(where){
            case 0://end of the first expression,begin of second expression
                addIns(new PopIns());
                ctx.enterLoop(getLine());
                for_loop_stack.push(new ForContext());
                break;
            case 1://end of second expression,begin of third expression
                OptionalGotoIns ins2 = new OptionalGotoIns();
                addIns(ins2);
                for_loop_stack.peek().cond_ins = ins2;
                for_loop_stack.peek().loop_line = getLine();
                break;
            case 2://end of third expression,begin of code block
                GotoIns ins_goto = new GotoIns(ctx.getLoopStartLine());
                addIns(ins_goto);
                for_loop_stack.peek().cond_ins.line1 = getLine();
                break;
            case 3://end of code block
                addIns(new GotoIns(for_loop_stack.peek().loop_line));
                for_loop_stack.peek().cond_ins.line2 = getLine();
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
                fctx.iterator_ins_line = getLine();
                addIns(new ArrayIteratorIns(false));
                addIns(new StoreIns(fctx.getTempRegister()));

                ctx.enterLoop(getLine());
                addIns(new TloadIns(fctx.getTempRegister()));
                addIns(new IteratorOptrIns(IteratorOptrIns.OPTR_END));
                fctx.escape_ins = new ConditionalGotoIns(true);
                addIns(fctx.escape_ins);
                addIns(new TloadIns(fctx.getTempRegister()));
                addIns(new IteratorOptrIns(IteratorOptrIns.OPTR_NEXT));
                addIns(new PopIns());

                break;
            case 1://end of the whole statement
                fctx = foreach_context_stack.peek();
                addIns(new GotoIns(ctx.getLoopStartLine()));
                fctx.escape_ins.line = getLine();
                foreach_context_stack.pop().finish();
                break;
            default:throw new AssertionError("unknown position in foreach");
        }
    }

    protected void doForEachFirstExpr(boolean is_ref) throws CompilationException {
        convertRvalueToLvalue();
        ForEachContext fctx = foreach_context_stack.peek();
        if(is_ref){
            Instruction itIns = getIns(fctx.iterator_ins_line);
            assert itIns instanceof ArrayIteratorIns;
            ((ArrayIteratorIns) itIns).isRef = true;
        }
        addIns(new TloadIns(fctx.getTempRegister()));
        fctx.first_expr_ins_line = getLine();
        addIns(new ArrayIteratorGet());
        addIns(new BinaryOperatorIns(Operator.BINARY_ASSIGN));
    }

    protected void doForEachSecondExpr(boolean is_ref) throws CompilationException {
        convertRvalueToLvalue();
        ForEachContext fctx = foreach_context_stack.peek();
        Instruction itIns = getIns(fctx.iterator_ins_line);
        assert itIns instanceof ArrayIteratorIns;
        if(((ArrayIteratorIns) itIns).isRef){
            throw generateException("Key element cannot be a reference");
        }
        setIns(fctx.iterator_ins_line,new MapIteratorIns(is_ref));
        setIns(fctx.first_expr_ins_line,new MapIteratorGetIns(MapIteratorGetIns.KEY));
        addIns(new TloadIns(fctx.getTempRegister()));
        addIns(new MapIteratorGetIns(MapIteratorGetIns.VALUE));
        addIns(new BinaryOperatorIns(Operator.BINARY_ASSIGN));
    }

    protected void DoSwitchStatement(int where) {
        switch(where){
            case 0://begin of switch statement
                SwitchContext sctx = new SwitchContext();
                switchstmt_stack.push(sctx);
                ctx.enterSwitch();
                break;
            case 1://end of expression,begin of code block
                addIns(new StoreIns(switchstmt_stack.peek().treg_index));
                GotoIns ins1 = new GotoIns(Opcode.GOTO);
                addIns(ins1);
                switchstmt_stack.peek().last_ins = ins1;
                break;
            case 2://end of the statement
                SwitchContext sctx2 = switchstmt_stack.peek();
                if(sctx2.first_default_line != -1){
                    GotoIns skip_ins = new GotoIns(Opcode.GOTO);
                    addIns(skip_ins);
                    sctx2.last_ins.setLine(getLine());
                    addIns(new GotoIns(sctx2.first_default_line));
                    skip_ins.line = getLine();
                }
                else{
                    sctx2.last_ins.setLine(getLine());
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
                GotoIns ins = new GotoIns(Opcode.GOTO);
                addIns(ins);
                sctx.last_ins.setLine(getLine());
                addIns(new TloadIns(sctx.treg_index));
                sctx.case_skip_ins = ins;
                break;
            case 1://case label end
                addIns(new BinaryOperatorIns(Operator.BINARY_EQUAL));
                ConditionalGotoIns ins2 = new ConditionalGotoIns(true);
                addIns(ins2);
                sctx.last_ins = ins2;
                sctx.case_skip_ins.line = getLine();
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
            addIns(new IntegerIns(1));
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
                addIns(new ReturnOrThrowIns(false));
                break;
            case 1://throw
                addIns(new ReturnOrThrowIns(true));
                break;
            default:throw new AssertionError("not return nor throw:" + which);
        }
    }

    protected void DoUnset() throws CompilationException {
        convertRvalueToLvalue();
        //TODO:unset in compile
        addIns(new UnSetIns());
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
        addIns(new GlobalIns(name));
    }

    private void finishCompiling() {
        addIns(new IntegerIns(0));
        addIns(new ExitIns());
        top_routine.loopTable = ctx.getLoopTable();
    }

    protected void doBeginFunctionDeclaration(String fname, boolean is_ref) throws CompilationException {
        ZendFunction f;
        if((f = current_routine.getFunction(fname)) != null){
            throw generateException("Cannot redeclare " + fname + "() (previously declared in " + f.getHead().filename + ")");
        }
        function_name_stack.push(fname);
        currentHead = new FunctionHead(fname,is_ref);
        currentHead.filename = this.fname;
    }

    protected void doBeginFunctionBody(boolean isAnonymous){
        ZendFunction func = new ZendFunction(currentHead,current_routine.newSubRoutine());
        if(!isAnonymous){
            current_routine.addFunction(func);
        }
        else{
            addIns(new NewFunctionIns(func));
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
        function_name_stack.pop();
        if(!current_routine.hasReturn){
            addIns(new NullIns());
            addIns(new ReturnOrThrowIns(false));
        }

        current_routine.loopTable = ctx.getLoopTable();
        current_routine = current_routine.parent();
        ctx = ctx.getParent();

    }


    protected void doAssign(boolean is_array) {
        if(!is_array){
            addIns(new BinaryOperatorIns(Operator.BINARY_ASSIGN));
        }
        else{
            addIns(new ArrayAssignIns());
        }
    }

    protected void DoReference() throws CompilationException {
        convertRvalueToLvalue();
    }

    protected void DoNew() {
        if(getLastIns() instanceof FunctionCallIns)
            setLastIns(new NewIns());
        else
            addIns(new NewIns());
    }

    protected void DoFindClass(String name) {
        addIns(new FindClassIns(name));
    }

    protected void DoInstanceOf() {
        addIns(new BinaryOperatorIns(Operator.BINARY_INSTANCEOF));
    }

    protected void DoPackArg(int count) {
        addIns(new PackArgIns(count));
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
        public List<GotoIns> pending_exit_ins;
        public ConditionalGotoIns last_ins;
        public IfContext(){
            pending_exit_ins = new ArrayList<>();
        }
        public void finish(int line){
            for(GotoIns ins : pending_exit_ins){
                ins.line = line;
            }
        }
    }
    class ConditionalExprContext{
        ConditionalGotoIns mainIns;
        GotoIns skipIns;
    }
    class SwitchContext{
        public SingleJumpIns last_ins;
        public int first_default_line = -1;
        public int treg_index;
        public GotoIns case_skip_ins;

        public SwitchContext(){
            treg_index = regmgr.requestTempReg();
        }
        public void finish(){
            regmgr.freeTempReg(treg_index);
        }
    }

    class ForContext{
        OptionalGotoIns cond_ins;
        int loop_line;
    }
    class ForEachContext{
        private int treg_index;
        ConditionalGotoIns escape_ins;
        int iterator_ins_line;
        int first_expr_ins_line;
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
