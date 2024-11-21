import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ATMSimulatorThreadPoolExecutor {
    public static void main(String[] args) {
        Account account = new Account(1000);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

        executor.submit(new Client(account, 200));
        executor.submit(new Client(account, -500));
        executor.submit(new Client(account, -300));
        executor.submit(new Client(account, 700));

        executor.shutdown();
    }
}