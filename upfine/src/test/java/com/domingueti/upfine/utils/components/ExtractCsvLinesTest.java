package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.daos.ConfigDAO;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ExtractCsvLinesTest {

    @InjectMocks
    private ExtractCsvLines extractCsvLines;

    @Mock
    private GetConfigByNameService getConfigByNameService;

    private String zipFileUrl;
    private String zipFilePathStr;
    private String csvFilePathStr;
    private String charsetPattern;
    private String nonExistingConfigName;

    @Mock
    private ConfigDAO zipFileUrlValueDao;
    @Mock
    private ConfigDAO zipFilePathStrValueDao;
    @Mock
    private ConfigDAO csvFilePathStrValueDao;
    @Mock
    private ConfigDAO charsetPatternValueDao;

    @BeforeEach
    void setup() {
        zipFileUrl = "ZIP-FILE-URL";
        zipFilePathStr = "ZIP-FILE-PATH-STR";
        csvFilePathStr = "CSV-FILE-PATH-STR";
        charsetPattern = "CHARSET-PATTERN";
        nonExistingConfigName = zipFileUrl;
    }

    @Test
    @DisplayName("Should extract lines of the given CSV file and delete it.")
    public void shouldExtractLinesOfCsvAndDeleteIt() {

        when(getConfigByNameService.execute(zipFileUrl)).thenReturn(zipFileUrlValueDao);
        when(getConfigByNameService.execute(zipFilePathStr)).thenReturn(zipFilePathStrValueDao);
        when(getConfigByNameService.execute(csvFilePathStr)).thenReturn(csvFilePathStrValueDao);
        when(getConfigByNameService.execute(charsetPattern)).thenReturn(charsetPatternValueDao);

        when(zipFileUrlValueDao.getValue()).thenReturn("https://dados.cvm.gov.br/dados/CIA_ABERTA/DOC/IPE/DADOS/ipe_cia_aberta_2023.zip");
        when(zipFilePathStrValueDao.getValue()).thenReturn("src/main/resources/zip/ipe_cia_aberta_2023.zip");
        when(csvFilePathStrValueDao.getValue()).thenReturn("src/main/resources/csv/ipe_cia_aberta_2023.zip");
        when(charsetPatternValueDao.getValue()).thenReturn("ISO-8859-1");

        assertDoesNotThrow(() -> {
            extractCsvLines.execute();
        });
        verify(getConfigByNameService, times(1)).execute(zipFileUrl);
        verify(getConfigByNameService, times(1)).execute(zipFilePathStr);
        verify(getConfigByNameService, times(1)).execute(csvFilePathStr);
        verify(getConfigByNameService, times(1)).execute(charsetPattern);
    }

    @Test
    @DisplayName("Should throw NotFoundException if config does not exist by name.")
    public void shouldThrowNotFoundExceptionIfConfigDoesNotExist() {

        doThrow(NotFoundException.class).when(getConfigByNameService).execute(nonExistingConfigName);

        assertThrows(NotFoundException.class, () -> {
            extractCsvLines.execute();
        });

        verify(getConfigByNameService, times(1)).execute(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on failure while extracting.")
    public void shouldThrowBusinessExceptionOnFailureWhileExtracting() {

        doThrow(BusinessException.class).when(getConfigByNameService).execute(nonExistingConfigName);

        assertThrows(BusinessException.class, () -> {
            extractCsvLines.execute();
        });

        verify(getConfigByNameService, times(1)).execute(any());
    }

}
