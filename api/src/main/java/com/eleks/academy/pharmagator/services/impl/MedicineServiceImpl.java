package com.eleks.academy.pharmagator.services.impl;

import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<Medicine> findAll() {

        return medicineRepository.findAll();

    }

    @Override
    public Optional<Medicine> findById(Long id) {

        return medicineRepository.findById(id);

    }

    @Override
    public Medicine save(MedicineDto medicineDto) {

        Medicine medicine = modelMapper.map(medicineDto, Medicine.class);
        return medicineRepository.save(medicine);

    }

    @Override
    public Optional<Medicine> update(Long id,MedicineDto medicineDto) {

        return medicineRepository.findById(id)
                .map(source -> {
                    Medicine target = modelMapper.map(medicineDto, Medicine.class);
                    target.setId(source.getId());
                    medicineRepository.save(target);
                    return target;
                });

    }

    @Override
    public void deleteById(Long id) {

        medicineRepository.deleteById(id);

    }

}
