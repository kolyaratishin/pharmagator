package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final List<DataProvider> dataProviders;
    MedicineRepository medicineRepository;
    private final ModelMapper modelMapper;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void schedule() {
        log.info("Scheduler started at {}", Instant.now());
        dataProviders.stream().flatMap(DataProvider::loadData).forEach(this::storeToDatabase);
    }

    private void storeToDatabase(MedicineDto dto) {
        log.info(dto.getTitle() + " - " + dto.getPrice());
        log.info(dto.toString());
    //    medicineRepository.save(modelMapper.map(dto,Medicine.class));

    }
}
