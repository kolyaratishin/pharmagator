package com.eleks.academy.pharmagator.services;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.exceptions.UploadExceptions;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

    private final CsvParser parser;

    private final BeanListProcessor<MedicineDto> rowProcessor;

    private final ModelMapper modelMapper;

    private final PriceRepository priceRepository;

    private final PharmacyRepository pharmacyRepository;

    private final MedicineRepository medicineRepository;

    private final static String TYPE = "text/csv";

    @Override
    public String parseAndSave(MultipartFile file) {
        if (Objects.equals(file.getContentType(), TYPE)) {
            try {
                InputStream inputStream = file.getInputStream();
                parser.parse(inputStream);
                rowProcessor.getBeans().forEach(this::storeToDatabase);
                return "Save of file " + file.getOriginalFilename() + " was successful";
            } catch (IOException e) {
                throw new UploadExceptions(UploadExceptions.Error.SAVE_WAS_NOT_SUCCESSFUL, e.getMessage());
            }
        }
        throw new UploadExceptions(UploadExceptions.Error.INVALID_FILE_FORMAT);
    }

    private void storeToDatabase(MedicineDto dto) {
        Medicine medicine = modelMapper.map(dto, Medicine.class);
        Price price = modelMapper.map(dto, Price.class);

        String pharmacyName = dto.getPharmacyName();
        Pharmacy pharmacyFromDb = pharmacyRepository.findByName(pharmacyName).orElseGet(() -> {
            Pharmacy pharmacy = new Pharmacy();
            pharmacy.setName(pharmacyName);
            return pharmacyRepository.save(pharmacy);
        });

        Medicine medicineFromDb = medicineRepository.findByTitle(medicine.getTitle()).orElseGet(() -> medicineRepository.save(medicine));

        price.setMedicineId(medicineFromDb.getId());
        price.setPharmacyId(pharmacyFromDb.getId());
        price.setUpdatedAt(Instant.now());
        price.setExternalId(dto.getExternalId());
        priceRepository.save(price);
    }
}
