import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    final static int PORT = 9999;

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.setup(PORT);
    }
}