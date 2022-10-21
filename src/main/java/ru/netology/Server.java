package ru.netology;



import org.apache.http.client.utils.URLEncodedUtils;

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
    ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlets = new ConcurrentHashMap<>();

    public void setup(int port) {
        try (var serverSocket = new ServerSocket(port);) {

            var executorService = Executors.newFixedThreadPool(64);
            System.out.println("Starting server!!!");

            while (true) {
                System.out.println("Client connect");
                Socket socket = serverSocket.accept();
                executorService.submit(() -> listen(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void listen(Socket socket)  {

        try (
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            System.out.println(Thread.currentThread().getName());

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                bedRequest(out);
                return;
            }
//            final var request = new Request(parts[0], parts[1]);
//            System.out.println(request.getQueryParams());
//            System.out.println(request.getQueryParam("id"));
            final var request = Request.createRequest(parts[0], parts[1]);

            System.out.println(request.getQueryParams());
            System.out.println(request.getQueryParam("id"));

            if (!handlets.containsKey(request.getMethod())) {
                notFound(out);
                return;
            }

            var methodHandlers = handlets.get(request.getMethod());

            if (!methodHandlers.containsKey(request.getPath())) {
                notFound(out);
                return;
            }

            var handler = methodHandlers.get(request.getPath());

            if (handler == null){
                notFound(out);
                return;
            }

            handler.handle(request, out);

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
    public void bedRequest(BufferedOutputStream out) throws IOException {
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
}