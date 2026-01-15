package com.example.sysdesignola3.domain;

/**
 * Immutable output of PricingService.
 */
public record BillingResult(
        Plan plan,
        Money subtotal,
        Money discount,
        Money vat,
        Money total
) { }
