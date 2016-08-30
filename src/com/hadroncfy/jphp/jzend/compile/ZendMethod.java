package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-21.
 */
public class ZendMethod  {

    protected ZendClass clazz = null;

    protected ZendFunction body = null;

    protected MethodHead head;

    private boolean shouldHaveBody = true;

    protected ZendMethod(MethodHead head,boolean isAbstract){
        this.head = head;
        shouldHaveBody = isAbstract;
        if(!isAbstract){
            body = new ZendFunction(head);
        }
    }

    public String getFullName() {
        return head.getFullName();
    }

    public String getName(){
        return head.fname;
    }

    public boolean isAbstract(){
        return shouldHaveBody;
    }

    protected ZendFunction getBody(){
        return body;
    }

    protected void dump_self(Dumper dumper){
        if(isAbstract()){
            dumper.ps.println(head.toString() + ";");
        }
        else{
            dumper.ps.println(head.toString() + " -> " + dumper.addFunction(body));
        }
    }


}
