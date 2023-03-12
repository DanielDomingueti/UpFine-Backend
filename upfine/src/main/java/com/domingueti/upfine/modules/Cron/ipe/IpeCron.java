package com.domingueti.upfine.modules.Cron.ipe;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Ipe.dtos.IpeDTO;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.Ipe.services.InsertIpeFromCronService;
import com.domingueti.upfine.modules.RelevantFact.services.InsertRelevantFactFromCronService;
import com.domingueti.upfine.utils.components.ExtractCsvLines;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
@AllArgsConstructor
public class IpeCron {
    //verifies for new IPEs from the CSV and save the final summarized text

//    private GptClient gptClient;

    final private ExtractCsvLines extractCsvLines;

    final private InsertIpeFromCronService insertIpeFromCronService;

    final private InsertRelevantFactFromCronService insertRelevantFactFromCronService;

    final private IpeRepository ipeRepository;

    @Transactional
    public void execute() {

        try {
            final Optional<Ipe> latestIpeOptional = ipeRepository.findTop1ByOrderByReferenceDateDesc();

            for (String[] ipeArray : extractCsvLines.execute()) {

                final LocalDate ipeReferenceDate = parse(ipeArray[8], ofPattern("yyyy-MM-dd"));

                if (doesIpeExistsInDatabase(latestIpeOptional, ipeReferenceDate)) {
                    continue;
                }

                IpeDTO ipeDTO = insertIpeFromCronService.execute(ipeArray, ipeReferenceDate);

                insertRelevantFactFromCronService.execute(ipeDTO.getId());

            }

        } catch (Exception e) {
            throw new BusinessException("Erro ao rodar CRON de Ipe." + e.getMessage());
        }
    }

    private boolean doesIpeExistsInDatabase(Optional<Ipe> latestIpeOptional, LocalDate ipeReferenceDate) {
        return latestIpeOptional.isPresent() && !ipeReferenceDate.isAfter(latestIpeOptional.get().getReferenceDate());
//        LocalDate testDate = LocalDate.of(2023, 3, 1);
//        return !ipeReferenceDate.isAfter(testDate);
    }

}
