package com.domingueti.upfine.modules.test;

import com.domingueti.upfine.components.StockData.interfaces.GetStockData;
import com.domingueti.upfine.modules.Cron.services.IpeCron;
import com.domingueti.upfine.utils.beans.DownloadFileToByteArray;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private DownloadFileToByteArray downloadFileToByteArray;

    private RestTemplate restTemplate;

    private GetStockData getStockData;

    private IpeCron ipeCron;

    @GetMapping
    public ResponseEntity<String> execute() {
        String pdfUrl = "https://www.rad.cvm.gov.br/ENET/frmDownloadDocumento.aspx?Tela=ext&descTipo=IPE&CodigoInstituicao=1&numProtocolo=1050868&numSequencia=575598&numVersao=1";
        String saveLocation = "src/main/resources/pdf/output.pdf";
        String pdfOutput = "";

//        byte[] arrayOfBytes = downloadFile.execute(pdfUrl);
//
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            FileOutputStream fos = new FileOutputStream(saveLocation)) {
//            baos.write(arrayOfBytes);
//            baos.writeTo(fos);
//
//            PDDocument document = PDDocument.load(new File("src/main/resources/pdf/output.pdf"));
//            PDFTextStripper stripper = new PDFTextStripper();
//            pdfOutput = stripper.getText(document);
////            System.out.println(pdfOutput);
//
//            StockIndicatorsDTO dto = getStockData.execute("UNIP6");
////            System.out.println(dto.toString());
//
//            List<String[]> vector = ExtractCsvIPE.execute();
//            vector.forEach(v -> System.out.println(v.toString()));
        ipeCron.execute();

//        } catch (IOException e) {
//            e.getCause();
//        }

        return ResponseEntity.ok().body(pdfOutput);
    }

}
