package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.daos.ConfigDAO;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class GenerateHtmlEmailTest {

    @InjectMocks
    private GenerateHtmlEmail generateHtmlEmail;

    @Mock
    private GetConfigByNameService getConfigByNameService;

    @Mock
    private ConfigDAO templateRelevantFactNameDao;
    @Mock
    private ConfigDAO templateRelevantFactPrefixPathDao;
    @Mock
    private ConfigDAO templateRelevantFactSufixPathDao;
    @Mock
    private ConfigDAO charsetPatternDao;

    @Mock
    private RelevantFactIpeDAO validRelevantFactIpeDAO;
    @Mock
    private RelevantFactIpeDAO invalidRelevantFactIpeDAO;

    private String templateRelevantFactName;
    private String templateRelevantFactPrefixPath;
    private String templateRelevantFactSufixPath;
    private String charsetPattern;
    private String nonExistingConfigName;

    @BeforeEach
    void setup() {
        templateRelevantFactName = "TEMPLATE_RELEVANT_FACT_NAME";
        templateRelevantFactPrefixPath = "TEMPLATE_RELEVANT_FACT_PREFIX_PATH";
        templateRelevantFactSufixPath = "TEMPLATE_RELEVANT_FACT_SUFIX_PATH";
        charsetPattern = "CHARSET-PATTERN";
        nonExistingConfigName = templateRelevantFactName;

        when(validRelevantFactIpeDAO.getUserName()).thenReturn("userName");
        when(validRelevantFactIpeDAO.getCorporation()).thenReturn("corporation");
        when(validRelevantFactIpeDAO.getCnpj()).thenReturn("00650062000149");
        when(validRelevantFactIpeDAO.getSubject()).thenReturn("subject");
        when(validRelevantFactIpeDAO.getReferenceDate()).thenReturn(LocalDate.of(2023, 01, 01));
        when(validRelevantFactIpeDAO.getSummarized()).thenReturn("summarized");
    }

    @Test
    @DisplayName("Should generate HTML email.")
    public void shouldGenerateHtmlEmail() {

        when(getConfigByNameService.execute(templateRelevantFactName)).thenReturn(templateRelevantFactNameDao);
        when(getConfigByNameService.execute(templateRelevantFactPrefixPath)).thenReturn(templateRelevantFactPrefixPathDao);
        when(getConfigByNameService.execute(templateRelevantFactSufixPath)).thenReturn(templateRelevantFactSufixPathDao);
        when(getConfigByNameService.execute(charsetPattern)).thenReturn(charsetPatternDao);

        when(templateRelevantFactNameDao.getValue()).thenReturn("template_relevant_fact");
        when(templateRelevantFactPrefixPathDao.getValue()).thenReturn("templates/");
        when(templateRelevantFactSufixPathDao.getValue()).thenReturn(".html");
        when(charsetPatternDao.getValue()).thenReturn("ISO-8859-1");

        assertDoesNotThrow(() -> {
            generateHtmlEmail.execute(Arrays.asList(validRelevantFactIpeDAO));
        });
        verify(getConfigByNameService, times(1)).execute(templateRelevantFactName);
        verify(getConfigByNameService, times(1)).execute(templateRelevantFactPrefixPath);
        verify(getConfigByNameService, times(1)).execute(templateRelevantFactSufixPath);
        verify(getConfigByNameService, times(1)).execute(charsetPattern);
    }

    @Test
    @DisplayName("Should throw NotFoundException if config does not exist by name.")
    public void shouldThrowNotFoundExceptionIfConfigDoesNotExist() {

        doThrow(NotFoundException.class).when(getConfigByNameService).execute(nonExistingConfigName);

        assertThrows(NotFoundException.class, () -> {
            generateHtmlEmail.execute(Arrays.asList(validRelevantFactIpeDAO));
        });

        verify(getConfigByNameService, times(1)).execute(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on failure while generating HTML.")
    public void shouldThrowBusinessExceptionOnFailureWhileGeneratingHtml() {

        doThrow(BusinessException.class).when(getConfigByNameService).execute(nonExistingConfigName);

        assertThrows(BusinessException.class, () -> {
            generateHtmlEmail.execute(Arrays.asList(invalidRelevantFactIpeDAO));
        });

        verify(getConfigByNameService, never()).execute(any());
    }

}
