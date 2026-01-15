package com.example.sysdesignola3;

import com.example.sysdesignola3.domain.Money;
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
}
