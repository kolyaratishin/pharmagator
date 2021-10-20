package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;

import java.util.List;
import java.util.Optional;

public interface PriceService {

    List<Price> findAll();

    Optional<Price> findById(Long pharmacyId, Long medicineId);

    Optional<Price> update(PriceDto priceDto, Long pharmacyId, Long medicineId);

    void deleteById(Long pharmacyId, Long medicineId);
}
