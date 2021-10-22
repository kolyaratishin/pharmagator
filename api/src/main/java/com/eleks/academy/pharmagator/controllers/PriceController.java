package com.eleks.academy.pharmagator.controllers;


import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.projections.PriceLight;
import com.eleks.academy.pharmagator.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public List<PriceLight> getAll() {

        return this.priceService.findAll();

    }

    @GetMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<PriceLight> getById(
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {

        return this.priceService.findById(pharmacyId, medicineId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<PriceLight> update(
            @Valid @RequestBody PriceDto priceDto,
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {

        return this.priceService.update(pharmacyId, medicineId, priceDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{pharmacyId}/{medicineId}")
    public ResponseEntity<?> delete(
            @PathVariable("pharmacyId") Long pharmacyId,
            @PathVariable("medicineId") Long medicineId) {

        this.priceService.deleteById(pharmacyId, medicineId);

        return ResponseEntity.noContent().build();

    }

}
