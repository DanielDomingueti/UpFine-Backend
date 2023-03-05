package com.domingueti.upfine.utils.components;

import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import com.domingueti.upfine.utils.statics.DownloadFileLocally;
import com.domingueti.upfine.utils.statics.UnzipFileLocally;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;

@Component
@AllArgsConstructor
public class ExtractCsvLines {

    private GetConfigByNameService getConfigByNameService;

    public List<String[]> execute() throws IOException {
        final String ZIP_FILE_URL = getConfigByNameService.execute("ZIP-FILE-URL").getValue();
        final String ZIP_FILE_PATH_STR = getConfigByNameService.execute("ZIP-FILE-PATH-STR").getValue();
        final String CSV_FILE_PATH_STR = getConfigByNameService.execute("CSV-FILE-PATH-STR").getValue();
        final String CHARSET_PATTERN = getConfigByNameService.execute("CHARSET-PATTERN").getValue();

        DownloadFileLocally.execute(ZIP_FILE_URL, ZIP_FILE_PATH_STR);

        UnzipFileLocally.execute(ZIP_FILE_PATH_STR, CSV_FILE_PATH_STR);
        deleteIfExists(get(ZIP_FILE_PATH_STR));

        return getExtractedCsvLines(CSV_FILE_PATH_STR, CHARSET_PATTERN);

    }

    private List<String[]> getExtractedCsvLines(String CSV_FILE_PATH_STR, String CHARSET_PATTERN) throws IOException {
        final Path CSV_FILE = get(CSV_FILE_PATH_STR);
        final String cvsSplitBy = ";";
        String line = "";
        List<String[]> rows = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH_STR), CHARSET_PATTERN));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if (row[4].equals("Fato Relevante")) {
                    rows.add(row);
                }
            }
            br.close();

        } catch (IOException e) {
            deleteIfExists(CSV_FILE);
            throw new RuntimeException(e);
        }
        deleteIfExists(CSV_FILE);
        return rows;
    }

}
