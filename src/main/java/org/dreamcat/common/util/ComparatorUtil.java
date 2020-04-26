package org.dreamcat.common.util;

/**
 * Create by tuke on 2020/4/19
 */
public final class ComparatorUtil {

    /**
     * use Comparable and hashCode to compare two objects
     *
     * @param a   the left object to compare
     * @param b   the right object to compare
     * @param <A> type of a
     * @param <B> type of b
     * @return positive if more than, negative if less than, 0 if equal
     * @see Comparable#compareTo(Object)
     * @see java.util.Comparator
     */
    @SuppressWarnings({"unchecked"})
    public static <A, B> int compare(A a, B b) {
        if (a == null && b == null) return 0;
        // null always less than not-null
        if (a == null) return -1;
        if (b == null) return 1;

        if (a instanceof Comparable &&
                a.getClass().isInstance(b)) {
            return ((Comparable<A>) a).compareTo((A) b);
        } else if (b instanceof Comparable &&
                b.getClass().isInstance(a)) {
            return -((Comparable<B>) b).compareTo((B) a);
        }

        int offset = a.hashCode() - b.hashCode();
        if (offset != 0) return offset;

        return a.getClass().hashCode() - b.getClass().hashCode();
    }
}
