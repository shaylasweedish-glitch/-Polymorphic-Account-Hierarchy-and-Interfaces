package bank;

public class TieredInterest implements InterestStrategy {
    private final double lowRate;
    private final double highRate;
    private final double threshold;

    public TieredInterest(double lowRate, double highRate, double threshold) {
        if (lowRate < 0 || highRate < 0) throw new IllegalArgumentException("Rates must be >= 0");
        if (threshold < 0) throw new IllegalArgumentException("Threshold must be >= 0");
        this.lowRate = lowRate;
        this.highRate = highRate;
        this.threshold = threshold;
    }

    @Override
    public double calculate(double balance) {
        // Example: balance up to threshold earns lowRate; remainder earns highRate
        double lowPart = Math.min(balance, threshold) * lowRate;
        double highPart = Math.max(0, balance - threshold) * highRate;
        return lowPart + highPart;
    }

    @Override
    public String name() {
        return "TieredInterest(low=" + (lowRate * 100) + "%, high=" + (highRate * 100) + "%, threshold=" + threshold + ")";
    }
}
