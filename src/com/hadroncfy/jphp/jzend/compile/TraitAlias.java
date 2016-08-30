package com.hadroncfy.jphp.jzend.compile;

/**
 * Created by cfy on 16-8-30.
 */
public class TraitAlias  {
    protected String traitName;
    protected String methodName;

    private Access aliasAccess = Access.PUBLIC;
    private String aliasMethodName;
    private boolean accessChanged = false;
    private boolean nameChanged = false;

    public TraitAlias(String traitName,String methodName){
        this.traitName = traitName;
        this.methodName = methodName;
    }
    public TraitAlias(String methodName){
        this(null,methodName);
    }
    public void setAliasAccess(Access access){
        aliasAccess = access;
        accessChanged = true;
    }

    public void setAliasName(String name){
        aliasMethodName = name;
        nameChanged = true;
    }

    @Override
    public String toString() {
        return (traitName != null ? traitName + "::" : "") + methodName + " as " + (accessChanged ? aliasAccess.toString() + " " : "") + (nameChanged ? aliasMethodName : "");
    }
}
