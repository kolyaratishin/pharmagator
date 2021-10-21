package com.eleks.academy.pharmagator.dataproviders;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCCategoryDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicinesResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCSubcategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Qualifier("pharmacyANCDataProvider")
public class PharmacyANCDataProvider implements DataProvider {

    private final WebClient ancClient;

    @Value("${pharmagator.data-providers.pharmacy-anc.category-fetch-url}")
    private String categoriesFetchUrl;

    @Override
    public Stream<MedicineDto> loadData() {

        return this.fetchCategories().stream()
                .filter(categories -> categories.getName().equals("Медикаменти"))
                .map(ANCSubcategoryDto::getSubcategories)
                .flatMap(Collection::stream)
                .map(ANCSubcategoryDto::getLink)
                .flatMap(this::fetchProductsByCategory);

    }

    private List<ANCSubcategoryDto> fetchCategories() {

        return this.ancClient.get().uri(categoriesFetchUrl)
                .retrieve().bodyToMono(new ParameterizedTypeReference<ANCCategoryDto>() {
                }).map(ANCCategoryDto::getCategories).block();

    }

    private Stream<MedicineDto> fetchProductsByCategory(String category) {

        ANCMedicinesResponse ancMedicinesResponse = this.ancClient.get()
                .uri(categoriesFetchUrl + "/" + category)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ANCMedicinesResponse>() {
                })
                .block();

        return ancMedicinesResponse.getProducts().stream().map(this::mapToMedicineDto);

    }

    private MedicineDto mapToMedicineDto(ANCMedicineDto ancMedicineDTO) {

        return MedicineDto.builder()
                .externalId(ancMedicineDTO.getId())
                .price(ancMedicineDTO.getPrice())
                .title(ancMedicineDTO.getName())
                .build();
    }

}
