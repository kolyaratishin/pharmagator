package com.eleks.academy.pharmagator.services.impl;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.entities.PriceId;
import com.eleks.academy.pharmagator.projections.PriceLight;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private final MedicineRepository medicineRepository;

    private final PharmacyRepository pharmacyRepository;

    private final ProjectionFactory projectionFactory;

    @Override
    public List<PriceLight> findAll() {
        return priceRepository.findAll().stream()
                .map(entity -> projectionFactory.createProjection(PriceLight.class, entity))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<PriceLight> findById(Long pharmacyId, Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);

        return this.priceRepository.findById(priceId)
                .map(entity -> projectionFactory.createProjection(PriceLight.class, entity));

    }

    @Override
    public Optional<PriceLight> update(Long pharmacyId, Long medicineId, PriceDto priceDto) {

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
                .map(source -> projectionFactory
                        .createProjection(PriceLight.class, priceRepository.save(source)));

    }

    @Override
    public void deleteById(Long pharmacyId, Long medicineId) {

        PriceId priceId = new PriceId(pharmacyId, medicineId);
        priceRepository.deleteById(priceId);

    }

}
