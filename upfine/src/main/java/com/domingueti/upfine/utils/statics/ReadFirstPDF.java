package com.domingueti.upfine.utils.statics;

import com.domingueti.upfine.exceptions.BusinessException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReadFirstPDF {

    public static String execute(String pdfFilePath) {

        try {
            final PDFTextStripper textStripper = new PDFTextStripper();

//            final File pdfDirectory = new File(pdfFilePath);
//            final File[] files = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//            if (files.length == 0 || files == null) {
//                throw new BusinessException("No PDF was found in: " + pdfFilePath);
//            }

            final File pdfFile = new File(pdfFilePath);
            PDDocument document = PDDocument.load(pdfFile);
            final String content = textStripper.getText(document);
            document.close();

            return content;

        } catch (IOException e) {
            throw new BusinessException("Error while reading PDF. Error: " + e.getMessage());
        }
    }
}
