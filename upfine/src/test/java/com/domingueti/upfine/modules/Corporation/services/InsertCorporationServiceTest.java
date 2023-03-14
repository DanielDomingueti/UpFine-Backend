package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class InsertCorporationServiceTest {

    @InjectMocks
    private InsertCorporationService insertCorporationService;

    @Mock
    private CorporationRepository corporationRepository;


    private String ipeCorporationCnpj;

    private String ipeCorporationName;

    private Corporation corporation;

    @BeforeEach
    void setup() {
        ipeCorporationCnpj = "29874995000100";
        ipeCorporationName = "IPE_NAME";

        corporation = Factory.createCorporation();
    }

    @Test
    @DisplayName("Should save a corporation and return CorporationDTO.")
    public void shouldSaveCorporationAndReturnCorporationDTO() {

        when(corporationRepository.save(any())).thenReturn(corporation);

        assertDoesNotThrow(() -> {
            insertCorporationService.execute(ipeCorporationCnpj, ipeCorporationName);
        });

        verify(corporationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on failure while inserting a corporation.")
    public void shouldThrowNotBusinessExceptionOnFailureWhileInsertingCorporation() {

        doThrow(BusinessException.class).when(corporationRepository).save(any());

        assertThrows(BusinessException.class, () -> {
            insertCorporationService.execute(ipeCorporationCnpj, ipeCorporationName);
        });

        verify(corporationRepository, times(1)).save(any());
    }

}
