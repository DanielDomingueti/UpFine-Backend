package com.domingueti.upfine.modules.Config.services;

import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.daos.ConfigDAO;
import com.domingueti.upfine.modules.Config.repositories.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class GetConfigByNameServiceTest {

    @InjectMocks
    private GetConfigByNameService getConfigByNameService;

    @Mock
    private ConfigRepository configRepository;

    @Mock
    private ConfigDAO validConfigDao;

    private String validName;

    private String invalidName;

    @BeforeEach
    void setup() {
        validName = "VALID_NAME";
        invalidName = "INVALID_NAME";
    }

    @Test
    @DisplayName("Should return ConfigDAO.")
    public void shouldReturnConfigDAO() {

        when(configRepository.findByName(validName)).thenReturn(Optional.of(validConfigDao));

        assertDoesNotThrow(() -> {
            getConfigByNameService.execute(validName);
        });

        verify(configRepository, times(1)).findByName(validName);
    }

    @Test
    @DisplayName("Should throw NotFoundException if ConfigDAO does not exist by name.")
    public void shouldThrowNotFoundExceptionIfConfigDaoNameDoesNotExist() {

        doThrow(NotFoundException.class).when(configRepository).findByName(invalidName);

        assertThrows(NotFoundException.class, () -> {
            getConfigByNameService.execute(invalidName);
        });

        verify(configRepository, times(1)).findByName(invalidName);
    }

}
