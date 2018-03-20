package com.proxy.cryptoserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.proxy.cryptoserver.db.entity.APIHitCachingModel;
import com.proxy.cryptoserver.db.entity.ExchangeKeyListEntity;
import com.proxy.cryptoserver.db.entity.KoinexCacheEntity;
import com.proxy.cryptoserver.exception.CryptoServerWebException;
import com.proxy.cryptoserver.utils.CryptoNameUrlPair;
import com.proxy.cryptoserver.utils.CryptoServerConstants;
import com.proxy.cryptoserver.utils.UrlConfig;

@Component
public class KoinexJobScheduler {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UrlConfig urlConfig;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	HttpEntity<String> httpEntity;

	/*
	 * @Scheduled(cron = "0 * * * * ?") public void reportCurrentTime() {
	 * System.out.
	 * println("========================job running every minute================");
	 * Object object = restTemplate.getForObject("https://koinex.in/api/ticker",
	 * Object.class); Boolean isUnderLength = checkCollectionLength();
	 * 
	 * if (!isUnderLength) { deleteData(); }
	 * 
	 * if (!ObjectUtils.isEmpty(object)) { KoinexCacheEntity koinexCacheEntity = new
	 * KoinexCacheEntity(); koinexCacheEntity.setKoinexData(object);
	 * koinexCacheEntity.setKoinexDataTimeStamp(DateTime.now());
	 * mongoTemplate.save(koinexCacheEntity); }
	 * 
	 * System.out.
	 * println("======================job running every minute======================"
	 * );
	 * 
	 * }
	 */

	/*
	 * @Scheduled(cron = "0 * * * * ?") public void parallelCachingMethod() throws
	 * CryptoServerWebException { CryptoNameUrlPair[] cryptoNameUrlPairArray =
	 * urlConfig.getCryptoNameUrlPairArray(); List<CryptoNameUrlPair>
	 * cryptoNameUrlPairList = Arrays.asList(cryptoNameUrlPairArray);
	 * List<CryptoNameUrlPair> cryptoNameUrlPair = null;
	 * validateCryptoNameUrlPairList(cryptoNameUrlPairList); Object object = null;
	 * APIHitCachingModel apiHitCachingModel = new APIHitCachingModel();
	 * List<ExchangeKeyListEntity> exchaneKeyListEntityList = new ArrayList();
	 * 
	 * for (String key : CryptoServerConstants.EXCHANGES_JOB) { cryptoNameUrlPair =
	 * cryptoNameUrlPairList.stream().filter(x -> x.getName().equalsIgnoreCase(key))
	 * .collect(Collectors.toList()); List<String> exceptionCoinList =
	 * Arrays.asList(CryptoServerConstants.EXCEPTION_COINS); if
	 * (exceptionCoinList.contains(key.toLowerCase())) {
	 * 
	 * object = restTemplate.exchange(cryptoNameUrlPair.get(0).getUrl(),
	 * HttpMethod.GET, httpEntity, Object.class); } else { object =
	 * restTemplate.getForObject(cryptoNameUrlPair.get(0).getUrl(), Object.class); }
	 * ExchangeKeyListEntity exchangeKeyListEntity = new ExchangeKeyListEntity();
	 * List<Object> objectList = new ArrayList<Object>(); objectList.add(object);
	 * exchangeKeyListEntity.setDataList(objectList);
	 * exchaneKeyListEntityList.add(exchangeKeyListEntity); }
	 * apiHitCachingModel.setExchangeKeylist(exchaneKeyListEntityList);
	 * 
	 * }
	 */
	@Scheduled(cron = "0 * * * * ?")
	private void persistCacheObject() throws CryptoServerWebException {
		System.out.println("<============================entered +persistCacheObject=============================>");
		CryptoNameUrlPair[] cryptoNameUrlPairArray = urlConfig.getCryptoNameUrlPairArray();
		List<CryptoNameUrlPair> cryptoNameUrlPairList = Arrays.asList(cryptoNameUrlPairArray);
		List<CryptoNameUrlPair> cryptoNameUrlPair = null;
		validateCryptoNameUrlPairList(cryptoNameUrlPairList);
		ResponseEntity<Object> responseEntity = null;
		APIHitCachingModel apiHitCachingModel = new APIHitCachingModel();
		List<ExchangeKeyListEntity> exchaneKeyListEntityList = new ArrayList<ExchangeKeyListEntity>();
		for (String key : CryptoServerConstants.EXCHANGES_JOB) {
			cryptoNameUrlPair = cryptoNameUrlPairList.stream().filter(x -> x.getName().equalsIgnoreCase(key))
					.collect(Collectors.toList());
			//List<String> exceptionCoinList = Arrays.asList(CryptoServerConstants.EXCEPTION_COINS);
				responseEntity = restTemplate.exchange(cryptoNameUrlPair.get(0).getUrl(), HttpMethod.GET, httpEntity,
						Object.class);
			ExchangeKeyListEntity exchangeKeyListEntity = new ExchangeKeyListEntity();
			List<Object> objectList = new ArrayList<Object>();
			if (responseEntity.hasBody()) {
				objectList.add(responseEntity.getBody());
			}
			exchangeKeyListEntity.setKey(key);
			exchangeKeyListEntity.setDataList(objectList);
			exchaneKeyListEntityList.add(exchangeKeyListEntity);
		}
		apiHitCachingModel.setExchangeKeylist(exchaneKeyListEntityList);
		List<APIHitCachingModel> apiCachingModel = mongoTemplate.findAll(APIHitCachingModel.class);
		if (!ObjectUtils.isEmpty(apiCachingModel)) {
			String objectId = apiCachingModel.get(0).getId();
			apiHitCachingModel.setId(objectId);
		}
		apiHitCachingModel.setDataTimeStamp(DateTime.now());
		mongoTemplate.save(apiHitCachingModel);
		System.out.println("<============================exited +persistCacheObject=============================>");
	}

	private void validateCryptoNameUrlPairList(List<CryptoNameUrlPair> cryptoNameUrlPairList)
			throws CryptoServerWebException {

		if (ObjectUtils.isEmpty(cryptoNameUrlPairList)) {
			throw new CryptoServerWebException("configration not found");
		}

	}

	private void deleteData() {
		System.out.println("==================delete the data==============");
		Query query = new Query();
		query.with(new Sort(new Order(Direction.ASC, "id")));
		List<KoinexCacheEntity> koinexCacheEntity = mongoTemplate.find(query, KoinexCacheEntity.class);
		mongoTemplate.remove(koinexCacheEntity.get(0));
		System.out.println("==================delete the data==============");
	}

	private Boolean checkCollectionLength() {
		System.out.println("=====================in checkCollectionLength======================");
		Boolean isUnderLength = true;
		Query query = new Query();
		long koinexCacheCount = mongoTemplate.count(query, KoinexCacheEntity.class);
		if (koinexCacheCount >= 5) {
			isUnderLength = false;
		}
		System.out.println("=====================" + koinexCacheCount);
		return isUnderLength;
	}

}
