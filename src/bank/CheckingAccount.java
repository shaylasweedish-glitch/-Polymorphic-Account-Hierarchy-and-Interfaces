package bank;

public class CheckingAccount extends Account {
    private final double transactionFee;
    private final double overdraftLimit; // e.g. 50 means can go down to -50

    public CheckingAccount(String owner, double startingBalance, InterestStrategy interestStrategy,
                           double transactionFee, double overdraftLimit) {
        super(owner, startingBalance, interestStrategy);
        if (transactionFee < 0) throw new IllegalArgumentException("Fee must be >= 0");
        if (overdraftLimit < 0) throw new IllegalArgumentException("Overdraft limit must be >= 0");
        this.transactionFee = transactionFee;
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    protected double adjustWithdrawalAmount(double requestedAmount) {
        return requestedAmount + transactionFee; // fee added per withdrawal
    }

    @Override
    protected boolean canWithdraw(double amountToDeduct) {
        // can go down to -overdraftLimit
        return (balance - amountToDeduct) >= (-overdraftLimit);
    }

    @Override
    protected void onWithdraw(double amountDeducted) {
        System.out.printf("%s: Checking withdrawal (incl fee %.2f): %.2f. Balance: %.2f%n",
                getOwner(), transactionFee, amountDeducted, getBalance());
    }

    @Override
    protected void onWithdrawalDenied(double amount) {
        System.out.printf("%s: Checking withdrawal denied (attempted %.2f incl fee). Balance: %.2f, overdraftLimit: %.2f%n",
                getOwner(), amount, getBalance(), overdraftLimit);
    }
}
