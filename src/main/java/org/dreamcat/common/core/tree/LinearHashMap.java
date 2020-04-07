package org.dreamcat.common.core.tree;

/**
 * Create by tuke on 2020/4/4
 */
public class LinearHashMap {


    /**
     * @see java.util.HashMap#put(Object, Object) 
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
