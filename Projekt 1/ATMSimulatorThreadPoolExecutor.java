import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ATMSimulatorThreadPoolExecutor {
    public static void main(String[] args) throws InterruptedException {
        Account account = new Account(1000);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            tasks.add(() -> {
                new Client(account, new Random().nextInt(0, 1000)).run();
                return null;
            });
        }
        executor.invokeAll(tasks);
        executor.shutdown();
    }
}