package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.dtos.RelevantFactIpeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class GeneratePdfEmail {

    final private GetConfigByNameService getConfigByNameService;

    public File execute(List<RelevantFactIpeDAO> relevantFactIpeDAOs) {
        try {
            final List<RelevantFactIpeDTO> relevantFactIpeDTOs = relevantFactIpeDAOs.stream().map(RelevantFactIpeDTO::new).collect(Collectors.toList());
            final String templateRelevantFactName = getConfigByNameService.execute("TEMPLATE_RELEVANT_FACT_NAME").getValue();
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

//            return templateEngine.process(templateRelevantFactName, context);
            return null;
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while generating HTML email: " + e.getMessage());
        }
    }
}
