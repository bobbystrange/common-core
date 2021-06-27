package org.dreamcat.common;

import lombok.EqualsAndHashCode;

/**
 * Create by tuke on 2020/6/24
 */
@EqualsAndHashCode
public class OneOf<L, R> {

    private L left;
    private R right;

    private OneOf(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> OneOf<L, R> wrapLeft(L left) {
        return new OneOf<>(left, null);
    }

    public static <L, R> OneOf<L, R> wrapRight(R right) {
        return new OneOf<>(null, right);
    }

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public void setLeft(L left) {
        this.left = left;
        this.right = null;
    }

    public void setRight(R right) {
        this.left = null;
        this.right = right;
    }
}
