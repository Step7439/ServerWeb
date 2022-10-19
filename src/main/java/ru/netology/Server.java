package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;


public class Server {
    public static final String GET = "GET";
    public static final String POST = "POST";
    ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlets = new ConcurrentHashMap<>();

    public void setup(int port) {
        try (var serverSocket = new ServerSocket(port);) {

            var executorService = Executors.newFixedThreadPool(64);
            System.out.println("Starting server!!!");

            while (true) {

                Socket socket = serverSocket.accept();
                executorService.submit(() -> listen(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void listen(Socket socket) {
        final var allowedMethods = List.of(GET, POST);
        try (
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            System.out.println(Thread.currentThread().getName());

            final var limit = 4096;

            in.mark(limit);
            final var buffer = new byte[limit];
            final var read = in.read();

            // ищем request line
            final var requestLineDelimiter = new byte[]{'\r', '\n'};
            final var requestLineEnd = indexOf(buffer, requestLineDelimiter, 0, read);
            if (requestLineEnd == -1) {
                badRequest(out);
                return;
            }

            // читаем request line
            final var requestLine = new String(Arrays.copyOf(buffer, requestLineEnd)).split(" ");
            if (requestLine.length != 3) {
                badRequest(out);
                return;
            }

            final var method = requestLine[0];
            if (!allowedMethods.contains(method)) {
                badRequest(out);
                return;
            }
            System.out.println(method);

            final var path = requestLine[1];
            if (!path.startsWith("/")) {
                badRequest(out);
               return;
            }
var reqvest = new Request()
            var methodHandlers = handlets.get(.getMethod());


            if (!methodHandlers.containsKey(reqvest.getPath())) {
                notFound(out);
                return;
            }

            var handler = methodHandlers.get(reqvest.getPath());

            if (handler == null) {
                notFound(out);
                return;
            }

            handler.handle(reqvest, out);

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(Exception e) {
        if (!(e instanceof SocketException)) {
            e.printStackTrace();
        }
    }

    public void notFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    public void badRequest(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    public void addHandler(String method, String path, Handler handler) {
        handlets.putIfAbsent(method, new ConcurrentHashMap<>());
        handlets.get(method).put(path, handler);

    }
    private static int indexOf(byte[] array, byte[] target, int start, int max) {
        outer:
        for (int i = start; i < max - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}

