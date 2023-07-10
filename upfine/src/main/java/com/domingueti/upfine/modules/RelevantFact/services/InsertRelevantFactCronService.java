package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.statics.DownloadFileLocally;
import com.domingueti.upfine.utils.statics.ReadPDF;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;

@Service
@AllArgsConstructor
public class InsertRelevantFactCronService {

    final private RelevantFactRepository relevantFactRepository;

    final private IpeRepository ipeRepository;

    final private GetConfigByNameService getConfigByNameService;

    @Transactional
    public void execute() {
        try {
            final List<Ipe> newIpes = ipeRepository.findByNonExistingRelevantFactAndDeletedAtIsNull();
            final int size = newIpes.size();
            int i = 1;

            for (Ipe ipe : newIpes) {
                final String PDF_FILE_PATH_STR = getConfigByNameService.execute("PDF-FILE-PATH-STR").getValue();
                DownloadFileLocally.execute(ipe.getLink(), PDF_FILE_PATH_STR);

                final String rawPdfContent = ReadPDF.execute(PDF_FILE_PATH_STR);
                final String cleanedPdfContent = removeNullCharacters(rawPdfContent);

                deleteIfExists(get(PDF_FILE_PATH_STR));

                RelevantFact relevantFact = new RelevantFact();
                relevantFact.setIpeId(ipe.getId());
                relevantFact.setSummarized(cleanedPdfContent);
                relevantFactRepository.save(relevantFact);

                System.out.println("IPE " + i + " out of " + size);
                i++;
            }

        } catch (Exception e) {
            throw new BusinessException("CRON: Error on inserting new relevant fact. Error: " + e.getMessage());
        }
    }

    private String removeNullCharacters(String input) {
        return input.replaceAll("\\x00", "");
    }
}