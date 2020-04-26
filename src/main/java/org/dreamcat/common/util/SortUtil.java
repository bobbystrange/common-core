package org.dreamcat.common.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Create by tuke on 2020/4/4
 */
public class SortUtil {

    // bubble sort
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

    // insert sort
    public static void insertSort(int[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0,k-1]
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

            System.out.printf("k=%s, a=%s\n", k, Arrays.toString(a));

        }
    }

    public static void insertSort(long[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0,k-1]
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

    public static void insertSort(double[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0,k-1]
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

    public static <T extends Comparable<T>> void insertSort(T[] a) {
        int size = a.length;
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0,k-1]
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

    public static <T extends Comparable<T>> void insertSort(List<T> a) {
        int size = a.size();
        // sorted slice [0, k-1], unsorted slice [k, size-1]
        // insert k to [0,k-1]
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

    // bin sort, box
    @SuppressWarnings("unchecked")
    public static <E> void binSort(LinkedList<E> list, Function<E, Integer> scorer, int bound) {
        int size = list.size();
        LinkedList<E>[] bin = new LinkedList[bound];
        for (int j = bound - 1; j >= 0; j--) {
            bin[j] = new LinkedList<>();
        }

        while (!list.isEmpty()) {
            E e = list.removeFirst();
            bin[scorer.apply(e)].addFirst(e);
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
    public static <E> void radixSort(LinkedList<E> list, BiFunction<E, Integer, Integer> scorer, int binBound, int bound) {
        for (int j = 0; j <= bound - 1; j++) {
            int finalJ = j;
            binSort(list, e -> scorer.apply(e, finalJ), binBound);
        }
    }
}
