package org.dreamcat.common.util;

public final class Sorts {

    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private static void swap(int[][] data, int i1, int j1, int i2, int j2) {
        int tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    private static void swap(double[] data, int i, int j) {
        double tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private static void swap(double[][] data, int i1, int j1, int i2, int j2) {
        double tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;
    }

    public static void shellSort(int[] data) {
        int j = 0;
        int temp = 0;

        for (int increment = data.length / 2; increment > 0; increment /= 2) {
            for (int i = increment; i < data.length; i++) {
                temp = data[i];
                for (j = i; j >= increment; j -= increment) {

                    if (temp > data[j - increment]) {
                        data[j] = data[j - increment];
                    } else {
                        break;
                    }

                }
                data[j] = temp;
            }

        }
    }

    public static void bhsort(int[] a) {

        int n = a.length;

        for (int i = 0; i < n - 1; i++) {

            buildBigHeap(a, n - 1 - i);

            swap(a, 0, n - 1 - i);
        }

    }

    private static void buildBigHeap(int[] data, int lastIndex) {

        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {

            int k = i;

            while (k * 2 + 1 <= lastIndex) {

                int biggerIndex = 2 * k + 1;

                if (biggerIndex < lastIndex) {

                    if (data[biggerIndex] < data[biggerIndex + 1]) {

                        biggerIndex++;
                    }
                }

                if (data[k] < data[biggerIndex]) {

                    swap(data, k, biggerIndex);
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

    public static void lhsort(int[] a) {

        int n = a.length;

        for (int i = 0; i < n - 1; i++) {
            buildLittleHeap(a, n - 1 - i);

            swap(a, 0, n - 1 - i);
        }
    }

    private static void buildLittleHeap(int[] data, int last) {
        for (int i = (last - 1) / 2; i >= 0; i--) {
            int parent = i;
            while (2 * parent + 1 <= last) {
                int bigger = 2 * parent + 1;
                if (bigger < last) {

                    if (data[bigger] > data[bigger + 1]) {

                        bigger = bigger + 1;
                    }
                }
                if (data[parent] > data[bigger]) {
                    swap(data, parent, bigger);
                    parent = bigger;
                } else {
                    break;
                }
            }
        }
    }

    public static void hsort(int[] a) {
        int k = a.length;
        for (int i = 0; i < k; i++) {
            nohsort(a, i, k);
        }
    }

    public static void hsort(int[] a, int k) {
        int n = a.length;
        if (k >= n) k = n;
        for (int i = 0; i < k; i++) {
            nohsort(a, i, n);
        }
    }

    private static void nohsort(int[] input, int root, int end) {
        for (int j = end - 1; j >= root; j--) {
            int parent = (j + root - 1) / 2;
            if (input[parent] > input[j]) {
                int temp = input[j];
                input[j] = input[parent];
                input[parent] = temp;
            }
        }
    }

    public static int[] msort(int[] nums) {
        int low = 0, high = nums.length;
        int mid = (low + high) / 2;
        if (low < high) {
            msort(nums, low, mid);
            msort(nums, mid + 1, high);
            merge(nums, low, mid, high);
        }
        return nums;
    }

    private static int[] msort(int[] nums, int low, int high) {
        int mid = (low + high) / 2;
        if (low < high) {
            msort(nums, low, mid);
            msort(nums, mid + 1, high);
            merge(nums, low, mid, high);
        }
        return nums;
    }

    private static void merge(int[] nums, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= high) {
            if (nums[i] < nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = nums[i++];
        }

        while (j <= high) {
            temp[k++] = nums[j++];
        }

        //for (int k2 = 0; k2 < temp.length; k2++) {
        //    nums[k2 + low] = temp[k2];
        //}
        if (temp.length > 0) {
            System.arraycopy(temp, 0, nums, low, temp.length);
        }
    }

}
