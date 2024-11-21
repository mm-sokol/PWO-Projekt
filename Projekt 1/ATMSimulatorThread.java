public class ATMSimulatorThread {
    public static void main(String[] args) {
        Account account = new Account(1000);

        Thread client1 = new Thread(new Client(account, 200));
        Thread client2 = new Thread(new Client(account,  -500));
        Thread client3 = new Thread(new Client(account,  -300));
        Thread client4 = new Thread(new Client(account, 700));

        client1.start();
        client2.start();
        client3.start();
        client4.start();
    }
}