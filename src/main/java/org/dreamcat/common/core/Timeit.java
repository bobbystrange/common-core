package org.dreamcat.common.core;

import lombok.AllArgsConstructor;
import org.dreamcat.common.function.ThrowableConsumer;
import org.dreamcat.common.function.ThrowableObjectArrayConsumer;
import org.dreamcat.common.function.ThrowableVoidConsumer;
import org.dreamcat.common.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-06-06
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Timeit {
    private final List actions = new ArrayList();
    // assert skip < count
    // such as count=100, skip=10, then discard head 10 & tail 10 before merge
    private int skip = 0;
    // repeat times, one action avg time is `the result or run()` / repeat
    // such as one action cost 1ms, if repea=12 then total cost almost equal 12ms
    private int repeat = 1;
    // multi metering
    private int count = 1;

    private Timeit() {
    }

    public static Timeit ofActions() {
        return new Timeit();
    }

    public static String formatUs(long[] ts, String delimiter) {
        return Arrays.stream(ts).mapToObj(it -> String.format("%6.3fus", it / 1000.)).collect(Collectors.joining(delimiter));
    }

    /**
     * run actions and stat its elapsed time
     *
     * @return nanoTime array for actions
     * @throws RuntimeException if any action throw a exception
     */
    public long[] run() {
        int size = actions.size();
        long[][] tss = new long[size][count];
        for (int seq = 0; seq < size; seq++) {
            Object o = actions.get(seq);
            if (o instanceof Action) {
                Action action = (Action) o;
                doAction(tss[seq], action);
            } else if (o instanceof UnaryAction) {
                UnaryAction action = (UnaryAction) o;
                doUnaryAction(tss[seq], action);
            } else {
                VoidAction action = (VoidAction) o;
                doVoidAction(tss[seq], action);
            }
        }
        return stat(tss, skip);
    }

    public Timeit addAction(Supplier<Object[]> supplier, ThrowableObjectArrayConsumer action) {
        this.actions.add(new Action(supplier, action));
        return this;
    }

    public <T> Timeit addAction(ThrowableVoidConsumer action) {
        this.actions.add(new VoidAction(action));
        return this;
    }

    public <T> Timeit addUnaryAction(Supplier<T> supplier, ThrowableConsumer<T> action) {
        this.actions.add(new UnaryAction(supplier, action));
        return this;
    }

    public Timeit skip(int skip) {
        ObjectUtil.requirePositive(skip, "skip");
        this.skip = skip;
        return this;
    }

    public Timeit repeat(int repeat) {
        ObjectUtil.requirePositive(repeat, "repeat");
        this.repeat = repeat;
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public Timeit count(int count) {
        ObjectUtil.requirePositive(count, "count");
        this.count = count;
        return this;
    }

    private void doAction(long[] ts, Action action) {
        long t;
        for (int k = 0; k < count; k++) {
            t = 0;
            for (int i = 0; i < repeat; i++) {
                Object[] args = action.supplier.get();
                try {
                    long nanoTime = System.nanoTime();
                    action.action.accept(args);
                    t += System.nanoTime() - nanoTime;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ts[k] = t;
        }
    }

    private void doUnaryAction(long[] ts, UnaryAction action) {
        long t;
        for (int k = 0; k < count; k++) {
            t = 0;
            for (int i = 0; i < repeat; i++) {
                Object arg = action.supplier.get();
                try {
                    long nanoTime = System.nanoTime();
                    action.action.accept(arg);
                    t += System.nanoTime() - nanoTime;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ts[k] = t;
        }
    }

    private void doVoidAction(long[] ts, VoidAction action) {
        long t;
        for (int k = 0; k < count; k++) {
            t = 0;
            for (int i = 0; i < repeat; i++) {
                try {
                    long nanoTime = System.nanoTime();
                    action.action.accept();
                    t += System.nanoTime() - nanoTime;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ts[k] = t;
        }
    }

    private long[] stat(long[][] tss, int skip) {
        int len = tss.length;
        long[] avgs = new long[len];
        for (int k = 0; k < len; k++) {
            long[] ts = tss[k];
            Arrays.sort(ts);
            int s = ts.length;
            int c = 0;
            long avg = 0;
            for (int i = 0; i < s; i++) {
                // if skip = 10, then
                // skip 0, 1, ..., 9 or len-10, ..., len - 1
                if (i < skip || i >= s - skip) continue;
                c++;
                avg += ts[i];
            }
            avgs[k] = avg / c;
        }
        return avgs;
    }

    @AllArgsConstructor
    private static class Action {
        Supplier<Object[]> supplier;
        ThrowableObjectArrayConsumer action;
    }

    @AllArgsConstructor
    private static class UnaryAction<T> {
        Supplier<T> supplier;
        ThrowableConsumer<T> action;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @AllArgsConstructor
    private static class VoidAction {
        ThrowableVoidConsumer action;
    }
}
