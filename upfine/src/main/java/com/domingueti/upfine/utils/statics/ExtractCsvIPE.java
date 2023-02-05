package com.domingueti.upfine.utils.statics;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.Files.deleteIfExists;

public class ExtractCsvIPE {

    public static List<String[]> execute() throws IOException {

        String zipFileUrl = "https://dados.cvm.gov.br/dados/CIA_ABERTA/DOC/IPE/DADOS/ipe_cia_aberta_2023.zip";
        String zipFilePath = "ipe_cia_aberta_2023.zip";
        downloadFile(zipFileUrl, zipFilePath);

        // Extract the csv file from the zip file
        Path csvFilePath = Paths.get("src/main/resources/csv/ipe_cia_aberta_2023.csv");
        unzipFile(zipFilePath, csvFilePath);

        // Delete the zip file
        deleteIfExists(Paths.get(zipFilePath));

        String csvFile = "src/main/resources/csv/ipe_cia_aberta_2023.csv";
        String line = "";
        String cvsSplitBy = ";";
        List<String[]> rows = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[4].equals("Fato Relevante")) {
                    rows.add(row);
                }
            }
        } catch (
                IOException e) {
            deleteIfExists(csvFilePath);
            e.printStackTrace();
        }

        deleteIfExists(csvFilePath);

        return rows;
    }

    public static void downloadFile(String url, String dest) throws IOException {
        URL website = new URL(url);
        try (InputStream in = website.openStream()) {
            Files.copy(in, Paths.get(dest));
        }
        catch (SocketException e) {
            throw new SocketException(e.getMessage());
        }
    }

    private static void unzipFile(String zipFilePath, Path csvFilePath) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".csv")) {
                    try (FileOutputStream fos = new FileOutputStream(csvFilePath.toFile())) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

}
