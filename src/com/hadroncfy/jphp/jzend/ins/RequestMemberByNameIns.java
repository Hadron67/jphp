package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.Zstring;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Castable;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-2.
 */

@ExplicitTypeInstruction
public class RequestMemberByNameIns implements ReferencableIns {

    public boolean isRef;
    public boolean isFunc;

    public RequestMemberByNameIns(boolean isRef, boolean isFunc){
        this.isFunc = isFunc;
        this.isRef = isRef;
    }
    @Override
    public void exec(VM vm) {
        Zval val = Tool.fullyDeRef(vm.pop());
        String name;
        if(val instanceof Zstring){
            name = ((Zstring) val).value;
        }
        else if(val instanceof Castable){
            name = ((Castable) val).stringCast().value;
        }
        else{
            vm.makeError("Cannot convert " + val.getTypeName() + "to string");
        }
        //TODO:request member by name
        assert false;

    }

    @Override
    public String toString() {
        return (isFunc ? "REQUEST_FUNC_BY_NAME" : "REQUEST_MEMBER_BY_NAME") + (isRef ? "&" : " ");
    }

    @Override
    public void convertToLvalue() {
        isRef = true;
    }

    @Override
    public void convertToRvalue() {
        isRef = false;
    }
}
