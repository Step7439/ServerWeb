import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.logging.Handler;


public class Server {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlets = new ConcurrentHashMap<>();
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

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

        try {
            System.out.println(Thread.currentThread().getName());
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var out = new BufferedOutputStream(socket.getOutputStream());
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                return;

            }
            var reqvest = new Request(parts[0], parts[1]);

            if (!handlets.containsKey(reqvest.getMethod())){
                notFound(out);
            }

            //final var path = parts[1];
            //            if (!validPaths.contains(path)) {
//                out.write((
//                        "HTTP/1.1 404 Not Found\r\n" +
//                                "Content-Length: 0\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                out.flush();
//                return;
//            }

//            final var filePath = Path.of(".", "public", path);
//            final var mimeType = Files.probeContentType(filePath);
//
//            // special case for classic
//            if (path.equals("/classic.html")) {
//                final var template = Files.readString(filePath);
//                final var content = template.replace(
//                        "{time}",
//                        LocalDateTime.now().toString()
//                ).getBytes();
//                out.write((
//                        "HTTP/1.1 200 OK\r\n" +
//                                "Content-Type: " + mimeType + "\r\n" +
//                                "Content-Length: " + content.length + "\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                out.write(content);
//                out.flush();
//                return;
//            }
//
//            final var length = Files.size(filePath);
//            out.write((
//                    "HTTP/1.1 200 OK\r\n" +
//                            "Content-Type: " + mimeType + "\r\n" +
//                            "Content-Length: " + length + "\r\n" +
//                            "Connection: close\r\n" +
//                            "\r\n"
//            ).getBytes());
//            Files.copy(filePath, out);
//            out.flush();
        } catch (
                IOException e) {
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

    public void addHandler(String method, String path, Handler handler) {
        handlets.putIfAbsent(method, new ConcurrentHashMap<>());
        handlets.get(method).put(path, handler);

    }
}

