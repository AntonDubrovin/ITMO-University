package info.kgeorgiy.ja.dubrovin.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerError;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPClient implements HelloClient {
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        final SocketAddress socketAddress = new InetSocketAddress(host, port);
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            final int threadID = i;
            executorService.submit(new Thread(() -> {
                try (final DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(1000);
                    final DatagramPacket response = new DatagramPacket(
                            new byte[socket.getReceiveBufferSize()],
                            socket.getReceiveBufferSize()
                    );

                    for (int requestID = 0; requestID < requests; requestID++) {
                        final String requestString = prefix + threadID + "_" + requestID;
                        final DatagramPacket request = new DatagramPacket(
                                requestString.getBytes(StandardCharsets.UTF_8),
                                requestString.length(),
                                socketAddress
                        );

                        while (!socket.isClosed()) {
                            try {
                                socket.send(request);
                                socket.receive(response);

                                String answer = new String(
                                        response.getData(), response.getOffset(),
                                        response.getLength(), StandardCharsets.UTF_8
                                );
                                if (answer.contains(requestString)) {
                                    System.out.println(answer);
                                    break;
                                }
                            } catch (final IOException e) {
                                System.err.println("Error while socket send or receive: " + e.getMessage());
                            }
                        }
                    }
                } catch (final SocketException e) {
                    System.err.println("Failed socket: " + e.getMessage());
                }
            }));
        }

        shutdownAndWait(executorService);
    }

    private void shutdownAndWait(final ExecutorService executorService) {
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

    private static void checkArguments(String[] args) {
        if (args == null || args.length != 5) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        for (String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("One of argument is null");
            }
        }
    }

    public static void main(String[] args) {
        checkArguments(args);

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        String prefix = args[2];
        int threads = Integer.parseInt(args[3]);
        int requests = Integer.parseInt(args[4]);
        new HelloUDPClient().run(host, port, prefix, threads, requests);
    }
}
