package com.eleks.academy.pharmagator.dataproviders.impl;


import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCCategoryDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicinesResponse;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCSubcategoryDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
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
                .map(this::findAllDippestCategories)
                .flatMap(Collection::stream)
                .map(ANCSubcategoryDto::getLink)
                .flatMap(this::fetchProductsByCategory);

    }

    private List<ANCSubcategoryDto> fetchCategories() {
        return this.ancClient.get().uri(categoriesFetchUrl)
                .retrieve().bodyToMono(new ParameterizedTypeReference<ANCCategoryDto>() {
                }).map(ANCCategoryDto::getCategories).block();
    }

    private Deque<ANCSubcategoryDto> findAllDippestCategories(ANCSubcategoryDto ancSubcategoryDto) {
        Deque<ANCSubcategoryDto> allSubcategories = new LinkedList<>();

        for (ANCSubcategoryDto subcategory : ancSubcategoryDto.getSubcategories()) {
            allSubcategories.addAll(subcategory.getSubcategories());
        }

        return allSubcategories;
    }

    private Stream<MedicineDto> fetchProductsByCategory(String category){
            ANCMedicinesResponse ancMedicinesResponse = this.ancClient.get()
                    .uri(categoriesFetchUrl + "/" + category)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ANCMedicinesResponse>(){})
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
