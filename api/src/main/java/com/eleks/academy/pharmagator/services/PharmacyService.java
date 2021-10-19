package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.services.projection.PharmacyLight;

import java.util.List;
import java.util.Optional;

public interface PharmacyService {

    List<PharmacyLight> findAll();

    Optional<PharmacyLight> findById(Long id);

    PharmacyLight save(PharmacyDto pharmacyDto);

    Optional<PharmacyLight> update(PharmacyDto pharmacyDto, Long id);

    void deleteById(Long id);
}
