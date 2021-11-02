package com.eleks.academy.pharmagator.controllers;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicineControllerIT {

    private MockMvc mockMvc;

    private DatabaseDataSourceConnection dataSourceConnection;

    private final String URI_MEDICINES = "/medicines";

    private final String CONTENT = "{\"title\": \"title1\"}";

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @BeforeEach
    void setUp() throws DatabaseUnitException, IOException, SQLException {
        DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());
    }

    @Test
    public void findAllMedicines_findIds_ok() throws Exception {
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(URI_MEDICINES))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].id",
                            Matchers.hasItems(2021102701, 2021102702)));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findMedicinesById_ok() throws Exception {
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(URI_MEDICINES + "/2021102701"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("title",
                            Matchers.equalToIgnoringCase("MedicineControllerIT_name1")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findMedicinesById_notExisted_notFound() throws Exception {
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(URI_MEDICINES + "/20211027011"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void saveMedicine_isOk() throws Exception {
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post(URI_MEDICINES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CONTENT))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("title",
                            Matchers.equalToIgnoringCase("title1")));
        } finally {
            this.dataSourceConnection.close();
        }

    }

    @Test
    void updateMedicineById_isOk() throws Exception {

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.put(URI_MEDICINES + "/2021102701")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CONTENT))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("title",
                            Matchers.equalToIgnoringCase("title1")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    void deleteById() throws Exception {
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.delete(URI_MEDICINES + "/2021102701"))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
        } finally {
            this.dataSourceConnection.close();
        }
    }


    private IDataSet readDataset() throws DataSetException, IOException {
        try (var resource = getClass()
                .getResourceAsStream("MedicineControllerIT_dataset.xml")) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }

}
