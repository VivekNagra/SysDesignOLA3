package com.example.sysdesignola3.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class PricingServiceTest {

    private final PricingService pricing = new PricingService();

    @Test
    void monthlyWithoutPromo_hasVatAndCorrectTotal() {
        BillingResult r = pricing.quote(Plan.BASIC, 2, false, null);

        assertEquals(Plan.BASIC, r.plan());
        assertEquals(Money.ofDkk(158.00), r.subtotal());
        assertEquals(Money.ofDkk(0.00), r.discount());
        assertEquals(Money.ofDkk(39.50), r.vat());
        assertEquals(Money.ofDkk(197.50), r.total());
    }

    @Test
    void studentPromo_appliesTenPercentBeforeVat() {
        BillingResult r = pricing.quote(Plan.PREMIUM, 1, false, "STUDENT10");

        assertEquals(Money.ofDkk(149.00), r.subtotal());
        assertEquals(Money.ofDkk(14.90), r.discount());
        assertEquals(Money.ofDkk(33.53), r.vat());
        assertEquals(Money.ofDkk(167.63), r.total());
    }

    @Test
    void annualPaidUpfront_uses12MonthsAndAnnualDiscount() {
        BillingResult r = pricing.quote(Plan.STANDARD, 3, true, null);

        assertEquals(Money.ofDkk(1308.00), r.subtotal());
        assertEquals(Money.ofDkk(196.20), r.discount());
        assertEquals(Money.ofDkk(277.95), r.vat());
        assertEquals(Money.ofDkk(1389.75), r.total());
    }

    @Test
    void promoCodeBlank_isTreatedAsNoPromo() {
        BillingResult r = pricing.quote(Plan.BASIC, 1, false, "   ");

        assertEquals(Money.ofDkk(79.00), r.subtotal());
        assertEquals(Money.ofDkk(0.00), r.discount());
        assertEquals(Money.ofDkk(19.75), r.vat());
        assertEquals(Money.ofDkk(98.75), r.total());
    }

    @Test
    void unknownPromoCode_givesNoDiscount() {
        BillingResult r = pricing.quote(Plan.STANDARD, 1, false, "NOT_A_REAL_CODE");

        assertEquals(Money.ofDkk(109.00), r.subtotal());
        assertEquals(Money.ofDkk(0.00), r.discount());
        assertEquals(Money.ofDkk(27.25), r.vat());
        assertEquals(Money.ofDkk(136.25), r.total());
    }

    @Test
    void halfMonthPromo_isAppliedAndStillNonNegative() {
        BillingResult r = pricing.quote(Plan.BASIC, 1, false, "HALFMONTH");

        assertEquals(Money.ofDkk(79.00), r.subtotal());
        assertEquals(Money.ofDkk(39.50), r.discount());
        assertEquals(Money.ofDkk(9.88), r.vat());     // (79-39.50)*0.25 = 9.875 -> 9.88
        assertEquals(Money.ofDkk(49.38), r.total());   // 39.50 + 9.88 = 49.38
    }

    @Test
    void monthsOutsideRange_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> pricing.quote(Plan.BASIC, 0, false, null));
        assertThrows(IllegalArgumentException.class,
                () -> pricing.quote(Plan.BASIC, 25, false, null));
    }
}
