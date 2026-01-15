package com.example.sysdesignola3.app;

import com.example.sysdesignola3.domain.BillingResult;
import com.example.sysdesignola3.domain.Money;
import com.example.sysdesignola3.domain.Plan;
import com.example.sysdesignola3.domain.PricingService;

/**
 * Minimal CLI entry point (kept intentionally simple).
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        PricingService pricing = new PricingService();

        BillingResult result = pricing.quote(
                Plan.PREMIUM,
                3,
                false,
                "STUDENT10"
        );

        System.out.println("Plan: " + result.plan());
        System.out.println("Subtotal: " + result.subtotal());
        System.out.println("Discount: " + result.discount());
        System.out.println("VAT: " + result.vat());
        System.out.println("Total: " + result.total());

        if (result.total().isLessThan(Money.ofDkk(0))) {
            throw new IllegalStateException("Total cannot be negative");
        }
    }
}
