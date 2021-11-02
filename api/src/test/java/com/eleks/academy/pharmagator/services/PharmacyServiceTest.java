package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.controllers.dto.PharmacyDto;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.PharmacyLight;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.services.impl.PharmacyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PharmacyServiceTest {

    private PharmacyServiceImpl pharmacyService;

    @Mock
    private PharmacyRepository pharmacyRepository;

    private ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    private ModelMapper modelMapper = new ModelMapper();

    private PharmacyDto pharmacyDto;

    private List<Pharmacy> pharmacies;

    @Test
    public void contextLoads() {
    }

    @BeforeEach
    void setUp() {

        pharmacyService = new PharmacyServiceImpl(pharmacyRepository, modelMapper, projectionFactory);

        pharmacyDto = new PharmacyDto("ANC", "Link");

        pharmacies = Arrays.asList(
                new Pharmacy(1L, "ANC", "Link"),
                new Pharmacy(2L, "3I", "Link"),
                new Pharmacy(3L, "Liki24", "Link")
        );

    }

    @Test
    public void getAllUsers_ok() {

        when(pharmacyRepository.findAll()).thenReturn(pharmacies);

        List<PharmacyLight> expectedPharmacies = pharmacyService.findAll();

        assertEquals(
                expectedPharmacies.stream()
                        .map(PharmacyLight::getId)
                        .collect(Collectors.toList()),
                pharmacies.stream()
                        .map(Pharmacy::getId)
                        .collect(Collectors.toList())
        );

        verify(pharmacyRepository).findAll();

    }

    @Test
    public void savePharmacy_isOk() {

        Pharmacy pharmacy = new Pharmacy(1L, pharmacyDto.getName(), pharmacyDto.getMedicineLinkTemplate());

        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);

        PharmacyLight savedPharmacy = pharmacyService.save(pharmacyDto);

        assertThat(savedPharmacy.getName()).isSameAs(pharmacyDto.getName());

        verify(pharmacyRepository).save(any(Pharmacy.class));

    }

    @Test
    public void deletePharmacy_isOk() {

        pharmacyService.deleteById(1L);

        verify(pharmacyRepository).deleteById(1L);

    }


    @Test
    public void updatePharmacy_isOk() {

        Pharmacy pharmacyFromDB = new Pharmacy(1L, "Some name", "Some link");
        Pharmacy savedPharmacy = new Pharmacy(1L, pharmacyDto.getName(), pharmacyDto.getMedicineLinkTemplate());

        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacyFromDB));

        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(savedPharmacy);

        Optional<PharmacyLight> updatedPharmacy = pharmacyService.update(1L, pharmacyDto);

        assertThat(updatedPharmacy.get().getName()).isSameAs(pharmacyDto.getName());

        verify(pharmacyRepository).findById(1L);
        verify(pharmacyRepository).save(any(Pharmacy.class));

    }

    @Test
    public void updatePharmacyThatDoesNotExist() {

        Optional<PharmacyLight> emptyPharmacyLight = Optional.empty();

        when(pharmacyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<PharmacyLight> updatedPharmacy = pharmacyService.update(1L, pharmacyDto);

        assertThat(updatedPharmacy).isSameAs(emptyPharmacyLight);

        verify(pharmacyRepository, times(0)).save(any(Pharmacy.class));
        verify(pharmacyRepository).findById(anyLong());

    }

    @Test
    public void findPharmacyById_isOk() {

        Long id = 1L;
        Pharmacy pharmacyFromDB = new Pharmacy(id, pharmacyDto.getName(), pharmacyDto.getMedicineLinkTemplate());

        when(pharmacyRepository.findById(id)).thenReturn(Optional.of(pharmacyFromDB));

        Optional<PharmacyLight> foundPharmacy = pharmacyService.findById(id);

        assertThat(foundPharmacy.get().getId()).isSameAs(pharmacyFromDB.getId());

        verify(pharmacyRepository).findById(id);

    }

    @Test
    public void findPharmacyById_isNotFound() {

        Optional<PharmacyLight> emptyPharmacyLight = Optional.empty();

        when(pharmacyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<PharmacyLight> updatedPharmacy = pharmacyService.update(1L, pharmacyDto);

        assertThat(updatedPharmacy).isSameAs(emptyPharmacyLight);

        verify(pharmacyRepository).findById(anyLong());

    }

}
