package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.projections.PriceLight;

import java.util.List;
import java.util.Optional;

public interface PriceService {

    List<PriceLight> findAll();

    Optional<PriceLight> findById(Long pharmacyId, Long medicineId);

    Optional<PriceLight> update(Long pharmacyId, Long medicineId, PriceDto priceDto);

    void deleteById(Long pharmacyId, Long medicineId);

}
