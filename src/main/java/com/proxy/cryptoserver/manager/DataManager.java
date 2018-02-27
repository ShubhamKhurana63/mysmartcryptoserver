package com.proxy.cryptoserver.manager;

import com.proxy.cryptoserver.exception.CryptoServerWebException;
import com.proxy.cryptoserver.web.response.CustomResponseWrapper;

public interface DataManager {

	 CustomResponseWrapper fetchDataForKey(String key) throws CryptoServerWebException;

}
