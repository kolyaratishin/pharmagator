package com.eleks.academy.pharmagator.projections;

import java.math.BigDecimal;

public interface PriceLight {

    Long getPharmacyId();

    Long getMedicineId();

    BigDecimal getPrice();

    String getExternalId();

}
