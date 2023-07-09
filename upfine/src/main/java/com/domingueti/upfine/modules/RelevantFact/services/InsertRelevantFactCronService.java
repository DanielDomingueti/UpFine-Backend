package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
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

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;

@Service
@AllArgsConstructor
public class InsertRelevantFactCronService {

    final private RelevantFactRepository relevantFactRepository;

    final private IpeRepository ipeRepository;

    final private GetConfigByNameService getConfigByNameService;

    @Transactional
    public void execute(Long ipeId) {
        try {
            final Ipe ipe = ipeRepository.findById(ipeId).orElseThrow(() -> {
                throw new NotFoundException("IPE not found, ID: " + ipeId);
            });

            final String PDF_FILE_PATH_STR = getConfigByNameService.execute("PDF-FILE-PATH-STR").getValue();
            DownloadFileLocally.execute(ipe.getLink(), PDF_FILE_PATH_STR);

            final String rawPdfContent = ReadPDF.execute(PDF_FILE_PATH_STR);
            final String cleanedPdfContent = removeNullCharacters(rawPdfContent);

            deleteIfExists(get(PDF_FILE_PATH_STR));

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(ipeId);
            relevantFact.setSummarized(cleanedPdfContent);
            relevantFactRepository.save(relevantFact);

        } catch (Exception e) {
            throw new BusinessException("CRON: Error on inserting new relevant fact. Error: " + e.getMessage());
        }
    }

    private String removeNullCharacters(String input) {
        return input.replaceAll("\\x00", "");
    }
}