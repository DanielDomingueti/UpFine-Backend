package com.domingueti.upfine.modules.Cron.services;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.beans.DownloadFile;
import com.domingueti.upfine.utils.statics.ConvertToRawCnpj;
import com.domingueti.upfine.utils.beans.ExtractCsvIPE;
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

    private DownloadFile downloadFile;

//    private GptClient gptClient;

    private IpeRepository ipeRepository;

    private CorporationRepository corporationRepository;

    private RelevantFactRepository relevantFactRepository;

    @Transactional
    public void execute() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            Optional<Ipe> latestIpeOptional = ipeRepository.findTop1ByOrderByReferenceDateDesc();

            List<String[]> listOfDownloadedIpeArray = ExtractCsvIPE.execute();

            for (String[] ipeArray : listOfDownloadedIpeArray) {

                LocalDate ipeReferenceDate = LocalDate.parse(ipeArray[8], dateTimeFormatter);
//                if (latestIpeOptional.isPresent() && ipeReferenceDate.isBefore(latestIpeOptional.get().getReferenceDate())) {
                if (!ipeReferenceDate.isAfter(LocalDate.of(2023, 02, 15))) {
                    continue;
                }

                Ipe ipe = createIpeObject(ipeArray, ipeReferenceDate);

                createRelevantFact(ipe);

            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Ipe createIpeObject(String[] ipeArray, LocalDate ipeReferenceDate) {
        Ipe ipe = new Ipe();
        String ipeCorporationCnpj = ConvertToRawCnpj.execute(ipeArray[0]);
        String ipeCorporationName = ipeArray[1];
        String ipeSubject = ipeArray[7];
        String ipeLink = ipeArray[12];
        Long ipeCorporationId = defineCorporationId(ipeCorporationCnpj, ipeCorporationName);

        ipe.setSubject(ipeSubject);
        ipe.setLink(ipeLink);
        ipe.setCorporationId(ipeCorporationId);
        ipe.setReferenceDate(ipeReferenceDate);

        ipe = ipeRepository.saveAndFlush(ipe);
        return ipe;
    }

    private Long defineCorporationId(String ipeCorporationCnpj, String ipeCorporationName) {
        Optional<Corporation> corporationOptional = corporationRepository.findByCnpjAndName(ipeCorporationCnpj, ipeCorporationName);
        if (corporationOptional.isPresent()) {
            return corporationOptional.get().getId();
        }

        Corporation newCorporation = createNewCorporation(ipeCorporationCnpj, ipeCorporationName);
        return newCorporation.getId();
    }

    private Corporation createNewCorporation(String ipeCorporationCnpj, String ipeCorporationName) {
        Corporation newCorporation = new Corporation();
        newCorporation.setCnpj(ConvertToRawCnpj.execute(ipeCorporationCnpj));
        newCorporation.setName(ipeCorporationName);
        newCorporation = corporationRepository.saveAndFlush(newCorporation);
        return newCorporation;
    }

    private void createRelevantFact(Ipe newIpe) throws InterruptedException {
//        try {
            Thread.sleep(2000);
            byte[] pdfContent = downloadFile.execute(newIpe.getLink());
//            String summarizedPdfContent = gptClient.summarizeText(rawPdfContent, 300);
            String summarizedPdfContent = "summarizedWithoutGPT";

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(newIpe.getId());
            relevantFact.setSummarizedText(summarizedPdfContent);
            relevantFactRepository.save(relevantFact);
            
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
