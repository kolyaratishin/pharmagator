package com.eleks.academy.pharmagator.services;

import org.springframework.web.multipart.MultipartFile;

public interface CsvService {

    String parseAndSave(MultipartFile file);

}
