package com.fernandoruiz.app.management.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MonetaryUtils {
    private static final int SCALE = 2;

    private MonetaryUtils() {
    }

    public static BigDecimal safe(BigDecimal value) {
        return value != null ? scale(value) : BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal scale(BigDecimal value) {
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal value, int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("No es posible dividir entre cero");
        }
        return value.divide(BigDecimal.valueOf(divisor), SCALE, RoundingMode.HALF_UP);
    }
}
