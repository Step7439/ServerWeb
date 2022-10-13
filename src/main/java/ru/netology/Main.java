package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    final static int PORT = 9999;

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.addHandler("GET", "/classic.html", Main::prossesFile);
        server.addHandler("GET", "/events.html", Main::prossesFile);
        server.addHandler("GET", "/forms.html", Main::prossesFile);
        server.addHandler("GET", "/index.html", Main::prossesFile);
        server.addHandler("GET", "/links.html", Main::prossesFile);
        server.addHandler("GET", "/resources.html", Main::prossesFile);


        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream out) throws IOException {
                var messeger = "Hello World GET";
                out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + "text/plain" + "\r\n" +
                            "Content-Length: " + messeger.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                        messeger
            ).getBytes());
            out.flush();

            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream out) throws IOException {
                var messeger = "Hello World POST";
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + "text/plain" + "\r\n" +
                                "Content-Length: " + messeger.length() + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n" +
                                messeger
                ).getBytes());
                out.flush();
            }
        });

        server.setup(PORT);
    }
    public static void prossesFile(Request request, BufferedOutputStream out) throws IOException {
        final var filePath = Path.of(".", "public", request.getPath());
        final var mimeType = Files.probeContentType(filePath);

        // special case for classic

        final var template = Files.readString(filePath);
        final var content = template.replace(
                "{time}",
                LocalDateTime.now().toString()
        ).getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
        out.flush();
    }
}