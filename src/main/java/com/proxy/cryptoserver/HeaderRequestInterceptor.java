package com.proxy.cryptoserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		System.out.println("inside interceptor===============");

		HttpServletRequest serletRequest = (HttpServletRequest) request;

		System.out.println("========================"+serletRequest.getRemoteAddr());

		request.getHeaders().set("Accept", MediaType.APPLICATION_JSON_VALUE);
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
		return execution.execute(request, body);
	}

}
