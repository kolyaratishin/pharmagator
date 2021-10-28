package com.eleks.academy.pharmagator.scheduler;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class Scheduler {

    private final List<DataProvider> dataProviderList;

    private final MedicineRepository medicineRepository;

    private final ModelMapper modelMapper;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void schedule() {

        log.info("Scheduler started at {}", Instant.now());

        dataProviderList.stream().flatMap(DataProvider::loadData).forEach(this::storeToDatabase);

    }

    private void storeToDatabase(MedicineDto dto) {

        log.info(dto.getTitle() + " - " + dto.getPrice());

    }

}
