package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-5-12.
 */
public class ZNode {
    protected int type;
    public static int IS_CONST = 0;
    public static int IS_VAR = 1;

    protected Object data;

    protected ZNode[] children = null;

    public ZNode(int type){
        this(type,null,null);
    }
    public ZNode(int type,Object data){
        this(type,data,null);
    }
    public ZNode(int type,Object data,ZNode[] c){
        this.data = data;
        this.type = type;
        this.children = c;
    }
}
