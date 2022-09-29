import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        final var server = new Server();


        server.setup(9999);
    }
}