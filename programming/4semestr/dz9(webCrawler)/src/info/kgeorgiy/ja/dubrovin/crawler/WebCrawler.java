package info.kgeorgiy.ja.dubrovin.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final int perHost;
    private final Map<String, HostQueue> hostQueues;

    private class HostQueue {
        private final Queue<Runnable> tasksQueue = new ArrayDeque<>();
        private int count = 0;

        private synchronized void addTask(final Runnable task) {
            if (count < perHost) {
                count++;
                downloaders.submit(task);
            } else {
                tasksQueue.add(task);
            }
        }

        private synchronized void nextTask() {
            final Runnable task = tasksQueue.poll();
            if (task != null) {
                downloaders.submit(task);
            } else {
                count--;
            }
        }
    }

    public WebCrawler(final Downloader downloader, final int downloaders, final int extractors, final int perHost) {
        this.downloader = downloader;
        this.perHost = perHost;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        hostQueues = new ConcurrentHashMap<>();
    }

    @Override
    public Result download(final String url, final int depth) {
        final Set<String> downloaded = ConcurrentHashMap.newKeySet();
        final Map<String, IOException> errors = new ConcurrentHashMap<>();
        final Phaser phaser = new Phaser();
        phaser.register();
        download(url, depth, downloaded, errors, phaser);
        phaser.arriveAndAwaitAdvance();
        downloaded.removeAll(errors.keySet());
        return new Result(new ArrayList<>(downloaded), errors);
    }

    private void download(final String url, final int depth, final Set<String> downloaded,
                          final Map<String, IOException> errors, final Phaser phaser) {
        if (downloaded.add(url)) {
            try {
                final String host = URLUtils.getHost(url);
                final HostQueue hostQueue = hostQueues.computeIfAbsent(host, name -> new HostQueue());
                phaser.register();
                hostQueue.addTask(() -> {
                    try {
                        downloadPages(url, depth, downloaded, errors, phaser);
                    } finally {
                        phaser.arrive();
                        hostQueue.nextTask();
                    }
                });
            } catch (final IOException e) {
                errors.put(url, e);
            }
        }
    }

    private void downloadPages(final String url, final int depth, final Set<String> downloaded,
                               final Map<String, IOException> errors, final Phaser phaser) {
        try {
            final Document page = downloader.download(url);
            if (depth > 1) {
                phaser.register();
                extractors.submit(() -> {
                    try {
                        page.extractLinks().forEach((link) -> download(link, depth - 1, downloaded, errors, phaser));
                    } catch (final IOException e) {
                        errors.put(url, e);
                    } finally {
                        phaser.arrive();
                    }
                });
            }
        } catch (final IOException e) {
            errors.put(url, e);
        }
    }

    @Override
    public void close() {
        shutdownAndWait(downloaders);
        shutdownAndWait(extractors);
    }

    private void shutdownAndWait(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Time limit for closing executor service has been exceeded");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static int setArgs(final String[] args, final int index) {
        return index < args.length ? Integer.parseInt(args[index]) : 1;
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1 || args.length > 5) {
            throw new IllegalArgumentException("Illegal arguments");
        }

        final String url = args[0];
        final int depth = setArgs(args, 1);
        final int downloads = setArgs(args, 2);
        final int extractors = setArgs(args, 3);
        final int perHost = setArgs(args, 4);

        try (final WebCrawler webCrawler = new WebCrawler(new CachingDownloader(), downloads, extractors, perHost)) {
            webCrawler.download(url, depth);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}