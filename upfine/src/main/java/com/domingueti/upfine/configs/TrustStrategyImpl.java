package com.domingueti.upfine.configs;

import org.apache.http.conn.ssl.TrustStrategy;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustStrategyImpl implements TrustStrategy {

    private final KeyStore keyStore;
    public TrustStrategyImpl(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @Override
    public boolean isTrusted(X509Certificate[] x509Certificates, String authType) {

        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            TrustManager[] trustManagers = tmf.getTrustManagers();
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    X509TrustManager x509TrustManager = (X509TrustManager) trustManager;
                    x509TrustManager.checkServerTrusted(x509Certificates, authType);
                }
            }
            return true;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }
}