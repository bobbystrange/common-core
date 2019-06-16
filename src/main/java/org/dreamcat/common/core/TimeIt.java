package org.dreamcat.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dreamcat.common.function.ObjectArrayConsumer;
import org.dreamcat.common.function.ObjectArrayFunction;
import org.dreamcat.common.function.VoidConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Create by tuke on 2019-06-06
 */
@Data
public class TimeIt {
    private int skip = 0;
    private int repeat = 1;
    private int count = 1;
    private List<InnerAction> actions = new ArrayList<>();
    private List<InnerFunction> functions = new ArrayList<>();

    public long[] runForActions() {
        long t;
        int size = actions.size();
        long[][] tss = new long[size][count];
        for (int seq=0; seq<size; seq++){
            InnerAction innerAction = actions.get(seq);
            for (int k=0; k<count; k++){
                t = 0;
                for (int i=0; i<repeat; i++){
                    Object[] args = innerAction.supplier.get();
                    long ts = System.nanoTime();
                    innerAction.action.accept(args);
                    t += System.nanoTime() - ts;
                }
                tss[seq][k] = t;
            }
        }
        return stat(tss, skip);
    }

    public long[] runForFunctions() {
        long t;
        int size = functions.size();
        long[][] tss = new long[size][count];
        for (int seq=0; seq<size; seq++){
            InnerFunction innerFunction = functions.get(seq);
            for (int k=0; k<count; k++){
                t = 0;
                for (int i=0; i<repeat; i++){
                    Object[] args = innerFunction.supplier.get();
                    long ts = System.nanoTime();
                    innerFunction.function.apply(args);
                    t += System.nanoTime() - ts;
                }
                tss[seq][k] = t;
            }
        }
        return stat(tss, skip);
    }

    public TimeIt addAction(Supplier<Object[]> supplier, ObjectArrayConsumer action) {
        this.actions.add(new InnerAction(supplier, action));
        return this;
    }

    public TimeIt addFunction(Supplier<Object[]> supplier, ObjectArrayFunction function) {
        this.functions.add(new InnerFunction(supplier, function));
        return this;
    }

    public TimeIt skip(int skip){
        this.skip = skip;
        return this;
    }

    public TimeIt repeat(int repeat){
        this.repeat = repeat;
        return this;
    }

    public TimeIt count(int count){
        this.count = count;
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private long[] stat(long[][] tss, int skip) {
        int len = tss.length;
        long[] avgs = new long[len];
        for (int k=0; k<len; k++){
            long[] ts = tss[k];
            Arrays.sort(ts);
            int s = ts.length;
            int count = 0;
            long avg = 0;
            for (int i = 0; i < s; i++) {
                // if skip = 10, then
                // skip 0, 1, ..., 9 or len-10, ..., len - 1
                if (i < skip || i >= s - skip) continue;
                count++;
                avg += ts[i];
            }
            avgs[k] = avg / count;
        }
        return avgs;
    }

    @AllArgsConstructor
    private static class InnerAction {
        Supplier<Object[]> supplier;
        ObjectArrayConsumer action;
    }

    @AllArgsConstructor
    private static class InnerFunction {
        Supplier<Object[]> supplier;
        ObjectArrayFunction function;
    }

}
