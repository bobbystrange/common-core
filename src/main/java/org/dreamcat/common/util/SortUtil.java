package org.dreamcat.common.util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

/**
 * Create by tuke on 2020/4/4
 */
@SuppressWarnings("unchecked")
public final class SortUtil {

    private SortUtil() {
    }

    // bubble sort, swap the max value to the tail
    public static void bubbleSort(int[] a) {
        int size = a.length;
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static void bubbleSort(int[] a, int offset, int size) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static void bubbleSort(long[] a) {
        int size = a.length;
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static void bubbleSort(long[] a, int offset, int size) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static void bubbleSort(double[] a) {
        int size = a.length;
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static void bubbleSort(double[] a, int offset, int size) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T extends Comparable<T>> void bubbleSort(T[] a) {
        int size = a.length;
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T extends Comparable<T>> void bubbleSort(T[] a, int offset, int size) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T> void bubbleSort(T[] a, int offset, int size, Comparator<T> c) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (c.compare(a[j], a[j + 1]) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T extends Comparable<T>> void bubbleSort(List<T> a) {
        int size = a.size();
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T extends Comparable<T>> void bubbleSort(List<T> a, int offset, int size) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    public static <T> void bubbleSort(List<T> a, int offset, int size, Comparator<T> c) {
        for (int i = offset; i < offset + size - 1; i++) {
            boolean swapped = false;
            for (int j = offset; j < offset + size - 1 - i; j++) {
                if (c.compare(a.get(j), a.get(j + 1)) > 0) {
                    SwapUtil.swap(a, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // select sort, select the max value and insert to the tail
    public static void selectSort(int[] a) {
        int size = a.length;
        for (int i = size - 1; i > 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static void selectSort(int[] a, int offset, int size) {
        for (int i = size + offset - 1; i > 1 + offset; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static void selectSort(long[] a) {
        int size = a.length;
        for (int i = size - 1; i > 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static void selectSort(long[] a, int offset, int size) {
        for (int i = size + offset - 1; i > 1 + offset; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static void selectSort(double[] a) {
        int size = a.length;
        for (int i = size - 1; i > 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static void selectSort(double[] a, int offset, int size) {
        for (int i = size + offset - 1; i > 1 + offset; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (a[j] >= a[k]) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T extends Comparable<T>> void selectSort(T[] a) {
        int size = a.length;
        for (int i = size - 1; i > 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = 1; j <= i; j++) {
                if (a[j].compareTo(a[k]) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T extends Comparable<T>> void selectSort(T[] a, int offset, int size) {
        for (int i = offset + size - 1; i > offset + 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (a[j].compareTo(a[k]) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T> void selectSort(T[] a, int offset, int size, Comparator<T> c) {
        for (int i = offset + size - 1; i > offset + 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (c.compare(a[j], a[k]) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T extends Comparable<T>> void selectSort(List<T> a) {
        int size = a.size();
        for (int i = size - 1; i > 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = 1; j <= i; j++) {
                if (a.get(j).compareTo(a.get(k)) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T extends Comparable<T>> void selectSort(List<T> a, int offset, int size) {
        for (int i = offset + size - 1; i > offset + 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (a.get(j).compareTo(a.get(k)) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    public static <T extends Comparable<T>> void selectSort(List<T> a, int offset, int size,
            Comparator<T> c) {
        for (int i = offset + size - 1; i > offset + 1; i--) {
            // index of max
            int k = 0;
            boolean sorted = true;
            for (int j = offset + 1; j <= i; j++) {
                if (c.compare(a.get(j), a.get(k)) >= 0) {
                    k = j;
                } else {
                    sorted = false;
                }
            }

            if (sorted) return;
            SwapUtil.swap(a, i, k);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // insert sort, insert unsorted elements to the sorted head
    public static void insertSort(int[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0, k-1]
        for (int k = 1; k < size; k++) {
            int t = a[k];
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= 0; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static void insertSort(int[] a, int offset, int size) {
        // sorted slice [offset, k-1], unsorted slice [k, offset+size-1]
        // insert k to [0, k-1]
        for (int k = offset + 1; k < offset + size; k++) {
            int t = a[k];
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= offset; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static void insertSort(long[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0, k-1]
        for (int k = 1; k < size; k++) {
            long t = a[k];
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= 0; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static void insertSort(long[] a, int offset, int size) {
        for (int k = offset + 1; k < offset + size; k++) {
            long t = a[k];
            int j;
            for (j = k - 1; j >= offset; j--) {
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static void insertSort(double[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0, k-1]
        for (int k = 1; k < size; k++) {
            double t = a[k];
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= 0; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static void insertSort(double[] a, int offset, int size) {
        for (int k = offset + 1; k < offset + size; k++) {
            double t = a[k];
            int j;
            for (j = k - 1; j >= offset; j--) {
                if (t >= a[j]) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static <T extends Comparable<T>> void insertSort(T[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0, k-1]
        for (int k = 1; k < size; k++) {
            T t = a[k];
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= 0; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t.compareTo(a[j]) >= 0) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static <T extends Comparable<T>> void insertSort(T[] a, int offset, int size) {
        for (int k = offset + 1; k < offset + size; k++) {
            T t = a[k];
            int j;
            for (j = k - 1; j >= offset; j--) {
                if (t.compareTo(a[j]) >= 0) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static <T> void insertSort(T[] a, int offset, int size, Comparator<T> c) {
        for (int k = offset + 1; k < offset + size; k++) {
            T t = a[k];
            int j;
            for (j = k - 1; j >= offset; j--) {
                if (c.compare(t, a[j]) >= 0) break;
                a[j + 1] = a[j];
            }
            a[j + 1] = t;
        }
    }

    public static <T extends Comparable<T>> void insertSort(List<T> a) {
        int size = a.size();
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0, k-1]
        for (int k = 1; k < size; k++) {
            T t = a.get(k);
            // insert k to j+1
            // 0,1,,...,j-1,j,k,j+1,...,k-1
            int j;
            for (j = k - 1; j >= 0; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t.compareTo(a.get(j)) >= 0) break;
                a.set(j + 1, a.get(j));
            }
            a.set(j + 1, t);
        }
    }

    public static <T extends Comparable<T>> void insertSort(List<T> a, int offset, int size) {
        for (int k = offset + 1; k < offset + size; k++) {
            T t = a.get(k);
            int j;
            for (j = k - 1; j >= offset; j--) {
                // keep 0,1,...,j, only move j+1,...,k
                if (t.compareTo(a.get(j)) >= 0) break;
                a.set(j + 1, a.get(j));
            }
            a.set(j + 1, t);
        }
    }

    public static <T> void insertSort(List<T> a, int offset, int size, Comparator<T> c) {
        for (int k = offset + 1; k < offset + size; k++) {
            T t = a.get(k);
            int j;
            for (j = k - 1; j >= offset; j--) {
                if (c.compare(t, a.get(j)) >= 0) break;
                a.set(j + 1, a.get(j));
            }
            a.set(j + 1, t);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // bin sort, box
    public static <E> void binSort(LinkedList<E> list, ToIntFunction<E> scorer, int bound) {
        LinkedList<E>[] bin = new LinkedList[bound];
        for (int j = bound - 1; j >= 0; j--) {
            bin[j] = new LinkedList<>();
        }

        while (!list.isEmpty()) {
            E e = list.removeFirst();
            bin[scorer.applyAsInt(e)].addFirst(e);
        }

        for (int j = bound - 1; j >= 0; j--) {
            while (!bin[j].isEmpty()) {
                list.addFirst(bin[j].removeFirst());
            }
            // help Gc
            bin[j] = null;
        }
    }

    // radix sort
    public static <E> void radixSort(LinkedList<E> list, BiFunction<E, Integer, Integer> scorer,
            int binBound, int bound) {
        for (int j = 0; j <= bound - 1; j++) {
            int finalJ = j;
            binSort(list, e -> scorer.apply(e, finalJ), binBound);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // split A[p..r] to A[p..q-1] and A[q+1..r]
    // all elements in A[p..q-1] is less than or equals to A[q]
    // and all elements in A[q+1..r] is greater than or equals to A[q]
    public static void quickSort(int[] a) {
        int length = a.length;
        if (length <= 1) return;

        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, low, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    // low, new_low, new_high, high
    // low+1..new_low-1 < temp, new_high+1..high > temp
    // a[low] = a[new_high], a[new_high] = a[new_low]
    // then low..new_low-1 < temp, new_high..high > temp
    private static int getMiddle(int[] a, int low, int high) {
        int temp = a[low];
        while (low < high) {
            while (low < high && a[high] > temp) {
                high--;
            }
            // find first value < a[low]
            a[low] = a[high];
            // find first value > a[high]
            while (low < high && a[low] < temp) {
                low++;
            }
            a[high] = a[low];
        }

        a[low] = temp;
        return low;
    }

    public static void quickSort(long[] a) {
        int length = a.length;
        if (length <= 1) return;

        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(long[] a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, low, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    private static int getMiddle(long[] a, int low, int high) {
        long temp = a[low];
        while (low < high) {
            while (low < high && a[high] > temp) {
                high--;
            }
            // find first value < a[low]
            a[low] = a[high];
            // find first value > a[high]
            while (low < high && a[low] < temp) {
                low++;
            }
            a[high] = a[low];
        }

        a[low] = temp;
        return low;
    }

    public static void quickSort(double[] a) {
        int length = a.length;
        if (length <= 1) return;

        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(double[] a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, low, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    private static int getMiddle(double[] a, int low, int high) {
        double temp = a[low];
        while (low < high) {
            while (low < high && a[high] > temp) {
                high--;
            }
            // find first value < a[low]
            a[low] = a[high];
            // find first value > a[high]
            while (low < high && a[low] < temp) {
                low++;
            }
            a[high] = a[low];
        }

        a[low] = temp;
        return low;
    }

    public static <T extends Comparable<T>> void quickSort(T[] a) {
        int length = a.length;
        if (length <= 1) return;

        quickSort(a, 0, a.length - 1);
    }

    private static <T extends Comparable<T>> void quickSort(T[] a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, low, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    private static <T extends Comparable<T>> int getMiddle(T[] a, int low, int high) {
        T temp = a[low];
        while (low < high) {
            while (low < high && a[high].compareTo(temp) > 0) {
                high--;
            }
            // find first value < a[low]
            a[low] = a[high];
            // find first value > a[high]
            while (low < high && a[low].compareTo(temp) < 0) {
                low++;
            }
            a[high] = a[low];
        }

        a[low] = temp;
        return low;
    }

    public static <T> void quickSort(T[] a, Comparator<T> c) {
        int length = a.length;
        if (length <= 1) return;

        quickSort(a, 0, a.length - 1, c);
    }

    private static <T> void quickSort(T[] a, int low, int high, Comparator<T> c) {
        if (low < high) {
            int middle = getMiddle(a, low, high, c);
            quickSort(a, low, middle - 1, c);
            quickSort(a, middle + 1, high, c);
        }
    }

    private static <T> int getMiddle(T[] a, int low, int high, Comparator<T> c) {
        T temp = a[low];
        while (low < high) {
            while (low < high && c.compare(a[high], temp) > 0) {
                high--;
            }
            // find first value < a[low]
            a[low] = a[high];
            // find first value > a[high]
            while (low < high && c.compare(a[low], temp) < 0) {
                low++;
            }
            a[high] = a[low];
        }

        a[low] = temp;
        return low;
    }

    public static <T extends Comparable<T>> void quickSort(List<T> a) {
        int size = a.size();
        if (size <= 1) return;

        quickSort(a, 0, size - 1);
    }

    private static <T extends Comparable<T>> void quickSort(List<T> a, int low, int high) {
        if (low < high) {
            int middle = getMiddle(a, low, high);
            quickSort(a, low, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    private static <T extends Comparable<T>> int getMiddle(List<T> a, int low, int high) {
        T temp = a.get(low);
        while (low < high) {
            while (low < high && a.get(high).compareTo(temp) > 0) {
                high--;
            }
            a.set(low, a.get(high));
            while (low < high && a.get(low).compareTo(temp) < 0) {
                low++;
            }
            a.set(high, a.get(low));
        }

        a.set(low, temp);
        return low;
    }

    public static <T> void quickSort(List<T> a, Comparator<T> c) {
        int size = a.size();
        if (size <= 1) return;

        quickSort(a, 0, size - 1, c);
    }

    private static <T> void quickSort(List<T> a, int low, int high, Comparator<T> c) {
        if (low < high) {
            int middle = getMiddle(a, low, high, c);
            quickSort(a, low, middle - 1, c);
            quickSort(a, middle + 1, high, c);
        }
    }

    private static <T> int getMiddle(List<T> a, int low, int high, Comparator<T> c) {
        T temp = a.get(low);
        while (low < high) {
            while (low < high && c.compare(a.get(high), temp) > 0) {
                high--;
            }
            a.set(low, a.get(high));
            while (low < high && c.compare(a.get(low), temp) < 0) {
                low++;
            }
            a.set(high, a.get(low));
        }

        a.set(low, temp);
        return low;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

}
