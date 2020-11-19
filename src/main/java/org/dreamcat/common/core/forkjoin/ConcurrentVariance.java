package org.dreamcat.common.core.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Create by tuke on 2019-05-24
 */
public class ConcurrentVariance {

    public static double mean(double[] data, int seq) {
        ForkJoinTask<Double> task = new MeanRecursiveTask(data, 0, data.length, seq);
        return ForkJoinPool.commonPool().invoke(task);
    }

    public static double variance(double[] data, int seq) {
        double mean = mean(data, seq);
        ForkJoinTask<Double> task = new VarianceRecursiveTask(data, 0, data.length, mean, seq);
        return ForkJoinPool.commonPool().invoke(task);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class MeanRecursiveTask extends RecursiveTask<Double> {

        private double[] data;
        private int start;
        private int end;
        private int seq;

        @Override
        protected Double compute() {
            double sum = 0;
            double mean = 0;
            int len = data.length;
            if (end - start < seq) {
                for (int k = start; k < end; k++) {
                    sum += data[k];
                }
                mean += sum / len;
            } else {
                int middle = (start + end) / 2;
                MeanRecursiveTask subTaskA = new MeanRecursiveTask(data, start, middle, seq);
                MeanRecursiveTask subTaskB = new MeanRecursiveTask(data, middle, end, seq);
                subTaskA.fork();
                subTaskB.fork();

                mean = subTaskA.join() + subTaskB.join();
            }
            return mean;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class VarianceRecursiveTask extends RecursiveTask<Double> {

        private double[] data;
        private int start;
        private int end;
        private double mean;
        private int seq;

        @Override
        protected Double compute() {
            double sum = 0;
            double variance = 0;
            int len = data.length;
            if (end - start < seq) {
                for (int k = start; k < end; k++) {
                    sum += Math.pow(mean - data[k], 2);
                }
                variance += sum / len;
            } else {
                int middle = (start + end) / 2;
                VarianceRecursiveTask subTaskA = new VarianceRecursiveTask(
                        data, start, middle, mean, seq);
                VarianceRecursiveTask subTaskB = new VarianceRecursiveTask(
                        data, middle, end, mean, seq);
                subTaskA.fork();
                subTaskB.fork();

                variance = subTaskA.join() + subTaskB.join();
            }
            return variance;
        }
    }
}
