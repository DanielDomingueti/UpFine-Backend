package com.domingueti.upfine.utils.statics;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadFileLocally {

    public static void execute(String url, String dest) {
        try {
            final HttpClient httpClient = HttpClientBuilder.create().build();
            final HttpGet request = new HttpGet(url);
            final HttpResponse response = httpClient.execute(request);
            final HttpEntity entity = response.getEntity();

            if (entity != null) {
                final InputStream inputStream = entity.getContent();
                Files.copy(inputStream, Path.of(dest), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
