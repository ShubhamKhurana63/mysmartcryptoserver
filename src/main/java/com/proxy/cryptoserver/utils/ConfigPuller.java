package com.proxy.cryptoserver.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigPuller {

	private static UrlConfig urlConfig;

	private ConfigPuller() {

	}

	public static UrlConfig getConfig() {
		if (urlConfig == null) {
			synchronized (ConfigPuller.class) {
				if (urlConfig == null) {
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						urlConfig = objectMapper.readValue(classLoader.getResourceAsStream("requestconfig.json"),
								UrlConfig.class);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}

		}
		return urlConfig;
	}
}
