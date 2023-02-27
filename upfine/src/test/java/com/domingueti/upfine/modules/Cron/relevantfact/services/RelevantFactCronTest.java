package com.domingueti.upfine.modules.Cron.relevantfact.services;

import com.domingueti.upfine.modules.Cron.relevantfact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.Email.models.Email;
import com.domingueti.upfine.modules.Email.repositories.EmailRepository;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.domingueti.upfine.Factory.createEmail;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RelevantFactCronTest {

    @InjectMocks
    private RelevantFactCron relevantFactCron;

    @Mock
    private RelevantFactRepository relevantFactRepository;

    @Mock
    private EmailRepository emailRepository;

//    @Mock
//    private SendEmail sendEmail;

    @Mock
    private RelevantFactIpeDAO relevantFactIpeDAO;
    private Email email;

    @BeforeEach
    void setup() {
        when(relevantFactIpeDAO.getName()).thenReturn("name");
        when(relevantFactIpeDAO.getCnpj()).thenReturn("cnpj");
        when(relevantFactIpeDAO.getSubject()).thenReturn("subject");
        when(relevantFactIpeDAO.getSummarizedText()).thenReturn("summarizedText");

        email = createEmail();
    }

    @Test
    @DisplayName("Should send email with relevant facts")
    public void shouldSendEmailOfRelevantFact() {
        when(relevantFactIpeDAO.getReferenceDate()).thenReturn(now());
        //should get a list of RelevantFactIpeDAO when there's a valid IPE in the current day
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday()).thenReturn(List.of(relevantFactIpeDAO));

        //create a single final template with all relevant facts

        //should find all valid emails in the database
        when(emailRepository.findAll()).thenReturn(List.of(email));

        //for each email, should call SendEmail component to send the final template.
    }

    @Test
    @DisplayName("Should do nothing when there's no IPE in the current day")
    public void shouldDoNothingWhenTheresNoIpe() {
        when(relevantFactIpeDAO.getReferenceDate()).thenReturn(of(2022, 05, 17));
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday()).thenReturn(List.of());

        //should get an empty list of RelevantFactIpeDAO when there's not a valid IPE in the current day

    }

    //deal with failure on running
}
