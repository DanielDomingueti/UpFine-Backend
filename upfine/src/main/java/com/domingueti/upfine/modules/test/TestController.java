package com.domingueti.upfine.modules.test;

import com.domingueti.upfine.modules.Cron.ipe.IpeCron;
import com.domingueti.upfine.modules.Cron.relevantfact.RelevantFactCron;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private IpeCron ipeCron;

    private RelevantFactCron relevantFactCron;

    @GetMapping
    public ResponseEntity<Void> execute() {
//        https://www.rad.cvm.gov.br/ENET/frmDownloadDocumento.aspx?Tela=ext&descTipo=IPE&CodigoInstituicao=1&numProtocolo=1050868&numSequencia=575598&numVersao=1"

        ipeCron.execute();
        relevantFactCron.execute();

        return ResponseEntity.noContent().build();
    }

}
