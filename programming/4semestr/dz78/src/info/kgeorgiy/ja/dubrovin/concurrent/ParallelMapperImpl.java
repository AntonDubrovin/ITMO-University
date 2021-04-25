package info.kgeorgiy.ja.dubrovin.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> threads = new ArrayList<>();
    private final Queue<Tasks> tasksQueue = new ArrayDeque<>();
    private final Map<Integer, Integer> workingTasks = new LinkedHashMap<>();
    private int key = 0;

    private static class Tasks {
        Runnable task;
        int id;

        Tasks(Runnable task, int id) {
            this.id = id;
            this.task = task;
        }
    }

    public ParallelMapperImpl(final int threadsCount) {
        if (threadsCount < 1) {
            throw new IllegalArgumentException("Incorrect count of threads");
        }

        final Runnable thread = () -> {
            try {
                while (!Thread.interrupted()) {
                    solveTask();
                }
            } catch (InterruptedException ignored) {
            } finally {
                Thread.currentThread().interrupt();
            }
        };
        for (int i = 0; i < threadsCount; i++) {
            threads.add(new Thread(thread));
            threads.get(i).start();
        }
    }

    private void solveTask() throws InterruptedException {
        Tasks currentTask;
        synchronized (tasksQueue) {
            while (tasksQueue.isEmpty()) {
                tasksQueue.wait();
            }
            currentTask = tasksQueue.poll();
        }

        currentTask.task.run();

        synchronized (workingTasks) {
            workingTasks.put(currentTask.id, workingTasks.get(currentTask.id) - 1);
            workingTasks.notify();
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        workingTasks.put(key, args.size());
        List<R> result = new ArrayList<>(Collections.nCopies(args.size(), null));

        for (int i = 0; i < args.size(); i++) {
            final int curElemInd = i;
            tasksQueue.add(new Tasks(() -> result.set(curElemInd, f.apply(args.get(curElemInd))), key));
            synchronized (tasksQueue) {
                tasksQueue.notify();
            }
        }

        synchronized (workingTasks) {
            while (workingTasks.get(key) != 0) {
                workingTasks.wait();
            }
        }

        key++;

        return result;
    }

    @Override
    public void close() {
        threads.forEach(Thread::interrupt);
    }
}