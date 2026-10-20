package com.petly.pet.service;

import java.math.BigDecimal;

/**
 * @author farzane.rahmani
 * @created 10/18/2026
 */


public class CalorieEstimate {

    private final BigDecimal rerKcal;
    private final BigDecimal factor;
    private final BigDecimal merKcal;

    public CalorieEstimate(
            BigDecimal rerKcal,
            BigDecimal factor,
            BigDecimal merKcal
    ) {
        this.rerKcal = rerKcal;
        this.factor = factor;
        this.merKcal = merKcal;
    }

    public BigDecimal getRerKcal() {
        return rerKcal;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public BigDecimal getMerKcal() {
        return merKcal;
    }
}
