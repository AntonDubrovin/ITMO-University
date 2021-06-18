package info.kgeorgiy.ja.dubrovin.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class HelloUDPNonblockingClient implements HelloClient {
    @Override
    public void run(final String host, final int port, final String prefix, final int threads, final int requests) {
        final SocketAddress socketAddress = new InetSocketAddress(host, port);
        final Map<DatagramChannel, Pair<Integer>> channelsAndRequests = new HashMap<>();

        try (final Selector selector = Selector.open()) {
            IntStream.range(0, threads)
                    .forEach(i -> {
                        try {
                            final DatagramChannel datagramChannel = DatagramChannel.open();
                            datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                    .connect(socketAddress)
                                    .configureBlocking(false)
                                    .register(selector, SelectionKey.OP_WRITE);
                            channelsAndRequests.put(datagramChannel, new Pair<>(i, 0));
                        } catch (final IOException e) {
                            System.err.println("Something went wrong ith datagram channel");
                        }
                    });

            while (!Thread.interrupted() && !selector.keys().isEmpty()) {
                try {
                    selector.select(1000);
                } catch (final IOException e) {
                    System.err.println("Something went wrong with select");
                }
                final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                if (!selectedKeys.isEmpty()) {
                    for (final Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext(); ) {
                        final SelectionKey selectionKey = iterator.next();
                        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
                        if (selectionKey.isReadable()) {
                            if (!receive(datagramChannel, selectionKey, prefix, channelsAndRequests, requests)) {
                                try {
                                    datagramChannel.close();
                                } catch (final IOException e) {
                                    System.err.println("Can't close datagram channel");
                                }
                            }
                        } else if (selectionKey.isWritable()) {
                            send(datagramChannel, selectionKey, prefix, channelsAndRequests);
                        }
                        iterator.remove();
                    }
                } else {
                    for (final SelectionKey selectionKey : selector.keys()) {
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    }
                }
            }
        } catch (final IOException e) {
            System.err.println("Can't open selector");
        }
    }

    private boolean receive(final DatagramChannel datagramChannel, final SelectionKey selectionKey, final String prefix,
                            final Map<DatagramChannel, Pair<Integer>> channelsAndRequests,
                            final int totalRequests) {
        final Pair<Integer> pair = channelsAndRequests.get(datagramChannel);
        int currentRequests = pair.getRequests();
        final int thread = pair.getThread();
        final ByteBuffer buffer = ByteBuffer.allocate(500);
        try {
            datagramChannel.receive(buffer);
        } catch (final IOException e) {
            System.err.println("Can't receive buffer");
        }
        buffer.flip();
        final String responseString = StandardCharsets.UTF_8.decode(buffer).toString();
        final String requestString = prefix + thread + '_' + currentRequests;
        if (responseString.contains(requestString)) {
            System.out.println("Receive " + responseString);
            currentRequests++;
            channelsAndRequests.put(datagramChannel, new Pair<>(thread, currentRequests));
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        return currentRequests != totalRequests;
    }

    private void send(final DatagramChannel datagramChannel, final SelectionKey selectionKey, final String prefix,
                      final Map<DatagramChannel, Pair<Integer>> channelsAndRequests) {
        final Pair<Integer> pair = channelsAndRequests.get(datagramChannel);
        final int currentRequests = pair.getRequests();
        final int thread = pair.getThread();
        final String requestString = prefix + thread + '_' + currentRequests;
        try {
            datagramChannel.write(ByteBuffer.wrap(requestString.getBytes(StandardCharsets.UTF_8)));
            System.out.println("Send " + requestString);
        } catch (final IOException e) {
            System.err.println("Can't send request string");
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    private static class Pair<T> {
        private final T thread;
        private final T requests;

        public Pair(final T thread, final T requests) {
            this.thread = thread;
            this.requests = requests;
        }

        public T getThread() {
            return thread;
        }

        public T getRequests() {
            return requests;
        }
    }

    private static void checkArguments(final String[] args) {
        if (args == null || args.length != 5) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        for (final String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("One of argument is null");
            }
        }
    }

    public static void main(final String[] args) {
        checkArguments(args);

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final String prefix = args[2];
        final int threads = Integer.parseInt(args[3]);
        final int requests = Integer.parseInt(args[4]);
        new HelloUDPNonblockingClient().run(host, port, prefix, threads, requests);
    }
}
