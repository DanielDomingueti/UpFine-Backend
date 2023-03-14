package com.domingueti.upfine.modules.Cron.relevantfact;

import com.domingueti.upfine.Factory;
import com.domingueti.upfine.exceptions.BusinessException;
import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.repositories.RelevantFactRepository;
import com.domingueti.upfine.modules.User.models.User;
import com.domingueti.upfine.modules.User.repositories.UserRepository;
import com.domingueti.upfine.utils.components.GenerateHtmlEmail;
import com.domingueti.upfine.utils.components.SendEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RelevantFactCronTest {

    @InjectMocks
    private RelevantFactCron relevantFactCron;

    @Mock
    private RelevantFactRepository relevantFactRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenerateHtmlEmail generateHtmlEmail;

    @Mock
    private SendEmail sendEmail;

    private User user;
    private RelevantFactIpeDAO relevantFactIpeDAO;
    private String htmlEmailOutput;

    @BeforeEach
    void setup() {
        user = Factory.createUser();
        relevantFactIpeDAO = mock(RelevantFactIpeDAO.class);
        htmlEmailOutput = "htmlEmailOutput";

        when(relevantFactIpeDAO.getUserName()).thenReturn("username");
        when(relevantFactIpeDAO.getName()).thenReturn("name");
        when(relevantFactIpeDAO.getCnpj()).thenReturn("cnpj");
        when(relevantFactIpeDAO.getSubject()).thenReturn("subject");
        when(relevantFactIpeDAO.getReferenceDate()).thenReturn(now());
        when(relevantFactIpeDAO.getSummarized()).thenReturn("summarized");

    }

    @Test
    @DisplayName("Should run RelevantFact CRON fetching active users, creating emails based on chosen corporations and sending them.")
    public void shouldRunRelevantFactCronAndSendEmailToActiveUsersWithChosenCorporations() throws MessagingException {
        List<RelevantFactIpeDAO> relevantFactIpeDAOList = List.of(relevantFactIpeDAO);

        when(userRepository.findByActiveIsTrueAndDeletedAtIsNull()).thenReturn(List.of(user));
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId())).thenReturn(List.of(relevantFactIpeDAO));
        when(generateHtmlEmail.execute(relevantFactIpeDAOList)).thenReturn(htmlEmailOutput);
        doNothing().when(sendEmail).execute(user.getEmail(), htmlEmailOutput);

        assertDoesNotThrow(() -> {
            relevantFactCron.execute();
        });

        verify(userRepository, times(1)).findByActiveIsTrueAndDeletedAtIsNull();
        verify(relevantFactRepository, times(2)).findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId());
        verify(generateHtmlEmail, times(1)).execute(relevantFactIpeDAOList);
        verify(sendEmail, times(1)).execute(user.getEmail(), htmlEmailOutput);
    }

    @Test
    @DisplayName("Should run RelevantFact CRON fetching active users, creating emails and sending them.")
    public void shouldRunRelevantFactCronAndSendEmailToActiveUsers() throws MessagingException {
        List<RelevantFactIpeDAO> relevantFactIpeDAOList = List.of(relevantFactIpeDAO);

        when(userRepository.findByActiveIsTrueAndDeletedAtIsNull()).thenReturn(List.of(user));
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId())).thenReturn(List.of());
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(user.getId())).thenReturn(List.of(relevantFactIpeDAO));
        when(generateHtmlEmail.execute(relevantFactIpeDAOList)).thenReturn(htmlEmailOutput);
        doNothing().when(sendEmail).execute(user.getEmail(), htmlEmailOutput);

        assertDoesNotThrow(() -> {
            relevantFactCron.execute();
        });

        verify(userRepository, times(1)).findByActiveIsTrueAndDeletedAtIsNull();
        verify(relevantFactRepository, times(1)).findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId());
        verify(relevantFactRepository, times(1)).findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(user.getId());
        verify(generateHtmlEmail, times(1)).execute(relevantFactIpeDAOList);
        verify(sendEmail, times(1)).execute(user.getEmail(), htmlEmailOutput);
    }

    @Test
    @DisplayName("Should throw BusinessException on cron failure.")
    public void shouldThrowNotBusinessExceptionOnCronFailure() throws MessagingException {
        List<RelevantFactIpeDAO> relevantFactIpeDAOList = List.of(relevantFactIpeDAO);

        when(userRepository.findByActiveIsTrueAndDeletedAtIsNull()).thenReturn(List.of(user));
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId())).thenReturn(List.of());
        when(relevantFactRepository.findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(user.getId())).thenReturn(relevantFactIpeDAOList);
        doThrow(BusinessException.class).when(generateHtmlEmail).execute(any());

        assertThrows(BusinessException.class, () -> {
            relevantFactCron.execute();
        });

        verify(userRepository, times(1)).findByActiveIsTrueAndDeletedAtIsNull();
        verify(relevantFactRepository, times(1)).findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(user.getId());
        verify(relevantFactRepository, times(1)).findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(user.getId());
        verify(generateHtmlEmail, times(1)).execute(relevantFactIpeDAOList);
        verify(sendEmail, never()).execute(any(), any());
    }

}
