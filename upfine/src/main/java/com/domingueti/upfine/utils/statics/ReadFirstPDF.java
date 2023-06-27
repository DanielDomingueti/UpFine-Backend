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
