package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.MedicineDto;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.services.MedicineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MedicineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicineService medicineService;

    @InjectMocks
    private MedicineController medicineController;

    private final ModelMapper modelMapper = new ModelMapper();

    private final String URI = "/medicines";

    private final String CONTENT =  "{\"title\": \"John\"}";
    
    private Medicine medicine;

    private final Long medicineId = 1L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(medicineController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.medicine = new Medicine();
        medicine.setTitle("John");
        medicine.setId(medicineId);
    }

    @Test
    void getAllMedicines_isOk() throws Exception {

        mockMvc.perform(get(URI)).andExpect(status().isOk());
        verify(medicineService).findAll();
    }

    @Test
    void saveMedicine_isOk() throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        MedicineDto medicineDto = mapper.readValue(CONTENT, MedicineDto.class);
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTENT))
                .andExpect(status().isOk());
        verify(medicineService).save(medicineDto);
    }

    @Test
    void getById_noExistsId_isNotFound() throws Exception {

        String uri = URI + "/0";
        mockMvc.perform(get(uri)).andExpect(status().isNotFound());
        verify(medicineService).findById(0L);
    }

    @Test
    void getById_isOk() throws Exception {


        String uri = URI + "/" + medicineId;
        when(medicineService.findById(any(Long.class))).thenReturn(Optional.of(medicine));
        mockMvc.perform(get(uri)).andExpect(status().isOk());
        verify(medicineService).findById(eq(medicineId));
    }

    @Test
    void updateMedicineById_isOk() throws Exception {

        String uri = URI + "/" + medicineId;
        MedicineDto medicineDto = modelMapper.map(medicine, MedicineDto.class);

        when(medicineService.update(any(Long.class),eq(medicineDto))).thenReturn(Optional.of(medicine));

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT));

        verify(medicineService).update(any(Long.class),eq(medicineDto));
    }

    @Test
    void deleteById() throws Exception {

        mockMvc.perform(delete(URI + "/"+medicineId)).andExpect(status().isNoContent());
        verify(medicineService).deleteById(medicineId);
    }
}
