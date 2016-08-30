package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.types.Zval;

/**
 * Created by cfy on 16-8-26.
 */
class MemberBuilder {
    protected Access access = Access.PUBLIC;
    private boolean hasAccess = false;
    private boolean hasFinal = false;
    private boolean hasStatic = false;
    private boolean hasAbstract = false;

    private WarningReporter reporter;

    protected MemberBuilder(WarningReporter reporter){
        this.reporter = reporter;
    }

    protected void reInit(){
        access =Access.PUBLIC;
        hasAbstract = hasAccess  = hasFinal = hasStatic = false;
    }

    protected void addAccess(Access access) throws IllegalModifierException{
        if(hasAccess){
            throw new IllegalModifierException("Multiple access type modifiers are not allowed");
        }
        hasAccess = true;
        this.access = access;
    }

    protected void addFinal() throws IllegalModifierException{
        if(hasFinal){
            throw new IllegalModifierException("Multiple final modifiers are not allowed");
        }
        hasFinal = true;
    }

    protected void addStatic() throws IllegalModifierException{
        if(hasStatic){
            throw new IllegalModifierException("Multiple static modifiers are not allowed");
        }
        hasStatic = true;
    }

    protected void addAbstract() throws IllegalModifierException{
        if(hasAbstract){
            throw new IllegalModifierException("Multiple abstract modifiers are not allowed");
        }
        hasAbstract = true;
    }

    protected ClassMember<Zval> buildVar(Zval value) throws IllegalModifierException{
        if(hasAbstract){
            throw new IllegalModifierException("Properties cannot be declared abstract");
        }
        ClassMember<Zval> m = new ClassMember<>();
        m.member = value;
        m.access = access;
        m.isStatic = hasStatic;
        m.isFinal = hasFinal;
        return m;
    }

   protected ClassMember<ZendMethod> buildMethod(MethodHead head,boolean inInterface) throws IllegalModifierException {
       if(inInterface && (access != Access.PUBLIC || hasFinal || hasAbstract)){
           throw new IllegalModifierException("Access type for interface method " + head.getFullName() + "() must be omitted");
       }
       ZendMethod method = new ZendMethod(head,hasAbstract || inInterface);

       ClassMember<ZendMethod> m = new ClassMember<>();
       m.member = method;
       m.access = access;
       m.isStatic = hasStatic;
       m.isFinal = hasFinal;
       return m;
   }
    protected ClassMember<ZendMethod> buildMethod(MethodHead head) throws IllegalModifierException {
        return buildMethod(head,false);
    }
}
