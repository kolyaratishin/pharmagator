package com.eleks.academy.pharmagator.dataproviders;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.dataproviders.dto.anc.ANCMedicineDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.stream.Stream;

public class PharmacyANCDataProvider implements DataProvider {

    //private final WebClient ancClient;

    @Value("${pharmagator.data-providers.apteka-anc.category-fetch-url}")
    private String categoriesFetchUrl;

    @Override
    public Stream<MedicineDto> loadData() {
        return null;
    }

    private MedicineDto mapToMedicineDto(ANCMedicineDto ancMedicineDTO) {
        return MedicineDto.builder()
                .externalId(ancMedicineDTO.getId())
                .price(ancMedicineDTO.getPrice())
                .title(ancMedicineDTO.getName())
                .build();
    }
}
