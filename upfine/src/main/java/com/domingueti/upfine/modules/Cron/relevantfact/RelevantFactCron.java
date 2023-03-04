package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.utils.statics.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class RelevantFactCron {

    private RelevantFactRepository relevantFactRepository;

    private GetConfigByNameService getConfigByNameService;


    public void execute() {
        final String RELEVANT_FACT_EMAIL_TEMPLATE = "relevant_fact_email_template";
        final List<RelevantFactIpeDAO> relevantFactIpeDAOs = relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday();
        //Use ConvertCnpj rawToFormatted here after refactoring and applying SPR

        final SendEmail sendEmail = new SendEmail();
        final String sendFrom = getConfigByNameService.execute("EMAIL-SENDER").getValue();
        final String emailSubject = "Fatos relevantes: " + LocalDate.now();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("ISO-8859-1");
        templateResolver.setOrder(0);
        templateResolver.setCheckExistence(true);

        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        final Context context = new Context();
        context.setVariable("daos", relevantFactIpeDAOs);
        String resultEmailHtml = templateEngine.process(RELEVANT_FACT_EMAIL_TEMPLATE, context);

        try {

            sendEmail.execute(sendFrom, "danieldomingueti@hotmail.com", emailSubject, resultEmailHtml);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
