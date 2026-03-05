package bank;

public interface InterestStrategy {
    double calculate(double balance);

    // Helpful for console output (which strategy is active)
    String name();
}
