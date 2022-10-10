import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Main {
    final static int PORT = 9999;

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
            }
        });

        server.setup(PORT);
    }
}