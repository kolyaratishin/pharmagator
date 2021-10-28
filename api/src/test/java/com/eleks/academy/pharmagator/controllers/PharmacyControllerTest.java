package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.PharmacyLight;
import com.eleks.academy.pharmagator.services.PharmacyService;
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
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
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
public class PharmacyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PharmacyService pharmacyService;

    @InjectMocks
    private PharmacyController pharmacyController;

    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    private final ModelMapper modelMapper = new ModelMapper();

    private final String URI = "/pharmacies";

    private final String CONTENT =  "{\"name\": \"ph1\" , \"medicineLinkTemplate\": \"LinkTemplate\" }";

    private Pharmacy pharmacy;

    private final Long pharmacyId = 1L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(pharmacyController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.pharmacy = new Pharmacy();
        pharmacy.setName("ph1");
        pharmacy.setId(pharmacyId);
        pharmacy.setMedicineLinkTemplate("LinkTemplate");
    }

    @Test
    void getAllPharmacies_isOk() throws Exception {

        mockMvc.perform(get(URI)).andExpect(status().isOk());
        verify(pharmacyService).findAll();
    }
    @Test
    void savePharmacy_isOk() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        PharmacyDto pharmacyDto = mapper.readValue(CONTENT, PharmacyDto.class);
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTENT))
                .andExpect(status().isOk());
        verify(pharmacyService).save(pharmacyDto);
    }

    @Test
    void getById_noExistsId_isNotFound() throws Exception {

        String uri = URI + "/0";
        mockMvc.perform(get(uri)).andExpect(status().isNotFound());
        verify(pharmacyService).findById(0L);
    }

    @Test
    void getById_isOk() throws Exception {


        String uri = URI + "/" + pharmacyId;
        when(pharmacyService.findById(any(Long.class))).thenReturn(Optional.of(projectionFactory.createProjection(PharmacyLight.class, pharmacy)));
        mockMvc.perform(get(uri)).andExpect(status().isOk());
        verify(pharmacyService).findById(eq(pharmacyId));
    }

    @Test
    void updatePharmacyById_isOk() throws Exception {

        String uri = URI + "/" + pharmacyId;
        PharmacyDto pharmacyDto = modelMapper.map(pharmacy, PharmacyDto.class);

        when(pharmacyService.update(any(Long.class),eq(pharmacyDto))).thenReturn(Optional.of(projectionFactory.createProjection(PharmacyLight.class, pharmacy)));

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT));

        verify(pharmacyService).update(any(Long.class),eq(pharmacyDto));
    }

    @Test
    void deleteById() throws Exception {

        mockMvc.perform(delete(URI + "/"+pharmacyId)).andExpect(status().isNoContent());
        verify(pharmacyService).deleteById(pharmacyId);
    }
}
