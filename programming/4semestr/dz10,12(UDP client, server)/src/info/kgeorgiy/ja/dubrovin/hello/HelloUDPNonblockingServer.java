package info.kgeorgiy.ja.dubrovin.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPNonblockingServer implements HelloServer {
    private Selector selector;
    private DatagramChannel datagramChannel;
    private ExecutorService executorService;

    @Override
    public void start(final int port, final int threads) {
        executorService = Executors.newSingleThreadExecutor();
        try {
            selector = Selector.open();
        } catch (final IOException e) {
            System.err.println("Can't open selector");
        }
        try {
            final SocketAddress socketAddress = new InetSocketAddress(port);
            datagramChannel = DatagramChannel.open();
            datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(socketAddress)
                    .configureBlocking(false)
                    .register(selector, SelectionKey.OP_READ);
            executorService.submit(() -> {
                while (!Thread.interrupted()) {
                    try {
                        selector.select(1000);
                    } catch (final IOException e) {
                        System.err.println("Something went wrong with select");
                    }
                    final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    for (final Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext(); ) {
                        final SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            send(selectionKey, receive(selectionKey));
                        }
                        iterator.remove();
                    }
                }
            });
        } catch (final IOException e) {
            System.err.println("Can't open datagram channel");
        }
    }

    private Pair<SocketAddress, String> receive(final SelectionKey selectionKey) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        SocketAddress socketAddress = null;
        try {
            socketAddress = datagramChannel.receive(byteBuffer);
        } catch (final IOException e) {
            System.err.println("Can't receive byte buffer");
        }
        byteBuffer.flip();
        return new Pair<>(socketAddress,
                "Hello, " + StandardCharsets.UTF_8.decode(byteBuffer));
    }

    private void send(final SelectionKey selectionKey, final Pair<SocketAddress, String> pair) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        final String responseString = pair.getResponseString();
        final SocketAddress socketAddress = pair.getSocketAddress();
        try {
            datagramChannel.send(ByteBuffer.wrap(responseString.getBytes(StandardCharsets.UTF_8)), socketAddress);
        } catch (final IOException e) {
            System.err.println("Can't send response string");
        }
    }

    private static class Pair<T, U> {
        private final T socketAddress;
        private final U responseString;

        public Pair(final T socketAddress, final U responseString) {
            this.socketAddress = socketAddress;
            this.responseString = responseString;
        }

        public T getSocketAddress() {
            return socketAddress;
        }

        public U getResponseString() {
            return responseString;
        }
    }

    @Override
    public void close() {
        try {
            selector.close();
            datagramChannel.close();
        } catch (final IOException e) {
            System.err.println("Can't close selector or datagram channel");
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Time limit for closing executor service has been exceeded");
                }
            }
        } catch (final InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static void checkArguments(final String[] args) {
        if (args == null || args.length != 2) {
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

        final int port = Integer.parseInt(args[0]);
        final int threadCount = Integer.parseInt(args[1]);
        new HelloUDPNonblockingServer().start(port, threadCount);
    }
}