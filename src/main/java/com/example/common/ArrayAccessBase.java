package com.example.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *  屏蔽了子类不需要的方法，限制于JDK1.8
 *          TODO:可考虑其他的实现方法
 *
 * Created by baixiangzhu on 2017/7/28.
 */
public interface ArrayAccessBase<K,V> extends Map<K,V> {

    default int size()                      {return 0;}
    default boolean isEmpty()               {return false;}
    //default boolean containsKey(Object key) {return false;}
    default boolean containsValue(Object v) {return false;}
    // get
    default V put(K key, V value)           {return null;}
    default V remove (Object key)           {return null;}
    default void putAll(Map<? extends K, ? extends V> map)      {}
    default void clear  ()                                      {}
    default Set keySet ()                                       {return null;}
    default Collection values()                                 {return null;}
    default Set entrySet()                                      {return null;}

}
