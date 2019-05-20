package com.tukeof.common.legacy;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class MatchWhen<T> {

    private T value;
    private Object result;

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====
    private boolean matched;

    private MatchWhen(T value) {
        this.value = value;
        this.result = null;
        this.matched = false;
    }

    public static <T> MatchWhen<T> match(T value) {
        return new MatchWhen<>(value);
    }

    public Object result() {
        return result;
    }

    public MatchWhen<T> when(T other, Function<T, Object> function) {
        if (!isMatched()) {
            if (value.equals(other)) {
                result = function.apply(value);
                setMatched();
            }
        }
        return this;
    }

    public MatchWhen<T> when(Iterable<T> others, Function<T, Object> function) {
        if (!isMatched()) {
            for (T other : others) {
                if (value.equals(other)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public MatchWhen<T> when(T[] others, Function<T, Object> function) {
        if (!isMatched()) {
            for (T other : others) {
                if (value.equals(other)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(int[] others, Function<T, Object> function) {
        if (!isMatched()) {
            for (int other : others) {
                if (value.equals(other)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(long[] others, Function<T, Object> function) {
        if (!isMatched()) {
            for (long other : others) {
                if (value.equals(other)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(double[] others, Function<T, Object> function) {
        if (!isMatched()) {
            for (double other : others) {
                if (value.equals(other)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenContain(String[] contexts, Function<T, Object> function) {
        return whenContain(Arrays.asList(contexts), function);
    }

    public MatchWhen<T> whenContain(Iterable<String> contexts, Function<T, Object> function) {
        if (!isMatched()) {
            for (String context : contexts) {
                if (value.toString().contains(context)) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenContained(String[] contexts, Function<T, Object> function) {
        return whenContained(Arrays.asList(contexts), function);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public MatchWhen<T> whenContained(Iterable<String> contexts, Function<T, Object> function) {
        if (!isMatched()) {
            for (String context : contexts) {
                if (context.contains(value.toString())) {
                    result = function.apply(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenMatcher(String[] regexs, BiFunction<T, Matcher, Object> function) {
        List<Pattern> patterns = Arrays.stream(regexs)
                .map(Pattern::compile)
                .collect(Collectors.toList());

        return whenMatcher(patterns, function);
    }

    public MatchWhen<T> whenMatcher(Pattern[] patterns, BiFunction<T, Matcher, Object> function) {
        return whenMatcher(Arrays.asList(patterns), function);
    }

    public MatchWhen<T> whenMatcher(Iterable<Pattern> patterns, BiFunction<T, Matcher, Object> function) {
        if (!isMatched()) {
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(value.toString());
                if (matcher.matches()) {
                    result = function.apply(value, matcher);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public MatchWhen<T> when(Class<?> clazz, BiFunction<T, Class<?>, Object> function) {
        if (!isMatched()) {
            if (clazz.isInstance(value)) {
                result = function.apply(value, clazz);
                setMatched();
            }
        }
        return this;
    }

    public MatchWhen<T> when(Iterable<Class<?>> clazzes, BiFunction<T, Class<?>, Object> function) {
        if (!isMatched()) {
            for (Class<?> clazz : clazzes) {
                if (clazz.isInstance(value)) {
                    result = function.apply(value, clazz);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> when(Class<?>[] clazzes, BiFunction<T, Class<?>, Object> function) {
        if (!isMatched()) {
            for (Class<?> clazz : clazzes) {
                if (clazz.isInstance(value)) {
                    result = function.apply(value, clazz);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> orElse(Function<T, Object> function) {
        if (!isMatched()) {
            result = function.apply(value);
            setMatched();
        }
        return this;
    }

    public MatchWhen<T> when(T other, Consumer<T> consumer) {
        if (!isMatched()) {
            if (value.equals(other)) {
                consumer.accept(value);
                setMatched();
            }
        }
        return this;
    }

    public MatchWhen<T> when(Iterable<T> others, Consumer<T> consumer) {
        if (!isMatched()) {
            for (T other : others) {
                if (value.equals(other)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public MatchWhen<T> when(T[] others, Consumer<T> consumer) {
        if (!isMatched()) {
            for (T other : others) {
                if (value.equals(other)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(int[] others, Consumer<T> consumer) {
        if (!isMatched()) {
            for (int other : others) {
                if (value.equals(other)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(long[] others, Consumer<T> consumer) {
        if (!isMatched()) {
            for (long other : others) {
                if (value.equals(other)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenPrimitive(double[] others, Consumer<T> consumer) {
        if (!isMatched()) {
            for (double other : others) {
                if (value.equals(other)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenContain(String[] contexts, Consumer<T> consumer) {
        return whenContain(Arrays.asList(contexts), consumer);
    }

    public MatchWhen<T> whenContain(Iterable<String> contexts, Consumer<T> consumer) {
        if (!isMatched()) {
            for (String context : contexts) {
                if (value.toString().contains(context)) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenContained(String[] contexts, Consumer<T> consumer) {
        return whenContained(Arrays.asList(contexts), consumer);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public MatchWhen<T> whenContained(Iterable<String> contexts, Consumer<T> consumer) {
        if (!isMatched()) {
            for (String context : contexts) {
                if (context.contains(value.toString())) {
                    consumer.accept(value);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> whenMatcher(String[] regexs, BiConsumer<T, Matcher> consumer) {
        List<Pattern> patterns = Arrays.stream(regexs)
                .map(Pattern::compile)
                .collect(Collectors.toList());

        return whenMatcher(patterns, consumer);
    }

    public MatchWhen<T> whenMatcher(Pattern[] patterns, BiConsumer<T, Matcher> consumer) {
        return whenMatcher(Arrays.asList(patterns), consumer);
    }

    public MatchWhen<T> whenMatcher(Iterable<Pattern> patterns, BiConsumer<T, Matcher> consumer) {
        if (!isMatched()) {
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(value.toString());
                if (matcher.matches()) {
                    consumer.accept(value, matcher);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public MatchWhen<T> when(Class<?> clazz, BiConsumer<T, Class<?>> consumer) {
        if (!isMatched()) {
            if (clazz.isInstance(value)) {
                consumer.accept(value, clazz);
                setMatched();
            }
        }
        return this;
    }

    public MatchWhen<T> when(Iterable<Class<?>> clazzes, BiConsumer<T, Class<?>> consumer) {
        if (!isMatched()) {
            for (Class<?> clazz : clazzes) {
                if (clazz.isInstance(value)) {
                    consumer.accept(value, clazz);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public MatchWhen<T> when(Class<?>[] clazzes, BiConsumer<T, Class<?>> consumer) {
        if (!isMatched()) {
            for (Class<?> clazz : clazzes) {
                if (clazz.isInstance(value)) {
                    consumer.accept(value, clazz);
                    setMatched();
                    break;
                }
            }
        }
        return this;
    }

    public boolean isMatched() {
        return matched;
    }

    private void setMatched() {
        this.matched = true;
    }

    public MatchWhen<T> orElse(Consumer<T> consumer) {
        if (!isMatched()) {
            consumer.accept(value);
            setMatched();
        }
        return this;
    }


}
