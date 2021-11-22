package com.eleks.academy.pharmagator.services;


import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.exceptions.UploadExceptions;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class CsvServiceImplTest {

    private CsvServiceImpl csvService;

    @Autowired
    private ImportService importService;

    private MultipartFile file;

    private static final String TYPE = "text/csv";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        csvService = new CsvServiceImpl(csvParser(), rowProcessor(), importService);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prices", "pharmacies", "medicines");
    }

    private BeanListProcessor<MedicineDto> rowProcessor() {
        return new BeanListProcessor<>(MedicineDto.class);
    }

    private CsvParser csvParser() {
        BeanListProcessor<MedicineDto> rowProcessor = rowProcessor();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setProcessor(rowProcessor);

        return new CsvParser(settings);
    }

    @Test
    @SneakyThrows
    void dataImport_isOk() {
        InputStream inputStream = getClass().getResourceAsStream("import.csv");
        MultipartFile file = new MockMultipartFile("import.csv", "", TYPE, inputStream);

        String expectedMessage = csvService.parseAndSave(file);

        assertEquals(expectedMessage, "Save of file " + file.getOriginalFilename() + " was successful");
    }

    @Test
    @SneakyThrows
    void dataImport_isInvalidFormat() {
        InputStream inputStream = getClass().getResourceAsStream("import.csv");
        MultipartFile file = new MockMultipartFile("import.csv", "", "pdf", inputStream);

        UploadExceptions exception = assertThrows(UploadExceptions.class, () -> csvService.parseAndSave(file));
        assertEquals(UploadExceptions.Error.INVALID_FILE_FORMAT, exception.getError());
    }

}
