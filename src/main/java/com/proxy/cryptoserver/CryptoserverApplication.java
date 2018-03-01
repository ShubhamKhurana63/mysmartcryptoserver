package com.proxy.cryptoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoserverApplication.class, args);
	}
}
