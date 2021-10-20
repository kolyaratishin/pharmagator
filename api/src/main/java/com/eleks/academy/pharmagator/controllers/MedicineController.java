package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public List<Medicine> getAll() {
        return this.medicineService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable("id") Long id) {

        return this.medicineService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@Valid @RequestBody MedicineDto medicineDto) {

        return ResponseEntity.ok(this.medicineService.save(medicineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(
            @Valid @RequestBody MedicineDto medicineDto,
            @PathVariable("id") Long id) {

        return this.medicineService.update(medicineDto, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.medicineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
