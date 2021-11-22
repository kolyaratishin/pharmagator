package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.services.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CsvService csvService;

    @PostMapping("/medicines")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        csvService.parseAndSave(file);
        return ResponseEntity.noContent().build();
    }

}
