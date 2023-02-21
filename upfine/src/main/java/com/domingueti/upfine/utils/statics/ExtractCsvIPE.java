package com.domingueti.upfine.utils.statics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.deleteIfExists;

public class ExtractCsvIPE {

    public static List<String[]> execute() throws IOException {
        String line = "";
        String cvsSplitBy = ";";
        List<String[]> rows = new ArrayList<>();

        String zipFileUrl = "https://dados.cvm.gov.br/dados/CIA_ABERTA/DOC/IPE/DADOS/ipe_cia_aberta_2023.zip";
        String zipFilePath = "src/main/resources/zip/ipe_cia_aberta_2023.zip";
        String csvFilePath = "src/main/resources/csv/ipe_cia_aberta_2023.csv";

        DownloadFileLocally.execute(zipFileUrl, zipFilePath);

        UnzipFileLocally.execute(zipFilePath, csvFilePath);

        deleteIfExists(Paths.get(zipFilePath));

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFilePath), "ISO-8859-1"));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[4].equals("Fato Relevante")) {
                    rows.add(row);
                }
            }
            br.close();

        } catch (IOException e) {
            deleteIfExists(Paths.get(csvFilePath));
            e.printStackTrace();
        }

        deleteIfExists(Paths.get(csvFilePath));
        return rows;
    }

}
