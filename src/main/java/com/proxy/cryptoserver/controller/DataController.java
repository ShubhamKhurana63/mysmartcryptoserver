package com.proxy.cryptoserver.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.proxy.cryptoserver.exception.CryptoServerWebException;
import com.proxy.cryptoserver.manager.DataManager;
import com.proxy.cryptoserver.web.response.CustomResponseWrapper;

@Controller
public class DataController {

	@Resource
	DataManager dataManager;

	private static Logger LOGGER = Logger.getLogger(DataController.class);

	@RequestMapping(value = "/crypto/{cryptokey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomResponseWrapper> getCoindData(@PathVariable("cryptokey") String cryptokey)
			throws CryptoServerWebException {
		LOGGER.info("entering the " + "getCoindData" +"method");
		CustomResponseWrapper customResponseWrapper = null;
		System.out.println("=============================>>>>>>>>>>>>>>>" + cryptokey);
		customResponseWrapper = dataManager.fetchDataForKey(cryptokey);
		return new ResponseEntity<CustomResponseWrapper>(customResponseWrapper, HttpStatus.OK);
	}

}
