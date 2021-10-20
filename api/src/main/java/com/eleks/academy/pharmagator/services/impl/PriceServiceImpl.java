package com.eleks.academy.pharmagator.services.impl;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private final MedicineRepository medicineRepository;

    private final PharmacyRepository pharmacyRepository;

    @Override
    public List<Price> findAll() {
        return priceRepository.findAll();
    }

    @Override
    public Optional<Price> findById(Long pharmacyId, Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);

        return this.priceRepository.findById(priceId);
    }

    @Override
    public Optional<Price> update(PriceDto priceDto, Long pharmacyId, Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);

        Function<PriceId, Optional<Price>> create = id ->
                this.pharmacyRepository.findById(id.getPharmacyId())
                        .map(pharmacy -> Price.builder()
                                .pharmacyId(pharmacy.getId())
                        )
                        .flatMap(builder ->
                                this.medicineRepository.findById(id.getMedicineId())
                                        .map(medicine -> builder
                                                .medicineId(medicine.getId())
                                        )
                        )
                        .map(builder -> builder
                                .price(priceDto.getPrice())
                                .externalId(priceDto.getExternalId()))
                        .map(Price.PriceBuilder::build);

        return this.priceRepository.findById(priceId)
                .map(source -> {
                    source.setPrice(priceDto.getPrice());
                    source.setExternalId(priceDto.getExternalId());
                    return source;
                }).or(() -> create.apply(priceId))
                .map(this.priceRepository::save);

    }

    @Override
    public void deleteById(Long pharmacyId, Long medicineId) {
        PriceId priceId = new PriceId(pharmacyId, medicineId);
        priceRepository.deleteById(priceId);
    }
}
