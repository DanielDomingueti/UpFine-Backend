package com.domingueti.upfine.utils.statics;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileLocally {

    public static void execute(String url, String dest) {
        try {
            final URL website = new URL(url);
            final InputStream in = website.openStream();
            Files.copy(in, Paths.get(dest));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
