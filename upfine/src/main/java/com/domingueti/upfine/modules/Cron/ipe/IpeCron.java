package com.domingueti.upfine.modules.Cron.ipe;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.components.ExtractCsvLines;
import com.domingueti.upfine.utils.statics.ConvertCnpj;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

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
//            final Optional<Ipe> latestIpeOptional = ipeRepository.findTop1ByOrderByReferenceDateDesc();

            final List<String[]> csvLinesIpe = extractCsvLines.execute();

            for (String[] ipeArray : csvLinesIpe) {

                final LocalDate ipeReferenceDate = parse(ipeArray[8], ofPattern("yyyy-MM-dd"));
//                if (latestIpeOptional.isPresent() && ipeReferenceDate.isBefore(latestIpeOptional.get().getReferenceDate())) {
                if (!ipeReferenceDate.isAfter(LocalDate.of(2023, 2, 24))) {
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
        final String ipeCorporationCnpj = ConvertCnpj.formattedToRaw(ipeArray[0]);
        final String ipeCorporationName = ipeArray[1];
        final String ipeSubject = ipeArray[7];
        final String ipeLink = ipeArray[12];
        final Long ipeCorporationId = defineCorporationId(ipeCorporationCnpj, ipeCorporationName);

        deleteRepeatedIpeAndRelevantFact(ipeCorporationId, ipeSubject, ipeLink, ipeReferenceDate);

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
        newCorporation.setCnpj(ipeCorporationCnpj);
        newCorporation.setName(ipeCorporationName);
        newCorporation = corporationRepository.saveAndFlush(newCorporation);

        return newCorporation;
    }

    private void createAndSaveRelevantFact(Ipe newIpe) throws InterruptedException {
            sleep(2000);
//            String summarizedPdfContent = gptClient.summarizeText(rawPdfContent, 300);
            final String summarizedPdfContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(newIpe.getId());
            relevantFact.setSummarized(summarizedPdfContent);
            relevantFactRepository.save(relevantFact);
    }

    private void deleteRepeatedIpeAndRelevantFact(Long ipeCorporationId, String ipeSubject, String ipeLink, LocalDate ipeReferenceDate) {
        Optional<Ipe> existingIpe = ipeRepository.findByCorporationIdAndSubjectAndLinkAndReferenceDateAndDeletedAtIsNull(ipeCorporationId, ipeSubject, ipeLink, ipeReferenceDate);
        if (existingIpe.isPresent()) {
            ipeRepository.delete(existingIpe.get());
            relevantFactRepository.delete(existingIpe.get().getRelevantFact());
        }
    }

}
