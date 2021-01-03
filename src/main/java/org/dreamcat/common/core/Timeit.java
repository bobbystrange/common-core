package org.dreamcat.common.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.dreamcat.common.function.ThrowableConsumer;
import org.dreamcat.common.function.ThrowableObjectArrayConsumer;
import org.dreamcat.common.function.ThrowableSupplier;
import org.dreamcat.common.function.ThrowableVoidConsumer;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.ObjectUtil;

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
    // use parallel stream to execute
    private boolean parallel = false;

    private Timeit() {
    }

    public static Timeit ofActions() {
        return new Timeit();
    }

    public String runAndFormatUs() {
        return runAndFormatUs("\t");
    }

    public String runAndFormatUs(String delimiter) {
        return runAndFormat("us", 1000., delimiter);
    }

    public String runAndFormatMs() {
        return runAndFormatMs("\t");
    }

    public String runAndFormatMs(String delimiter) {
        return runAndFormat("ms", 1000_000., delimiter);
    }

    public String runAndFormatSec() {
        return runAndFormatSec("\t");
    }

    public String runAndFormatSec(String delimiter) {
        return runAndFormat("s", 1000_000_000., delimiter);
    }

    public String runAndFormat(String unit, double unitBase, String delimiter) {
        return Arrays.stream(run()).mapToObj(it -> String.format("%6.3f%s", it / unitBase, unit))
                .collect(Collectors.joining(delimiter));
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

        IntStream seqs = Arrays.stream(ArrayUtil.rangeOf(size));
        if (parallel) seqs = seqs.parallel();

        seqs.forEach(seq -> run(tss, seq));
        return stat(tss, skip);
    }

    private void run(long[][] tss, int seq) {
        Object o = actions.get(seq);
        if (o instanceof Actor) {
            Actor actor = (Actor) o;
            doAction(tss[seq], actor);
        } else if (o instanceof UnaryActor) {
            UnaryActor action = (UnaryActor) o;
            doUnaryAction(tss[seq], action);
        } else {
            VoidActor action = (VoidActor) o;
            doVoidAction(tss[seq], action);
        }
    }

    public Timeit addAction(
            ThrowableSupplier<Object[]> supplier,
            ThrowableObjectArrayConsumer action) {
        this.actions.add(new Actor(supplier, action));
        return this;
    }

    public Timeit addAction(ThrowableVoidConsumer action) {
        this.actions.add(new VoidActor(action));
        return this;
    }

    public <T> Timeit addUnaryAction(ThrowableSupplier<T> supplier, ThrowableConsumer<T> action) {
        this.actions.add(new UnaryActor(supplier, action));
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

    public Timeit parallel() {
        this.parallel = true;
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public Timeit count(int count) {
        ObjectUtil.requirePositive(count, "count");
        this.count = count;
        return this;
    }

    private void doAction(long[] ts, Actor actor) {
        long t;
        for (int k = 0; k < count; k++) {
            t = 0;
            for (int i = 0; i < repeat; i++) {
                try {
                    Object[] args = actor.supplier.get();
                    long nanoTime = System.nanoTime();
                    actor.action.accept(args);
                    t += System.nanoTime() - nanoTime;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            ts[k] = t;
        }
    }

    private void doUnaryAction(long[] ts, UnaryActor action) {
        long t;
        for (int k = 0; k < count; k++) {
            t = 0;
            for (int i = 0; i < repeat; i++) {
                try {
                    Object arg = action.supplier.get();
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

    private void doVoidAction(long[] ts, VoidActor action) {
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @AllArgsConstructor
    private static class Actor {

        ThrowableSupplier<Object[]> supplier;
        ThrowableObjectArrayConsumer action;
    }

    @AllArgsConstructor
    private static class UnaryActor<T> {

        ThrowableSupplier<T> supplier;
        ThrowableConsumer<T> action;
    }

    @AllArgsConstructor
    private static class VoidActor {

        ThrowableVoidConsumer action;
    }
}
