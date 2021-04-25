package info.kgeorgiy.ja.dubrovin.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterativeParallelism implements ListIP {
    private final ParallelMapper mapper;

    public IterativeParallelism() {
        this(null);
    }

    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    private <T, U> U handle(final int threads, final List<? extends T> values,
                            final Function<Stream<? extends T>, ? extends U> localMap,
                            final Function<Stream<? extends U>, ? extends U> globalMap) throws InterruptedException {
        final List<Stream<? extends T>> parts = split(threads, values);
        List<U> result;
        if (mapper != null) {
            result = mapper.map(localMap, parts);
        } else {
            List<Thread> threadsList = new ArrayList<>();
            result = new ArrayList<>(Collections.nCopies(parts.size(), null));
            for (int i = 0; i < parts.size(); i++) {
                final int index = i;
                threadsList.add(new Thread(() -> result.set(index, localMap.apply(parts.get(index)))));
                threadsList.get(i).start();
            }
            threadsCompletion(threadsList);
        }
        return globalMap.apply(result.stream());
    }

    private void threadsCompletion(List<Thread> threadList) throws InterruptedException {
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new InterruptedException("Threads can't join");
            }
        }
    }

    private <T> List<Stream<? extends T>> split(int threads, final List<? extends T> values) throws InterruptedException {
        if (threads < 1) {
            throw new InterruptedException("Incorrect count of threads");
        }

        threads = Math.min(threads, values.size());
        final int parts = values.size() / threads;
        final int remainder = values.size() % threads;
        final List<Stream<? extends T>> threadList = new ArrayList<>();
        int left = 0;
        int right;
        for (int i = 0; i < threads; i++) {
            right = left + parts + (remainder > i ? 1 : 0);
            threadList.add(values.subList(left, right).stream());
            left = right;
        }
        return threadList;
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return handle(threads, values, x -> x.map(Object::toString).collect(Collectors.joining()),
                x -> x.collect(Collectors.joining()));
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return handle(threads, values, x -> x.filter(predicate).collect(Collectors.toList()),
                x -> x.flatMap(List::stream).collect(Collectors.toList()));
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        return handle(threads, values, x -> x.map(f).collect(Collectors.toList()),
                x -> x.flatMap(List::stream).collect(Collectors.toList()));
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return minimum(threads, values, comparator.reversed());
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        Function<Stream<? extends T>, ? extends T> minimum = x -> x.min(comparator).orElse(null);
        return handle(threads, values, minimum, minimum);
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return handle(threads, values, x -> x.allMatch(predicate), x -> x.allMatch(Boolean::booleanValue));
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }
}