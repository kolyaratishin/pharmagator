package com.eleks.academy.pharmagator.controllers;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PharmacyControllerIT {

    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {

        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);

    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void findAllPharmacies_findIds_ok() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get("/pharmacies"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].id",
                            Matchers.hasItems(2021102101, 2021102102)));

        } finally {

            this.dataSourceConnection.close();

        }

    }

    @Test
    public void savePharmacy_isOk() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/pharmacies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"ANC\", \"medicineLinkTemplate\":\"Link\"}")
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name", is("ANC")));

        } finally {

            this.dataSourceConnection.close();

        }


    }

    @Test
    public void savePharmacy_notValid_nameIsBlank() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/pharmacies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"id\": \"1\",\"name\": \"\", \"medicineLinkTemplate\":\"Link\"}")
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        } finally {

            this.dataSourceConnection.close();

        }


    }

    @Test
    public void updatePharmacy_isOk() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/pharmacies" + "/2021102101")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"ANC\", \"medicineLinkTemplate\":\"Link\"}")
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name", is("ANC")))
                    .andExpect(jsonPath("$.id", is(2021102101)));

        } finally {

            this.dataSourceConnection.close();

        }

    }

    @Test
    public void updatePharmacy_notFound() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/pharmacies" + "/2021102103")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"ANC\", \"medicineLinkTemplate\":\"Link\"}")
                    )
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

        } finally {

            this.dataSourceConnection.close();

        }

    }

    @Test
    public void deletePharmacy_isOk() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.delete("/pharmacies" + "/2021102101"))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        } finally {

            this.dataSourceConnection.close();

        }

    }

    @Test
    public void findPharmacyById_isOk() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get("/pharmacies" + "/2021102101"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name", is("PharmacyControllerIT_name1")))
                    .andExpect(jsonPath("$.id", is(2021102101)));

        } finally {

            this.dataSourceConnection.close();

        }

    }

    @Test
    public void findPharmacyById_isNotFound() throws Exception {

        try {

            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get("/pharmacies" + "/2021102103"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

        } finally {

            this.dataSourceConnection.close();

        }

    }

    private IDataSet readDataset() throws DataSetException, IOException {

        try (var resource = getClass()
                .getResourceAsStream("PharmacyControllerIT_dataset.xml")) {

            return new FlatXmlDataSetBuilder()
                    .build(resource);

        }

    }

}
