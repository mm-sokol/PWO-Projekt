class Client implements Runnable {
    private final Account account;
    private final double amount;

    public Client(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.modifyBalance(this.amount);
    }
}