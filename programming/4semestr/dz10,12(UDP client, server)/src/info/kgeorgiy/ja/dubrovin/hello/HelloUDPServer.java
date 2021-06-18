package info.kgeorgiy.ja.dubrovin.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer implements HelloServer {
    private DatagramSocket socket;
    private ExecutorService senders;
    private ExecutorService workers;

    @Override
    public void start(int port, int threads) {
        try {
            socket = new DatagramSocket(port);
            senders = Executors.newFixedThreadPool(threads);
            workers = Executors.newSingleThreadExecutor();

            workers.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        final DatagramPacket request = new DatagramPacket(
                                new byte[socket.getReceiveBufferSize()],
                                socket.getReceiveBufferSize()
                        );
                        socket.receive(request);

                        senders.submit(() -> {
                            try {
                                final String answer = new String(
                                        request.getData(), request.getOffset(),
                                        request.getLength(), StandardCharsets.UTF_8
                                );
                                final String helloAnswer = "Hello, " + answer;
                                final DatagramPacket response = new DatagramPacket(
                                        helloAnswer.getBytes(StandardCharsets.UTF_8),
                                        helloAnswer.length(),
                                        request.getSocketAddress()
                                );
                                socket.send(response);
                            } catch (final IOException e) {
                                System.err.println("Error while socket send: " + e.getMessage());
                            }
                        });
                    } catch (final IOException e) {
                        System.err.println("Error while socket receive: " + e.getMessage());
                    }
                }
            });
        } catch (final SocketException e) {
            System.err.println("Failed socket: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        socket.close();
        workers.shutdownNow();
        senders.shutdownNow();
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
        final int threads = Integer.parseInt(args[1]);
        new HelloUDPServer().start(port, threads);
    }
}
