package bank;

public class SavingsAccount extends Account {
    private final int monthlyWithdrawalLimit;
    private int withdrawalsThisMonth = 0;

    public SavingsAccount(String owner, double startingBalance, InterestStrategy interestStrategy, int monthlyWithdrawalLimit) {
        super(owner, startingBalance, interestStrategy);
        if (monthlyWithdrawalLimit < 0) throw new IllegalArgumentException("Limit must be >= 0");
        this.monthlyWithdrawalLimit = monthlyWithdrawalLimit;
    }

    @Override
    protected boolean canWithdraw(double amountToDeduct) {
        if (withdrawalsThisMonth >= monthlyWithdrawalLimit) {
            return false;
        }
        return super.canWithdraw(amountToDeduct);
    }

    @Override
    protected void onWithdraw(double amount) {
        withdrawalsThisMonth++;
        System.out.printf("%s: Savings withdrawal #%d this month: %.2f. Balance: %.2f%n",
                getOwner(), withdrawalsThisMonth, amount, getBalance());
    }

    public void resetMonthlyWithdrawals() {
        withdrawalsThisMonth = 0;
    }
}
