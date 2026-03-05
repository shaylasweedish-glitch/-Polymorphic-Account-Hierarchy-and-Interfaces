package bank;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InterestStrategy simple2pct = new SimpleInterest(0.02);
        InterestStrategy tiered = new TieredInterest(0.01, 0.03, 1000);

        Account sav = new SavingsAccount("Ava", 1200.00, simple2pct, 2);
        Account chk = new CheckingAccount("Ben", 200.00, simple2pct, 1.50, 50.00);

        System.out.println("=== Initial Accounts ===");
        System.out.println(sav);
        System.out.println(chk);

        // --- Polymorphism: List<Account> with mixed subclasses ---
        List<Account> accounts = new ArrayList<>();
        accounts.add(sav);
        accounts.add(chk);

        System.out.println("\n=== Polymorphism Demo (deposit/withdraw through Account reference) ===");
        for (Account a : accounts) {
            System.out.println("\nWorking on: " + a.getOwner() + " (" + a.getClass().getSimpleName() + ")");
            a.deposit(100);
            boolean ok1 = a.withdraw(50);
            System.out.println("Withdraw 50 success? " + ok1 + " | Balance now: " + String.format("%.2f", a.getBalance()));

            boolean ok2 = a.withdraw(500);
            System.out.println("Withdraw 500 success? " + ok2 + " | Balance now: " + String.format("%.2f", a.getBalance()));
        }

        // --- Strategy usage + swapping at runtime ---
        System.out.println("\n=== Strategy Swap Demo (InterestStrategy) ===");
        System.out.println("Before interest: " + sav);
        sav.applyInterest(); // uses SimpleInterest
        System.out.println("After SimpleInterest: " + sav);

        sav.setInterestStrategy(tiered); // swap at runtime
        sav.applyInterest();             // now uses TieredInterest
        System.out.println("After TieredInterest: " + sav);

        /*
          Open/Closed Principle note:
          We can add a new strategy class (e.g., PromotionalInterest) without editing Account/SavingsAccount/CheckingAccount.
          We just create a new class implementing InterestStrategy and plug it in via setInterestStrategy(...).
        */

        System.out.println("\n=== Final Accounts ===");
        for (Account a : accounts) {
            System.out.println(a);
        }
    }
}
