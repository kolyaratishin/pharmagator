package com.eleks.academy.pharmagator.services.projection;

import java.math.BigDecimal;

public interface PriceLight {

    Long getPharmacyId();

    Long getMedicineId();

    BigDecimal getPrice();

    String getExternalId();

}
