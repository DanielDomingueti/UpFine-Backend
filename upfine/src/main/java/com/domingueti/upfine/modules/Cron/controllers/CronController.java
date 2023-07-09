package com.domingueti.upfine.modules.Cron.controllers;

import com.domingueti.upfine.exceptions.ForbiddenException;
import com.domingueti.upfine.modules.Cron.ipe.IpeCron;
import com.domingueti.upfine.modules.Cron.relevantfact.RelevantFactCron;
import com.domingueti.upfine.modules.RelevantFact.services.SendDailyRelevantFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/crons")
public class CronController {

    @Autowired
    private IpeCron ipeCron;

    @Autowired
    private RelevantFactCron relevantFactCron;

    @Autowired
    private SendDailyRelevantFactService sendDailyRelevantFactService;

    @Value("${cron.token}")
    private String CRON_TOKEN;

    @PostMapping(value = "/ipe")
    public ResponseEntity<Void> runIpeCron(HttpServletRequest request) {

        final String requestToken = request.getHeader("x-api-key");

        if (!requestToken.equals(CRON_TOKEN)) throw new ForbiddenException("Invalid request token");

        ipeCron.execute();

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/relevantFact")
    public ResponseEntity<Void> runRelevantFact(HttpServletRequest request) {

        final String requestToken = request.getHeader("x-api-key");

        if (!requestToken.equals(CRON_TOKEN)) throw new ForbiddenException("Invalid request token");

        relevantFactCron.execute();

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/sendEmail")
    public ResponseEntity<Void> runSendEmail(HttpServletRequest request) {

        final String requestToken = request.getHeader("x-api-key");

        if (!requestToken.equals(CRON_TOKEN)) throw new ForbiddenException("Invalid request token");

        sendDailyRelevantFactService.execute();

        return ResponseEntity.noContent().build();
    }

}
