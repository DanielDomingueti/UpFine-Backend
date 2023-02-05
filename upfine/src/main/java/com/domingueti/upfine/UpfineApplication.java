package com.domingueti.upfine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class UpfineApplication {
	public static void main(String[] args) throws NoSuchAlgorithmException {
//		System.setProperty("https.protocols", "SSLv3"); // with this I get javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
//		without I get java.net.SocketException: Connection reset
		SpringApplication.run(UpfineApplication.class, args);
		for (String s : SSLContext.getDefault().getSupportedSSLParameters().getProtocols()) {
			System.out.println(s);
		}
	}
}
