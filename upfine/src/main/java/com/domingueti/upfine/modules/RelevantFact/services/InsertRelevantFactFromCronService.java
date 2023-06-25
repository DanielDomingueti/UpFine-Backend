package com.domingueti.upfine.modules.RelevantFact.services;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.integrations.GPT3.GptClient;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.Ipe.models.Ipe;
import com.domingueti.upfine.modules.Ipe.repositories.IpeRepository;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.statics.DownloadFileLocally;
import com.domingueti.upfine.utils.statics.ReadFirstPDF;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;

@Service
@AllArgsConstructor
public class InsertRelevantFactFromCronService {

    final private RelevantFactRepository relevantFactRepository;

    final private IpeRepository ipeRepository;

    final private GptClient gptClient;

    final private GetConfigByNameService getConfigByNameService;


    @Transactional
    public void execute(Long ipeId) {

        try {
//            sleep(2000);


            final Ipe ipe = ipeRepository.findById(ipeId).orElseThrow(() -> {
                throw new NotFoundException("IPE not found, ID: " + ipeId);
            });

            final String PDF_FILE_PATH_STR = getConfigByNameService.execute("PDF-FILE-PATH-STR").getValue();
            DownloadFileLocally.execute(ipe.getLink(), PDF_FILE_PATH_STR);

            //read the pdf content
            final String rawPdfContent = ReadFirstPDF.execute(PDF_FILE_PATH_STR);

            deleteIfExists(get(PDF_FILE_PATH_STR));

//          String summarizedPdfContent = gptClient.summarizeText(rawPdfContent, 300);

            RelevantFact relevantFact = new RelevantFact();
            relevantFact.setIpeId(ipeId);
            relevantFact.setSummarized(rawPdfContent);
            relevantFactRepository.save(relevantFact);
        } catch (Exception e) {
            throw new BusinessException("Error while inserting RelevantFact from CRON: " + e.getMessage());
        }

    }
}
