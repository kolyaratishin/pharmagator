package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.exceptions.UploadExceptions;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

    private final CsvParser parser;

    private final BeanListProcessor<MedicineDto> rowProcessor;

    private final ImportService importService;

    private static final String TYPE = "text/csv";

    @Override
    public String parseAndSave(MultipartFile file) {
        if (Objects.equals(file.getContentType(), TYPE)) {
            try {
                InputStream inputStream = file.getInputStream();
                parser.parse(inputStream);
                rowProcessor.getBeans().forEach(importService::storeToDatabase);
                return "Save of file " + file.getOriginalFilename() + " was successful";
            } catch (IOException e) {
                throw new UploadExceptions(UploadExceptions.Error.SAVE_WAS_NOT_SUCCESSFUL);
            }
        }
        throw new UploadExceptions(UploadExceptions.Error.INVALID_FILE_FORMAT);
    }

}
