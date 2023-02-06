package com.domingueti.upfine.configs;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import static java.security.KeyStore.getInstance;
import static org.apache.http.ssl.SSLContexts.custom;

@Configuration
public class WebConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        try {
            KeyStore keyStore = getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            FileInputStream fis = new FileInputStream("src/main/resources/ssl-certificate/certificate.pem");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("--")) {
                    builder.append(line);
                }
                line = reader.readLine();
            }

            byte[] certBytes = Base64.getDecoder().decode(builder.toString());
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
            keyStore.setCertificateEntry("cert", certificate);

            SSLContext sslContext = custom().loadTrustMaterial(keyStore, new TrustStrategyImpl(keyStore)).build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            return new RestTemplate(requestFactory);

        } catch (IOException | KeyManagementException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
