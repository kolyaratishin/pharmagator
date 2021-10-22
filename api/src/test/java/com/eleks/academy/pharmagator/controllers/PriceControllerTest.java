package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.PriceDto;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.projections.PriceLight;
import com.eleks.academy.pharmagator.services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceController priceController;

    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    private final ModelMapper modelMapper = new ModelMapper();

    private final String URI = "/prices";

    private final String CONTENT =  "{\"price\": 11 , \"externalId\": \"11\" }";

    private Price price;

    private final Long medicineId = 1L;

    private final Long pharmacyId = 1L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(priceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.price = new Price();
        price.setPrice(BigDecimal.valueOf(11));
        price.setPharmacyId(pharmacyId);
        price.setMedicineId(medicineId);
        price.setExternalId("11");

    }

    @Test
    void getAllPrices_isOk() throws Exception {

        mockMvc.perform(get(URI)).andExpect(status().isOk());
        verify(priceService).findAll();
    }

    @Test
    void getById_noExistsId_isNotFound() throws Exception {

        String uri = URI + "/" +pharmacyId+"/"+medicineId;
        mockMvc.perform(get(uri)).andExpect(status().isNotFound());
        verify(priceService).findById(eq(pharmacyId),eq(pharmacyId));
    }

    @Test
    void getById_isOk() throws Exception {

        String uri = URI + "/" + pharmacyId +"/" + medicineId ;
        when(priceService.findById(any(Long.class),any(Long.class))).thenReturn(Optional.of(projectionFactory.createProjection(PriceLight.class, price)));
        mockMvc.perform(get(uri)).andExpect(status().isOk());
        verify(priceService).findById(eq(pharmacyId),eq(medicineId));
    }
    @Test
    void updatePharmacyById_isOk() throws Exception {

        String uri = URI + "/" + pharmacyId +"/" + medicineId ;

        PriceDto priceDto = modelMapper.map(price, PriceDto.class);
        when(priceService.update( any(Long.class), any(Long.class),eq(priceDto))).thenReturn(Optional.of(projectionFactory.createProjection(PriceLight.class, price)));

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT)).andExpect(status().isOk());

        verify(priceService).update(any(Long.class), any(Long.class),eq(priceDto));
    }

    @Test
    void deleteById() throws Exception {

        mockMvc.perform(delete(URI + "/" + pharmacyId +"/" + medicineId )).andExpect(status().isNoContent());
        verify(priceService).deleteById(pharmacyId,medicineId);
    }
}
