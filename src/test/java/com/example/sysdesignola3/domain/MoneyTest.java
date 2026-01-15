package com.example.sysdesignola3.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

final class MoneyTest {

    @Test
    void roundingIsStable_halfUpToTwoDecimals() {
        Money m = Money.ofDkk(33.525);
        assertEquals(Money.ofDkk(33.53), m);
    }

    @Test
    void plusMinusWork() {
        Money a = Money.ofDkk(10);
        Money b = Money.ofDkk(3.5);

        assertEquals(Money.ofDkk(13.50), a.plus(b));
        assertEquals(Money.ofDkk(6.50), a.minus(b));
    }

    @Test
    void max_returnsLargerValue_inBothOrders() {
        Money a = Money.ofDkk(10);
        Money b = Money.ofDkk(12);

        assertEquals(b, a.max(b));
        assertEquals(b, b.max(a));
    }

    @Test
    void ofBigDecimal_setsScaleToTwoDecimals() {
        Money m = Money.of(new BigDecimal("1.2"));
        assertEquals(Money.ofDkk(1.20), m);
    }

    @Test
    void isLessThan_works() {
        Money a = Money.ofDkk(10);
        Money b = Money.ofDkk(20);
        assertTrue(a.isLessThan(b));
        assertFalse(b.isLessThan(a));
        assertFalse(a.isLessThan(a));
    }

    @Test
    void multiply_works() {
        Money m = Money.ofDkk(10);
        assertEquals(Money.ofDkk(25), m.multiply(2.5));
    }

    @Test
    void asBigDecimal_returnsCorrectValue() {
        assertEquals(0, new BigDecimal("10.00").compareTo(Money.ofDkk(10).asBigDecimal()));
    }

    @Test
    void equals_handlesNullAndOtherTypes() {
        Money m = Money.ofDkk(10);
        assertEquals(m, m);
        assertNotEquals(null, m);
        assertNotEquals(new Object(), m);
        assertNotEquals(Money.ofDkk(11), m);
    }

    @Test
    void verifyHashCode() {
        Money a = Money.ofDkk(10);
        Money b = Money.ofDkk(10);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void verifyToString() {
        assertEquals("10.00 DKK", Money.ofDkk(10).toString());
    }
}
