package com.eleks.academy.pharmagator.dataproviders.dto.anc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ANCCategoryDto {

    private Integer total;

    private List<ANCSubcategoryDto> categories;
}
