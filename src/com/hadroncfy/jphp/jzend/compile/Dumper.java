package com.hadroncfy.jphp.jzend.compile;

import com.hadroncfy.tools.BiConsumer;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cfy on 16-8-22.
 */
class Dumper {
    public PrintStream ps;
    private int index = 0;

    private Queue<IndexedItem<ZendFunction>> pendingfuncs = new LinkedList<>();
    private Queue<IndexedItem<ZendClass>> pendingclass = new LinkedList<>();

    Dumper(PrintStream p){
        ps = p;
    }

    protected int addFunction(ZendFunction func){
        pendingfuncs.offer(new IndexedItem<ZendFunction>(func));
        return index - 1;
    }

    protected int addClass(ZendClass clazz){
        pendingclass.offer(new IndexedItem<ZendClass>(clazz));
        return index - 1;
    }

    protected boolean hasFunction(){
        return !pendingfuncs.isEmpty();
    }

    protected boolean hasClass(){
        return !pendingclass.isEmpty();
    }

    protected IndexedItem<ZendFunction> consumeOneFunc(){
        return pendingfuncs.poll();
    }

    protected IndexedItem<ZendClass> consumeOneClass(){
        return pendingclass.poll();
    }


    public class IndexedItem<T>{
        int index;
        T item;

        public IndexedItem(T func){
            item = func;
            this.index = Dumper.this.index++;
        }

        public int getIndex(){
            return index;
        }

        public T getItem(){
            return item;
        }
    }
}
