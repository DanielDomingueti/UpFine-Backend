package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
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
public class InsertRelevantFactServiceTest {

    @InjectMocks
    private InsertRelevantFactService insertRelevantFactService;

    @Mock
    private RelevantFactRepository relevantFactRepository;

    private Long ipeId;
    private RelevantFact relevantFact;

    @BeforeEach
    void setup() {
        ipeId = 1L;
        relevantFact = Factory.createRelevantFact();
    }

    @Test
    @DisplayName("Should insert a RelevantFact and return void")
    public void shouldSaveRelevantFactAndReturnVoid() {
        when(relevantFactRepository.save(any())).thenReturn(relevantFact);

        assertDoesNotThrow(() -> {
            insertRelevantFactService.execute(ipeId);
        });

        verify(relevantFactRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException on insertion failure.")
    public void shouldThrowNotBusinessExceptionOnInsertionFailure() {
        doThrow(BusinessException.class).when(relevantFactRepository).save(any());

        assertThrows(BusinessException.class, () -> {
            insertRelevantFactService.execute(ipeId);
        });

        verify(relevantFactRepository, times(1)).save(any());
    }

}
