public class Account {
    private double balance;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void modifyBalance(double amount) {
        if(balance + amount < 0) {
            System.out.println("Insufficient funds");
            return;
        }

        System.out.println("Modifying balance by: " + amount);
        this.balance += amount;
    }

    public synchronized double getBalance() {
        return balance;
    }
}