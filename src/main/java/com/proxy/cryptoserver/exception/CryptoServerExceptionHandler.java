package com.proxy.cryptoserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.proxy.cryptoserver.web.response.CustomResponseWrapper;

@ControllerAdvice
public class CryptoServerExceptionHandler {

	@ExceptionHandler(value = CryptoServerWebException.class)
	public ResponseEntity<CustomResponseWrapper> getExceptionResponse(Exception ex) {
		CustomResponseWrapper customResponseWrapper = new CustomResponseWrapper();
		customResponseWrapper.setResponseCode(500);
		customResponseWrapper.setResponseMessage(ex.getMessage());
		customResponseWrapper.setData(null);
		return new ResponseEntity<CustomResponseWrapper>(customResponseWrapper, HttpStatus.OK);
	}

}
