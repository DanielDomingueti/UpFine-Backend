package com.domingueti.upfine.configs;

import com.domingueti.upfine.modules.Config.services.GetConfigByNameService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class WebConfiguration {

    private GetConfigByNameService getConfigByNameService;

    @Bean
    public RestTemplate restTemplate() {
        final String SSL_CERTIFICATE_PATH = getConfigByNameService.execute("SSL-CERTIFICATE-PATH").getValue();
//        final String SSL_CERTIFICATE_PATH = "src/main/resources/ssl-certificate/certificate.pem";
        final String SSL_CERTIFICATE_TYPE = getConfigByNameService.execute("SSL-CERTIFICATE-TYPE").getValue();
//        final String SSL_CERTIFICATE_TYPE = "X.509";

        try {
            KeyStore keyStore = getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            FileInputStream fis = new FileInputStream(SSL_CERTIFICATE_PATH);
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
            CertificateFactory certificateFactory = CertificateFactory.getInstance(SSL_CERTIFICATE_TYPE);
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
