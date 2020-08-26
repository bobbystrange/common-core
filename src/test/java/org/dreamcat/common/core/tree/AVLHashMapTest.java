package org.dreamcat.common.core.tree;

import org.junit.Test;

import java.util.HashMap;


/**
 * Create by tuke on 2020/4/19
 */
public class AVLHashMapTest {

    @Test
    public void testHashMapPut() {
        HashMap<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 1000_000; i++) {
            map.put(i, i * i + "");
        }
        for (int i = 0; i < 32; i++) {
            System.out.printf("%d => %s\n", i, map.get(i));
        }
    }

    @Test
    public void testPut() {
        AVLHashMap<Integer, String> map = new AVLHashMap<>();
        for (int i = 0; i < 32; i++) {
            map.put(i, i * i + "");
        }
        for (int i = 0; i < 32; i++) {
            System.out.printf("%d => %s\n", i, map.get(i));
        }
    }

    @Test
    public void testRemove() {
        AVLHashMap<Integer, String> map = new AVLHashMap<>();
        for (int i = 0; i < 32; i++) {
            map.put(i, i * i + "");
        }

        for (int i = 16; i < 32; i++) {
            map.remove(i);
        }

        for (int i = 0; i < 32; i++) {
            System.out.printf("%d => %s\n", i, map.get(i));
        }
    }

}
