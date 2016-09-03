package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.jphp.jzend.ins.Instruction;
import com.hadroncfy.jphp.jzend.ins.BreakOrContinueIns;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cfy on 16-8-10.
 */
class CompilerContext {
    private CompilerContext parent = null;

    private LoopTable loopTable = new LoopTable();
    private Stack<Integer> continue_entry_stack = new Stack<>();
    private Stack<TryCatchItem> tryCatchItemStack = new Stack<>();
    private List<BreakItem> breakItems = new LinkedList<>();

    private int breakLevel = 0;

    public static CompilerContext newGlobalContext(){
        return new CompilerContext();
    }


    private CompilerContext(){
    }

    protected CompilerContext getParent(){
        return parent;
    }

    protected CompilerContext newSubContext(){
        CompilerContext ctx = new CompilerContext();
        ctx.parent = this;
        return ctx;
    }

    protected void enterSwitch(){
        breakLevel++;
        for(BreakItem item : breakItems){
            item.loop_ptr--;
        }
    }

    protected void leaveSwitch(int line){
        breakLevel--;
        for(BreakItem item : breakItems){
            if(item.loop_ptr == item.write_ptr){
                item.table[item.write_ptr++] = line;
            }
            item.loop_ptr++;
        }
    }

    protected void enterLoop(int line){
        continue_entry_stack.push(line);
        breakLevel++;
        for(BreakItem item : breakItems){
            item.loop_ptr--;
        }
    }

    protected void leaveLoop(int line){
        continue_entry_stack.pop();
        breakLevel--;
        for(BreakItem item : breakItems){
            if(item.loop_ptr == item.write_ptr){
                item.table[item.write_ptr++] = line;
            }
            item.loop_ptr++;
        }
    }

    protected Instruction newBreakInstruction(){
        int index = loopTable.getBreakTableSize();
        int[] blist = loopTable.newBreakItem(breakLevel);
        BreakItem item = new BreakItem(blist);
        breakItems.add(item);

        return new BreakOrContinueIns(index,true);
    }

    protected Instruction newContinueInstruction(){
        int index = loopTable.getContineTableSize();
        int size = continue_entry_stack.size();
        int[] clist = loopTable.newContinueItem(size);
        for(int i = 0;i < size;i++){
            clist[i] = continue_entry_stack.get(size - i - 1);
        }
        return new BreakOrContinueIns(index,false);
    }

    protected LoopTable getLoopTable(){
        if(!continue_entry_stack.isEmpty()){
            throw new IllegalStateException("cannot return the loop table when still in a loop.");
        }
        for(BreakItem item : breakItems){
            if(item.write_ptr != item.table.length){
                throw new AssertionError("incomplete break table,which shouldn't happen.");
            }
        }
        return loopTable;
    }

    protected boolean isBreakAvailable(){
        return breakLevel > 0;
    }
    protected boolean isContinueAvailable(){
        return !continue_entry_stack.isEmpty();
    }
    protected int getLoopStartLine(){
        return continue_entry_stack.peek();
    }

    protected void enterTryBlock(int line){
        TryCatchItem item = new TryCatchItem();
        item.from = line;
        tryCatchItemStack.push(item);
    }

    protected void leaveTryBlock(int line){
        tryCatchItemStack.peek().to = line;
    }

    protected void endTryCatch(){
        tryCatchItemStack.pop();
    }

    protected int getTryStart(){
        return tryCatchItemStack.peek().from;
    }

    protected int getTryEnd(){
        return tryCatchItemStack.peek().to;
    }


    class BreakItem {
        int[] table;
        int write_ptr = 0;
        int loop_ptr = 0;

        public BreakItem(int[] t){
            table = t;
        }
    }

    class TryCatchItem{
        int from;
        int to;
    }

}
