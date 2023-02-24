package com.domingueti.upfine.modules.Cron.ipe.services;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.components.ExtractCsvLines;
import com.domingueti.upfine.utils.statics.ConvertToRawCnpj;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class IpeCron {
    //verifies for new IPEs from the CSV and save the final summarized text

//    private GptClient gptClient;

    private ExtractCsvLines extractCsvLines;

    private IpeRepository ipeRepository;

    private CorporationRepository corporationRepository;

    private RelevantFactRepository relevantFactRepository;

    @Transactional
    public void execute() {

        try {
            final Optional<Ipe> latestIpeOptional = ipeRepository.findTop1ByOrderByReferenceDateDesc();

            final List<String[]> csvLinesIpe = extractCsvLines.execute();

            for (String[] ipeArray : csvLinesIpe) {

                final LocalDate ipeReferenceDate = LocalDate.parse(ipeArray[8], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                if (latestIpeOptional.isPresent() && ipeReferenceDate.isBefore(latestIpeOptional.get().getReferenceDate())) {
                if (!ipeReferenceDate.isAfter(LocalDate.of(2023, 02, 16))) {
                    continue;
                }

                final Ipe ipe = createAndSaveIpe(ipeArray, ipeReferenceDate);

                createAndSaveRelevantFact(ipe);

            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Ipe createAndSaveIpe(String[] ipeArray, LocalDate ipeReferenceDate) {
        final String ipeCorporationCnpj = ConvertToRawCnpj.execute(ipeArray[0]);
        final String ipeCorporationName = ipeArray[1];
        final String ipeSubject = ipeArray[7];
        final String ipeLink = ipeArray[12];
        final Long ipeCorporationId = defineCorporationId(ipeCorporationCnpj, ipeCorporationName);

        Ipe ipe = new Ipe();
        ipe.setSubject(ipeSubject);
        ipe.setLink(ipeLink);
        ipe.setCorporationId(ipeCorporationId);
        ipe.setReferenceDate(ipeReferenceDate);
        ipe = ipeRepository.saveAndFlush(ipe);

        return ipe;
    }

    private Long defineCorporationId(String ipeCorporationCnpj, String ipeCorporationName) {
        final Optional<Corporation> corporationOptional = corporationRepository.findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);
        if (corporationOptional.isPresent()) {
            return corporationOptional.get().getId();
        }

        final Corporation newCorporation = createNewCorporation(ipeCorporationCnpj, ipeCorporationName);
        return newCorporation.getId();
    }

    private Corporation createNewCorporation(String ipeCorporationCnpj, String ipeCorporationName) {
        Corporation newCorporation = new Corporation();
        newCorporation.setCnpj(ConvertToRawCnpj.execute(ipeCorporationCnpj));
        newCorporation.setName(ipeCorporationName);
        newCorporation = corporationRepository.saveAndFlush(newCorporation);

        return newCorporation;
    }

    private void createAndSaveRelevantFact(Ipe newIpe) throws InterruptedException {
//        try {
            Thread.sleep(2000);
//            String summarizedPdfContent = gptClient.summarizeText(rawPdfContent, 300);
            final String summarizedPdfContent = "summarizedWithoutGPT";

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(newIpe.getId());
            relevantFact.setSummarizedText(summarizedPdfContent);
            relevantFactRepository.save(relevantFact);
            
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
