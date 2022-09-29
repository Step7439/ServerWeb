import com.sun.net.httpserver.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Handler;

public class Main {
    final static int port = 9999;
    public static void main(String[] args) throws IOException {
        final var server = new Server();

        server.listen(port);
    }
}