package com.hadroncfy.jphp.jzend.compile;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfy on 16-8-10.
 */
public class LoopTable {
    private List<int[]> break_list;
    private List<int[]> continuer_list;

    public LoopTable(){
        break_list = new ArrayList<>();
        continuer_list = new ArrayList<>();
    }

    public int getBreakTableSize(){
        return break_list.size();
    }

    public int getContineTableSize(){
        return continuer_list.size();
    }

    protected int[] newBreakItem(int size){
        int[] ret = new int[size];
        break_list.add(ret);
        return ret;
    }

    protected int[] newContinueItem(int size){
        int[] ret = new int[size];
        continuer_list.add(ret);
        return ret;
    }

    public boolean isEmpty(){
        return break_list.isEmpty() && continuer_list.isEmpty();
    }

    public void dump(PrintStream ps){
        ps.println("break:");
        int index = 0;
        if(break_list.isEmpty()){
            ps.println("(no break statement)");
        }
        else {
            for (int[] l1 : break_list) {
                ps.print("#");
                ps.print(index++);
                for (int i : l1) {
                    ps.print(" ");
                    ps.print(i);
                }
                ps.println();
            }
        }

        ps.println("continue:");
        if(continuer_list.isEmpty()){
            ps.println("(no continue statement)");
        }
        else {
            index = 0;
            for (int[] l1 : continuer_list) {
                ps.print("#");
                ps.print(index++);
                for (int i : l1) {
                    ps.print(" ");
                    ps.print(i);
                }
                ps.println();
            }
        }
    }
}
