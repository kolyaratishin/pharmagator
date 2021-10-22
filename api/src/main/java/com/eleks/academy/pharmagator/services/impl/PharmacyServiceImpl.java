package com.eleks.academy.pharmagator.services.impl;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.services.PharmacyService;
import com.eleks.academy.pharmagator.projections.PharmacyLight;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    private final ModelMapper modelMapper;

    private final ProjectionFactory projectionFactory;

    @Override
    public List<PharmacyLight> findAll() {

        return pharmacyRepository.findAll().stream()
                .map(entity -> projectionFactory.createProjection(PharmacyLight.class, entity))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<PharmacyLight> findById(Long id) {

        return pharmacyRepository.findById(id)
                .map(entity -> projectionFactory.createProjection(PharmacyLight.class, entity));

    }

    @Override
    public PharmacyLight save(PharmacyDto pharmacyDto) {

        Pharmacy pharmacy = modelMapper.map(pharmacyDto, Pharmacy.class);

        return projectionFactory.createProjection(PharmacyLight.class, pharmacyRepository.save(pharmacy));

    }

    @Override
    public Optional<PharmacyLight> update(Long id, PharmacyDto pharmacyDto) {

        return pharmacyRepository.findById(id)
                .map(source -> {
                    Pharmacy target = modelMapper.map(pharmacyDto, Pharmacy.class);
                    target.setId(source.getId());
                    return projectionFactory
                            .createProjection(PharmacyLight.class, pharmacyRepository.save(target));
                });

    }

    @Override
    public void deleteById(Long id) {

        pharmacyRepository.deleteById(id);

    }

}
