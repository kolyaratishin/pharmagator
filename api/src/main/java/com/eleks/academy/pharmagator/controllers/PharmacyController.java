package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.projections.PharmacyLight;
import com.eleks.academy.pharmagator.services.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public List<PharmacyLight> getAll() {

        return this.pharmacyService.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyLight> getById(@PathVariable("id") Long id) {

        return this.pharmacyService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<PharmacyLight> create(@Valid @RequestBody PharmacyDto pharmacyDto) {

        return ResponseEntity.ok(this.pharmacyService.save(pharmacyDto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacyLight> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PharmacyDto pharmacyDto) {

        return this.pharmacyService.update(id, pharmacyDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        this.pharmacyService.deleteById(id);

        return ResponseEntity.noContent().build();

    }

}
