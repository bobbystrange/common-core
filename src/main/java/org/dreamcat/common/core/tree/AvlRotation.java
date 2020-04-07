package org.dreamcat.common.core.tree;

/**
 * Create by tuke on 2020/4/5
 */
public final class AvlRotation {

    public static <E> AvlNode<E> balance(AvlNode<E> node) {
        int diff = getHeight(node.left) - getHeight(node.right);
        if (diff == 2) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                node = rotateLL(node);
            } else {
                node = doubleRotateLR(node);
            }
        } else if (diff == -2) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                node = rotateRR(node);
            } else {
                node = doubleRotateRL(node);
            }
        }

        node.height = updateHeight(node);
        return node;
    }

    /*
    Left-Left Rotation, when Dx > y
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                k2
        k1				z
    Dx		y
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                k1
        Dx				k2
                    y		z
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    public static <E> AvlNode<E> rotateLL(AvlNode<E> k2) {
        AvlNode<E> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = updateHeight(k2);
        k1.height = updateHeight(k1);
        return k1;
    }

    /*
    Right-Right Rotation, when y < Dz
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                k1
        x				k2
                    y		Dz
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                k2
        k1				Dz
    x		y
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    public static <E> AvlNode<E> rotateRR(AvlNode<E> k1) {
        AvlNode<E> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k1.height = updateHeight(k1);
        k2.height = updateHeight(k2);
        return k2;
    }

    /*
    Left-Right Rotation
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k3
             k1					  d
    a				k2
                  b   c
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k3
             k2					  d
       k1		   c
    a     b
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k2
             k1					  k3
          a		 b			   c	  d
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    public static <E> AvlNode<E> doubleRotateLR(AvlNode<E> k3) {
        // first rotateRR k1
        k3.left = rotateRR(k3.left);
        // then rotateLL k3
        return rotateLL(k3);
    }

    /*
    Right-Left Rotation
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k1
             a					  k3
                            k2          d
                          b    c
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k1
             a					  k2
                              b    		k3
                                      c    d
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
                        k2
             k1					  k3
          a		 b			   c	  d
    ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----
     */
    public static <E> AvlNode<E> doubleRotateRL(AvlNode<E> k1) {
        // first rotateRR k1
        k1.right = rotateLL(k1.right);
        // then rotateLL k1
        return rotateRR(k1);
    }

    /// static utils

    public static <E> int updateHeight(AvlNode<E> node) {
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    public static <E> int getHeight(AvlNode<E> node) {
        return node != null ? node.height : 0;
    }
}
