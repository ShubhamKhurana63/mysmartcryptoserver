package com.proxy.cryptoserver.manager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.proxy.cryptoserver.db.entity.KoinexCacheEntity;
import com.proxy.cryptoserver.exception.CryptoServerWebException;
import com.proxy.cryptoserver.repository.DataRepo;
import com.proxy.cryptoserver.utils.CryptoNameUrlPair;
import com.proxy.cryptoserver.utils.CryptoServerConstants;
import com.proxy.cryptoserver.utils.UrlConfig;
import com.proxy.cryptoserver.web.response.CustomResponseWrapper;

@Component
public class DataManagerImpl implements DataManager {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UrlConfig urlConfig;

	@Autowired
	DataRepo dataRepo;

	@Override
	public CustomResponseWrapper fetchDataForKey(String key) throws CryptoServerWebException {
		CustomResponseWrapper customResponseWrapper = null;
		List<CryptoNameUrlPair> cryptoNameUrlPair = null;
		if (urlConfig != null) {
			CryptoNameUrlPair[] cryptoNameUrlPairArray = urlConfig.getCryptoNameUrlPairArray();
			List<CryptoNameUrlPair> cryptoNameUrlPairList = Arrays.asList(cryptoNameUrlPairArray);
			validateCryptoNameUrlPairList(cryptoNameUrlPairList);
			cryptoNameUrlPair = cryptoNameUrlPairList.stream().filter(x -> x.getName().equalsIgnoreCase(key))
					.collect(Collectors.toList());
			Object object = null;
			validateCryptoNameUrlPair(cryptoNameUrlPair);
			try {
				List<String> exceptionCoinList = Arrays.asList(CryptoServerConstants.EXCEPTION_COINS);
				if (exceptionCoinList.contains(key.toLowerCase())) {
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					headers.add(CryptoServerConstants.USER_AGENT, CryptoServerConstants.USER_AGENT_VALUE);
					HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
					object = restTemplate.exchange(cryptoNameUrlPair.get(0).getUrl(), HttpMethod.GET, entity,
							Object.class);
				} else if ("koinex".equals(key.toLowerCase())) {
					List<KoinexCacheEntity> koinexCacheDataList = dataRepo.getData();
					if (!ObjectUtils.isEmpty(koinexCacheDataList)) {
						KoinexCacheEntity koinexCacheEntity = koinexCacheDataList.get(koinexCacheDataList.size() - 1);
						object = koinexCacheEntity.getKoinexData();
					}

				} else {
					object = restTemplate.getForObject(cryptoNameUrlPair.get(0).getUrl(), Object.class);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				throw new CryptoServerWebException(ex.getMessage());
			}
			customResponseWrapper = new CustomResponseWrapper();
			customResponseWrapper.setResponseCode(200);
			customResponseWrapper.setResponseMessage("data fetched successfully for " + key);
			customResponseWrapper.setData(object);
		} else {
			throw new CryptoServerWebException("configuration cannot be pulled");
		}
		return customResponseWrapper;
	}

	private void validateCryptoNameUrlPairList(List<CryptoNameUrlPair> cryptoNameUrlPairList)
			throws CryptoServerWebException {

		if (ObjectUtils.isEmpty(cryptoNameUrlPairList)) {
			throw new CryptoServerWebException("configration not found");
		}

	}

	private void validateCryptoNameUrlPair(List<CryptoNameUrlPair> cryptoNameUrlPair) throws CryptoServerWebException {
		if (ObjectUtils.isEmpty(cryptoNameUrlPair)) {
			throw new CryptoServerWebException("exchange name not valid");
		}
	}

}
