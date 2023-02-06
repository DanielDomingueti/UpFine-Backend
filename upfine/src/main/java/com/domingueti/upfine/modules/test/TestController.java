package com.domingueti.upfine.modules.test;

import com.domingueti.upfine.utils.beans.DownloadFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DownloadFile downloadFile;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> execute() throws IOException {
        String pdfUrl = "https://www.rad.cvm.gov.br/ENET/frmDownloadDocumento.aspx?Tela=ext&descTipo=IPE&CodigoInstituicao=1&numProtocolo=1050720&numSequencia=575450&numVersao=1";
        String saveLocation = "src/main/resources/pdf/output.pdf";
        String pdfOutput = "";

        byte[] arrayOfBytes = downloadFile.executeBytes(pdfUrl);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileOutputStream fos = new FileOutputStream(saveLocation)) {
            baos.write(arrayOfBytes);
            baos.writeTo(fos);

            PDDocument document = PDDocument.load(new File("src/main/resources/pdf/output.pdf"));
            PDFTextStripper stripper = new PDFTextStripper();
            pdfOutput = stripper.getText(document);
            System.out.println(pdfOutput);

        } catch (IOException e) {
            e.getCause();
        }

        return ResponseEntity.ok().body(pdfOutput);
    }

}
