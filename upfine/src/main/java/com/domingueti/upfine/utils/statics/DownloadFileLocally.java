package com.domingueti.upfine.utils.statics;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileLocally {

    public static void execute(String url, String dest) throws IOException {
        URL website = new URL(url);
        try (InputStream in = website.openStream()) {
            Files.copy(in, Paths.get(dest));
        }
        catch (SocketException e) {
            throw new SocketException(e.getMessage());
        }
    }

}
