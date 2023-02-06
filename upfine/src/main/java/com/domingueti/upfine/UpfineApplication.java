package com.domingueti.upfine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class UpfineApplication {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(UpfineApplication.class, args);
	}
}
