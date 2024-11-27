import java.util.Random;

public class ATMSimulatorThread {
    public static void main(String[] args) {
        Account account = new Account(1000);

        for (int i = 0; i < 500; i++) {
            new Thread(new Client(account, new Random().nextInt(0, 1000))).start();
        }
    }
}