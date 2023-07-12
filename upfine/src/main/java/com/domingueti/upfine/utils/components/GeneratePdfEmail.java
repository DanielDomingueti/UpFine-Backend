package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.dtos.RelevantFactIpeDTO;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class GeneratePdfEmail {

    private final GetConfigByNameService getConfigByNameService;

    public File execute(List<RelevantFactIpeDAO> relevantFactIpeDAOs) throws IOException {
        final String PATH = "src/main/resources/pdf/";
        final String templateRelevantFactName = getConfigByNameService.execute("TEMPLATE_RELEVANT_FACT_NAME").getValue();
        final String templateRelevantFactPath = PATH + templateRelevantFactName;

        try {
            final List<RelevantFactIpeDTO> relevantFactIpeDTOs = relevantFactIpeDAOs.stream().map(RelevantFactIpeDTO::new).collect(Collectors.toList());
            final String templateRelevantFactPrefixPath = getConfigByNameService.execute("TEMPLATE_RELEVANT_FACT_PREFIX_PATH").getValue();
            final String templateRelevantFactSufixPath = getConfigByNameService.execute("TEMPLATE_RELEVANT_FACT_SUFIX_PATH").getValue();
            final String charsetPattern = getConfigByNameService.execute("CHARSET-PATTERN").getValue();

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix(templateRelevantFactPrefixPath);
            templateResolver.setSuffix(templateRelevantFactSufixPath);
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding(charsetPattern);
            templateResolver.setOrder(0);
            templateResolver.setCheckExistence(true);

            TemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            final Context context = new Context();
            context.setVariable("dtos", relevantFactIpeDTOs);

            String processedHtml = templateEngine.process(templateRelevantFactName, context);
            processedHtml = processedHtml.replaceAll("&amp;", "&");

            File pdfFile = File.createTempFile("generated_pdf_", ".pdf");

            FileOutputStream fileOut = new FileOutputStream(pdfFile);
            HtmlConverter.convertToPdf(processedHtml, fileOut);
            fileOut.flush();

            return pdfFile;
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while generating PDF: " + e.getMessage());
        }
        finally {
            Files.deleteIfExists(Path.of(templateRelevantFactPath));
        }
    }
}
