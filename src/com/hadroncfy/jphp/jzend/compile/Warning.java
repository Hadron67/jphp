package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-13.
 */
public class Warning {
    public static final int NOTICE = 0;
    public static final int WARN = 1;

    protected int level;
    protected int line;
    protected int column;
    protected String msg;
    protected String filename;

    public Warning(int level,String msg,int line,int column,String filename){
        this.msg = msg;
        this.line = line;
        this.column = column;
        this.level = level;
        this.filename = filename;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("PHP ").append(level == 0 ? "Notice" : "Warning").append(" : ").append(msg).append(" in ").append(filename).append(" on line ").append(line)
                .append(",column ").append(column).toString();
    }
}
