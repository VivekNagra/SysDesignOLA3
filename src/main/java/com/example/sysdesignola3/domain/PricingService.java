package com.example.sysdesignola3.domain;

import java.util.Locale;

import java.util.List;


/**
 * Business logic:
 * - Subscriptions billed monthly or annual upfront
 * - Promo codes (STUDENT10, HALFMONTH)
 * - VAT (25%)
 * - Guardrails: months must be 1..24; totals cannot be negative
 */
public final class PricingService {

    private static final double VAT_RATE = 0.25;

    /**
     * Quotes a subscription price.
     *
     * @param plan selected plan
     * @param months number of months (1..24)
     * @param annualPaidUpfront if true, treat as 12 months upfront with a 15% annual discount
     * @param promoCode optional promo code
     * @return billing result including subtotal/discount/vat/total
     */
    public BillingResult quote(Plan plan, int months, boolean annualPaidUpfront, String promoCode) {
        validateMonths(months);

        int billedMonths = annualPaidUpfront ? 12 : months;
        Money subtotal = plan.monthlyPrice().multiply(billedMonths);

        Money discount = Money.ofDkk(0);

        if (annualPaidUpfront) {
            discount = discount.plus(subtotal.multiply(0.15));
        }

        discount = discount.plus(promoDiscount(subtotal, promoCode));

        discount = discount.max(Money.ofDkk(0));
        if (subtotal.isLessThan(discount)) {
            discount = subtotal;
        }

        Money afterDiscount = subtotal.minus(discount);
        Money vat = afterDiscount.multiply(VAT_RATE);
        Money total = afterDiscount.plus(vat);

        if (total.isLessThan(Money.ofDkk(0))) {
            throw new IllegalStateException("Total cannot be negative");
        }

        return new BillingResult(plan, subtotal, discount, vat, total);
    }

    private static void validateMonths(int months) {
        if (months < 1 || months > 24) {
            throw new IllegalArgumentException("months must be between 1 and 24");
        }
    }

    private static Money promoDiscount(Money subtotal, String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return Money.ofDkk(0);
        }

        String code = promoCode.trim().toUpperCase(Locale.ROOT);

        if ("STUDENT10".equals(code)) {
            return subtotal.multiply(0.10);
        }

        if ("HALFMONTH".equals(code)) {
            return Money.ofDkk(39.50);
        }

        return Money.ofDkk(0);
    }
}
