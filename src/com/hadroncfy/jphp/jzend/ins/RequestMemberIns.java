package com.hadroncfy.jphp.jzend.ins;

import com.hadroncfy.jphp.jzend.VM;
import com.hadroncfy.jphp.jzend.types.typeInterfaces.Zval;

/**
 * Created by cfy on 16-9-1.
 */

@ExplicitTypeInstruction
public class RequestMemberIns implements ReferencableIns {

    public String memberName;
    public boolean isRef;
    public boolean isFunc = false;

    public RequestMemberIns(String memberName,boolean isRef,boolean isfunc){
        this.memberName = memberName;
        this.isRef = isRef;
        isFunc = isfunc;
    }
    @Override
    public void exec(VM vm) {
        Zval obj = Tool.fullyDeRef(vm.pop());
        // TODO: 16-9-1  Request member of an object,cannot implement now because object is not implemented yet.
        assert false;
    }

    @Override
    public String toString() {
        return "REQUEST_MEMBER" + (isFunc ? "_FUNC " : " ") + (isRef ? "&" : "") + memberName;
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
