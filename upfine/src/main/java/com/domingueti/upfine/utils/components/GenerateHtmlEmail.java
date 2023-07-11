package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.utils.components.dtos.GenerateHtmlEmailDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@AllArgsConstructor
@Component
public class GenerateHtmlEmail {

    final private GetConfigByNameService getConfigByNameService;

    public String execute(GenerateHtmlEmailDTO generateHtmlEmailDTO) {
        try {
            final String templateNotificationEmailName = getConfigByNameService.execute("TEMPLATE_NOTIFICATION_EMAIL_NAME").getValue();
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
            context.setVariable("dtos", generateHtmlEmailDTO);

            return templateEngine.process(templateNotificationEmailName, context);
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BusinessException("Error while generating HTML email: " + e.getMessage());
        }
    }
}
