package com.eleks.academy.pharmagator.sevices.impl;

import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.services.impl.MedicineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicineServiceImplTest {

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineServiceImpl medicineService;

    @Mock
    private ModelMapper modelMapper;

    private Medicine medicine;

    private MedicineDto medicineDto;

    @BeforeEach
    void setUp() {
        this.medicine = new Medicine();
        medicine.setTitle("Title");
        medicine.setId(1L);
        this.medicineDto = new MedicineDto();
        medicineDto.setTitle("Title");
    }

    @Test
    void findById() {

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        medicineService.findById(1L);
        assertEquals(medicineService.findById(1L), Optional.of(medicine));
    }

    @Test
    void findAllMedicines() {

        List<Medicine> medicines = List.of(new Medicine(), new Medicine());
        when(medicineRepository.findAll()).thenReturn(medicines);
        medicineService.findAll();
        assertEquals(medicineService.findAll().size(), medicines.size());
    }

    @Test
    void saveMedicine() {

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
        when(modelMapper.map(any(), any())).thenReturn(medicine);
        Medicine created = medicineService.save(medicineDto);
        assertThat(created.getTitle()).isSameAs(medicineDto.getTitle());
    }

    @Test
    void updateMedicine() {

        MedicineDto medicineDto = modelMapper.map(medicine, MedicineDto.class);
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(modelMapper.map(any(), any())).thenReturn(medicine);
        medicineService.update(1L, medicineDto);
        verify(medicineRepository).findById(1L);
        verify(medicineRepository).save(medicine);
    }

    @Test
    void deleteMedicine() {
        medicineService.deleteById(1L);
        verify(medicineRepository).deleteById(1L);
    }

}
