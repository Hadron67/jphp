package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.JZendVM;
import com.hadroncfy.jphp.jzend.Context;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Function;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-8-9.
 *
 */
public class ZendFunction implements Function {
    protected FunctionHead head;

    protected Routine body = null;


    protected String fname = "-";

    public ZendFunction(FunctionHead head,Routine body){
        this.head = head;
        this.body = body;
    }

    protected ZendFunction(FunctionHead head){
        this.head = head;
    }

    public String getName(){
        return head.fname;
    }

    public FunctionHead getHead(){
        return head;
    }

    protected Routine getBody(){
        return body;
    }

    protected void setBody(Routine body){
        this.body = body;
    }

    public int getStartLine(){
        return body.opcodes.get(0).line;
    }

    protected void dump_self(Dumper dumper) {
        dumper.ps.println(head.toString());
        body.dump_self(dumper);
    }

    @Override
    public Zval call(Context env, Zval[] args) {
        //TODO:call a function
        env.loadConsts(body.globalScope.constTable);
        env.loadFunctions(body.functionTable);
        env.enterScope();
        JZendVM vm = new JZendVM(env,body);
        vm.exec();
        env.leaveScope();
        return vm.getResult();
    }

    @Override
    public boolean doTypeCheck(String typename) {
        return typename.equals("function") || typename.equals("callable");
    }

    @Override
    public String dump() {
        return head.toString();
    }

    @Override
    public String getTypeName() {
        return "function";
    }

    @Override
    public Zval clone() {
        return this;
    }
}
