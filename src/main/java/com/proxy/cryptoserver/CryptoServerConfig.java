package com.proxy.cryptoserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.proxy.cryptoserver.utils.ConfigPuller;
import com.proxy.cryptoserver.utils.CryptoServerConstants;
import com.proxy.cryptoserver.utils.UrlConfig;

@Configuration
@ComponentScan(basePackages = "com.proxy.cryptoserver")
public class CryptoServerConfig {

	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> converter : messageConverters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				List<MediaType> m1 = new ArrayList<MediaType>();
				m1.add(MediaType.TEXT_HTML);
				m1.add(MediaType.APPLICATION_JSON);
				((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(m1);
			}
		}
		return restTemplate;
	}

	@Bean
	public UrlConfig getUrlConfig() {

		UrlConfig urlConfig = ConfigPuller.getConfig();
		return urlConfig;
	}

	@Bean
	public Mongo getMongoClient() {

		return new MongoClient(CryptoServerConstants.MONGO_HOST);
	}

	@Bean
	public MongoTemplate getMongoTemplate() {

		return new MongoTemplate(getMongoClient(), "SMARTCRYPTO_KOINEX_CACHE");
	}

}
