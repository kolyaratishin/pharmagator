package com.eleks.academy.pharmagator.dataproviders.dto.ds;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DSMedicineDto {

    private String id;
    private String name;
    private BigDecimal price;
    private String manufacturer;

}
