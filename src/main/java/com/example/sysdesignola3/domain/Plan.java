package com.example.sysdesignola3.domain;

/**
 * Subscription plans for a fictional streaming platform.
 */
public enum Plan {
    BASIC(79.0),
    STANDARD(109.0),
    PREMIUM(149.0);

    private final double monthlyPriceDkk;

    Plan(double monthlyPriceDkk) {
        this.monthlyPriceDkk = monthlyPriceDkk;
    }

    public Money monthlyPrice() {
        return Money.ofDkk(monthlyPriceDkk);
    }
}
