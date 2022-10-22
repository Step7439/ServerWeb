package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    final static int PORT = 9999;

    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/classic.html", Main::prossesFile);
        server.addHandler("GET", "/events.html", Main::prossesFile);
        server.addHandler("GET", "/forms.html", Main::prossesFile);
        server.addHandler("GET", "/index.html", Main::prossesFile);
        server.addHandler("GET", "/links.html", Main::prossesFile);
        server.addHandler("GET", "/resources.html", Main::prossesFile);
        server.addHandler("GET", "/events.js", Main::prossesFile);
        server.addHandler("GET", "/spring.png", Main::prossesFile);
        server.addHandler("GET", "/spring.svg", Main::prossesFile);
        server.addHandler("GET", "/styles.css", Main::prossesFile);
        server.addHandler("GET", "/app.js", Main::prossesFile);


        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", Main::prossesFile);
        server.addHandler("POST", "/messages", Main::prossesFile);


        server.setup(PORT);
    }
    public static void prossesFile(Request request, BufferedOutputStream out) throws IOException {
        final var filePath = Path.of(".", "public", request.getPath());
        final var mimeType = Files.probeContentType(filePath);

        // special case for classic

        final var length = Files.size(filePath);
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }
}