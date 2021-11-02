package com.eleks.academy.pharmagator.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDto {

    @NotBlank(message = "Pharmacy name cannot be null")
    private String name;

    private String medicineLinkTemplate;

}
