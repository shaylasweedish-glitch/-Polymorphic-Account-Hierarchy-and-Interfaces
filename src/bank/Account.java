package bank;

public abstract class Account {
    private final String owner;
    protected double balance; // protected so subclasses can enforce their rules safely

    private InterestStrategy interestStrategy;

    public Account(String owner, double startingBalance, InterestStrategy interestStrategy) {
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("Owner required");
        if (startingBalance < 0) throw new IllegalArgumentException("Starting balance must be >= 0");
        this.owner = owner;
        this.balance = startingBalance;
        this.interestStrategy = interestStrategy;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    // --- Strategy composition ---
    public void setInterestStrategy(InterestStrategy s) {
        this.interestStrategy = s;
    }

    public InterestStrategy getInterestStrategy() {
        return interestStrategy;
    }

    public void applyInterest() {
        if (interestStrategy == null) {
            System.out.println(owner + ": No interest strategy set; skipping interest.");
            return;
        }
        double interest = interestStrategy.calculate(balance);
        balance += interest;
        System.out.printf("%s: Applied interest using %s -> +%.2f%n", owner, interestStrategy.name(), interest);
    }

    // --- Polymorphic shared operations ---
    public final void deposit(double amount) {
        validatePositive(amount);
        balance += amount;
        onDeposit(amount); // hook (subclasses can react/log/adjust)
    }

    public final boolean withdraw(double amount) {
        validatePositive(amount);

        double allowedAmount = adjustWithdrawalAmount(amount); // hook: fees, etc.
        if (!canWithdraw(allowedAmount)) {                    // hook: overdraft rules, limits
            onWithdrawalDenied(allowedAmount);                // hook
            return false;
        }

        balance -= allowedAmount;
        onWithdraw(allowedAmount); // hook
        return true;
    }

    // --- Hook methods (vary by subclass) ---
    protected void onDeposit(double amount) {
        // default: do nothing
    }

    protected void onWithdraw(double amount) {
        // default: do nothing
    }

    protected void onWithdrawalDenied(double amount) {
        System.out.printf("%s: Withdrawal denied (attempted %.2f). Balance: %.2f%n", owner, amount, balance);
    }

    /**
     * Subclasses can add fees by returning a larger amount (e.g., amount + fee).
     * Default: no adjustment.
     */
    protected double adjustWithdrawalAmount(double requestedAmount) {
        return requestedAmount;
    }

    /**
     * Subclasses can impose rules (e.g., no overdraft, limited withdrawals).
     * Default: must have enough balance.
     */
    protected boolean canWithdraw(double amountToDeduct) {
        return balance >= amountToDeduct;
    }

    private void validatePositive(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
    }

    @Override
    public String toString() {
        String strat = (interestStrategy == null) ? "None" : interestStrategy.name();
        return getClass().getSimpleName() + "{owner='" + owner + "', balance=" + String.format("%.2f", balance) +
                ", interestStrategy=" + strat + "}";
    }
}
