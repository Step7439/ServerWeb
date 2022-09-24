import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        ExecutorService executorService = Executors.newFixedThreadPool(64);

        executorService.execute(server);
    }
}