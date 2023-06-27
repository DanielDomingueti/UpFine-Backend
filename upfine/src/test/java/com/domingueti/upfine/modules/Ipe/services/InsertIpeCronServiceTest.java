package com.domingueti.upfine.modules.Ipe.services;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Corporation.dtos.CorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.services.InsertCorporationService;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Arrays.fill;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class InsertIpeCronServiceTest {

    @InjectMocks
    private InsertIpeCronService insertIpeCronService;

    @Mock
    private CorporationRepository corporationRepository;

    @Mock
    private IpeRepository ipeRepository;

    @Mock
    private InsertCorporationService insertCorporationService;

    private String[] ipeArray;
    private LocalDate ipeReferenceDate;

    private String ipeCorporationCnpj;
    private String ipeCorporationName;

    private String invalidIpeCorporationCnpj;
    private String invalidIpeCorporationName;

    private CorporationDTO newCorporationDto;
    private Corporation corporation;
    private Ipe ipe;

    @BeforeEach
    void setup() {
        ipeArray = new String[13];
        fill(ipeArray, "any");

        ipeReferenceDate = LocalDate.of(2023, 01, 01);
        ipeCorporationCnpj = "99108774000106";
        ipeCorporationName = "corporation name";

        invalidIpeCorporationCnpj = "42624182000111";
        invalidIpeCorporationName = "invalid corporation name";

        newCorporationDto = Factory.createCorporationDTO();
        corporation = Factory.createCorporation();
        ipe = Factory.createIpe();

    }

    @Test
    @DisplayName("Should insert an Ipe and return IpeDTO with existing corporation")
    public void shouldSaveIpeAndReturnIpeDTOWithExistingCorporation() {
        ipeArray[0] = ipeCorporationCnpj;
        ipeArray[1] = ipeCorporationName;

        when(corporationRepository.findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName)).thenReturn(Optional.of(corporation));
        doNothing().when(ipeRepository).deleteRepeated(corporation.getId(), ipeArray[7], ipeArray[12], ipeReferenceDate);
        when(ipeRepository.save(any())).thenReturn(ipe);

        assertDoesNotThrow(() -> {
            insertIpeCronService.execute(ipeArray, ipeReferenceDate);
        });

        verify(corporationRepository, times(1)).findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);
        verify(ipeRepository, times(1)).deleteRepeated(corporation.getId(), ipeArray[7], ipeArray[12], ipeReferenceDate);
        verify(ipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should insert an Ipe and return IpeDTO with non-existing corporation")
    public void shouldSaveIpeAndReturnIpeDTOWithNonExistingCorporation() {
        ipeArray[0] = invalidIpeCorporationCnpj;
        ipeArray[1] = invalidIpeCorporationName;

        when(corporationRepository.findByCnpjAndName(invalidIpeCorporationCnpj, invalidIpeCorporationName)).thenReturn(Optional.empty());
        when(insertCorporationService.execute(invalidIpeCorporationCnpj, invalidIpeCorporationName)).thenReturn(newCorporationDto);
        doNothing().when(ipeRepository).deleteRepeated(newCorporationDto.getId(), ipeArray[7], ipeArray[12], ipeReferenceDate);
        when(ipeRepository.save(any())).thenReturn(ipe);

        assertDoesNotThrow(() -> {
            insertIpeCronService.execute(ipeArray, ipeReferenceDate);
        });

        verify(corporationRepository, times(1)).findByCnpjAndName(invalidIpeCorporationCnpj, invalidIpeCorporationName);
        verify(insertCorporationService, times(1)).execute(invalidIpeCorporationCnpj, invalidIpeCorporationName);
        verify(ipeRepository, times(1)).deleteRepeated(newCorporationDto.getId(), ipeArray[7], ipeArray[12], ipeReferenceDate);
        verify(ipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on insertion failure.")
    public void shouldThrowNotBusinessExceptionOnInsertionFailure() {
        ipeArray[0] = ipeCorporationCnpj;
        ipeArray[1] = ipeCorporationName;

        doThrow(BusinessException.class).when(corporationRepository).findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);

        assertThrows(BusinessException.class, () -> {
            insertIpeCronService.execute(ipeArray, ipeReferenceDate);
        });

        verify(corporationRepository, times(1)).findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);
        verify(insertCorporationService, never()).execute(any(), any());
        verify(ipeRepository, never()).deleteRepeated(any(), any(), any(), any());
        verify(ipeRepository, never()).save(any());
    }

}
