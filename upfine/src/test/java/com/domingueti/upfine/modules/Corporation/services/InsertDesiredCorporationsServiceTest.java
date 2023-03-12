package com.domingueti.upfine.modules.Corporation.services;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.InvalidRequestException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.validators.InsertDesiredCorporationsValidator;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
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
public class InsertDesiredCorporationsServiceTest {

    @InjectMocks
    private InsertDesiredCorporationsService insertDesiredCorporationsService;

    @Mock
    private CorporationRepository corporationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InsertDesiredCorporationsValidator validator;

    private ChooseCorporationDTO validChooseCorporationDTO;
    private ChooseCorporationDTO invalidChooseCorporationDTO;

    private Long validId;
    private User validUser;
    private Corporation validCorporation;

    @BeforeEach
    void setup() {

        validChooseCorporationDTO = Factory.createChooseCorporationDTO();
        invalidChooseCorporationDTO = Factory.createChooseCorporationDTO();

        validId = validChooseCorporationDTO.getCorporationIds().stream().findFirst().get();
        validUser = Factory.createUser();
        validCorporation = Factory.createCorporation();
    }

    @Test
    @DisplayName("Should save a corporation and return CorporationDTO.")
    public void shouldSaveCorporationAndReturnCorporationDTO() {

        doNothing().when(validator).execute(validChooseCorporationDTO);
        when(userRepository.findByEmail(validChooseCorporationDTO.getEmail())).thenReturn(validUser);
        when(userRepository.save(any())).thenReturn(validUser);
        when(corporationRepository.findById(validId)).thenReturn(Optional.of(validCorporation));

        assertDoesNotThrow(() -> {
            insertDesiredCorporationsService.execute(validChooseCorporationDTO);
        });

        verify(validator, times(1)).execute(validChooseCorporationDTO);
        verify(userRepository, times(1)).findByEmail(validChooseCorporationDTO.getEmail());
        verify(userRepository, times(2)).save(any());
        verify(corporationRepository, times(1)).findById(validId);
    }

    @Test
    @DisplayName("Should throw InvalidRequestException on validator failure.")
    public void shouldThrowNotInvalidRequestExceptionOnValidatorFailure() {

        doThrow(InvalidRequestException.class).when(validator).execute(invalidChooseCorporationDTO);

        assertThrows(InvalidRequestException.class, () -> {
            insertDesiredCorporationsService.execute(invalidChooseCorporationDTO);
        });

        verify(validator, times(1)).execute(invalidChooseCorporationDTO);
        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
        verify(corporationRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on insertion failure.")
    public void shouldThrowNotBusinessExceptionOnInsertionFailure() {

        doNothing().when(validator).execute(invalidChooseCorporationDTO);
        doThrow(BusinessException.class).when(userRepository).findByEmail(invalidChooseCorporationDTO.getEmail());

        assertThrows(BusinessException.class, () -> {
            insertDesiredCorporationsService.execute(invalidChooseCorporationDTO);
        });

        verify(validator, times(1)).execute(invalidChooseCorporationDTO);
        verify(userRepository, times(1)).findByEmail(invalidChooseCorporationDTO.getEmail());
        verify(userRepository, never()).save(any());
        verify(corporationRepository, never()).findById(any());
    }

}
