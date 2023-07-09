package com.domingueti.upfine.modules.Corporation.controllers;

import com.domingueti.upfine.exceptions.ForbiddenException;
import com.domingueti.upfine.modules.Corporation.dtos.ChooseCorporationDTO;
import com.domingueti.upfine.modules.Corporation.dtos.CorporationDTO;
import com.domingueti.upfine.modules.Corporation.repositories.CorporationRepository;
import com.domingueti.upfine.modules.Corporation.services.InsertUserChosenCorporationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/corporations")
@AllArgsConstructor
public class CorporationController {

    final private CorporationRepository corporationRepository;

    final private InsertUserChosenCorporationsService insertDesiredCorporationsService;

    @GetMapping
    public ResponseEntity<List<CorporationDTO>> findAll(HttpServletRequest request) {

        final String requestToken = request.getHeader("x-api-key");
        final String CRON_TOKEN = System.getenv("CRON_TOKEN");

        if (!requestToken.equals(CRON_TOKEN)) throw new ForbiddenException("Invalid request token");

        final List<CorporationDTO> corporations = corporationRepository.findAll()
                .stream().map(CorporationDTO::new)
                .sorted(comparing(CorporationDTO::getName))
                .collect(toList());

        return ResponseEntity.ok().body(corporations);
    }

    @PostMapping("/chosen")
    public ResponseEntity<Void> insertDesiredCorporations(@RequestBody @Valid ChooseCorporationDTO chooseCorporationDTO, HttpServletRequest request) {

        final String requestToken = request.getHeader("x-api-key");
        final String CRON_TOKEN = System.getenv("CRON_TOKEN");

        if (!requestToken.equals(CRON_TOKEN)) throw new ForbiddenException("Invalid request token");

        insertDesiredCorporationsService.execute(chooseCorporationDTO);

        return ResponseEntity.noContent().build();
    }

}
