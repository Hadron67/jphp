package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.Zval;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-8-9.
 */
public class ZendFunction {
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

}
