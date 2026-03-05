package bank;

public class SimpleInterest implements InterestStrategy {
    private final double rate; // e.g., 0.02 for 2%

    public SimpleInterest(double rate) {
        if (rate < 0) throw new IllegalArgumentException("Rate must be >= 0");
        this.rate = rate;
    }

    @Override
    public double calculate(double balance) {
        return balance * rate;
    }

    @Override
    public String name() {
        return "SimpleInterest(" + (rate * 100) + "%)";
    }
}
