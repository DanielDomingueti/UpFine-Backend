package com.domingueti.upfine.modules.Cron.ipe;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Ipe.dtos.IpeDTO;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.Ipe.services.InsertIpeCronService;
import com.domingueti.upfine.modules.RelevantFact.services.InsertRelevantFactService;
import com.domingueti.upfine.utils.components.ExtractCsvLines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.fill;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class IpeCronTest {

    @InjectMocks
    private IpeCron ipeCron;

    @Mock
    private ExtractCsvLines extractCsvLines;

    @Mock
    private InsertIpeCronService insertIpeCronService;

    @Mock
    private InsertRelevantFactService insertRelevantFactService;

    @Mock
    private IpeRepository ipeRepository;

    private List<String[]> ipeArrays;
    private LocalDate ipeReferenceDate;
    private IpeDTO ipeDTO;
    private Optional<Ipe> latestIpeOptional;

    @BeforeEach
    void setup() {
        ipeArrays = new ArrayList<>();

        ipeReferenceDate = LocalDate.of(2023, 01, 01);

        ipeDTO = Factory.createIpeDTO();
        latestIpeOptional = Optional.of(Factory.createIpe());
    }

    @Test
    @DisplayName("Should run Ipe CRON inserting Ipe and Relevant Fact when does not exist in DB.")
    public void shouldRunIpeCronInsertingIpeAndRelevantFact() {
        String[] ipeArray = new String[13];
        fill(ipeArray, "any");
        ipeArray[0] = "73351044000130";
        ipeArray[8] = "2023-03-14";
        ipeArrays.add(ipeArray);

        when(ipeRepository.findTop1ByOrderByReferenceDateDesc()).thenReturn(latestIpeOptional);
        when(extractCsvLines.execute()).thenReturn(ipeArrays);
        when(insertIpeCronService.execute(eq(ipeArrays.get(0)), any())).thenReturn(ipeDTO);
        doNothing().when(insertRelevantFactService).execute(ipeDTO.getId());

        assertDoesNotThrow(() -> {
            ipeCron.execute();
        });

        verify(ipeRepository, times(1)).findTop1ByOrderByReferenceDateDesc();
        verify(extractCsvLines, times(1)).execute();
        verify(insertIpeCronService, times(1)).execute(eq(ipeArrays.get(0)), any());
        verify(insertRelevantFactService, times(1)).execute(ipeDTO.getId());
    }

    @Test
    @DisplayName("Should throw BusinessException on cron failure.")
    public void shouldThrowNotBusinessExceptionOnCronFailure() {
        when(ipeRepository.findTop1ByOrderByReferenceDateDesc()).thenReturn(latestIpeOptional);
        doThrow(BusinessException.class).when(extractCsvLines).execute();

        assertThrows(BusinessException.class, () -> {
            ipeCron.execute();
        });

        verify(ipeRepository, times(1)).findTop1ByOrderByReferenceDateDesc();
        verify(extractCsvLines, times(1)).execute();
        verify(insertIpeCronService, never()).execute(any(), any());
        verify(insertRelevantFactService, never()).execute(any());
    }

}
