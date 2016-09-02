package com.hadroncfy.jphp.jzend;

import com.hadroncfy.tools.BiConsumer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cfy on 16-8-13.
 */
public class ReferenceMap<K,V> {

    private Map<K,Item> map = new HashMap<>();

    public ReferenceMap(){}

    public void put(K key,V value){
        map.put(key,new Item(value));
    }

    public V get(K key){
        return map.get(key).item;
    }

    public Item getRef(K key){
        return map.get(key);
    }

    public int size(){
        return map.size();
    }

    public void forEachRef(BiConsumer<K,Item> consumer){
        for(Map.Entry<K,Item> entry : map.entrySet()){
            consumer.accept(entry.getKey(),entry.getValue());
        }
    }

    public void forEach(BiConsumer<K,V> consumer){
        for(Map.Entry<K,Item> entry : map.entrySet()){
            consumer.accept(entry.getKey(),entry.getValue().item);
        }
    }

    public class Item{
        private V item;

        private Item(V t){
            item = t;
        }

        public V assign(V ni){
            return item = ni;
        }

        public V get(){
            return item;
        }
    }
}
