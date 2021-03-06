package com.proxy.cryptoserver;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.proxy.cryptoserver.db.entity.KoinexCacheEntity;
import com.proxy.cryptoserver.utils.UrlConfig;

@Component
public class KoinexJobScheduler {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UrlConfig urlConfig;

	@Autowired
	MongoTemplate mongoTemplate;

	//@Scheduled(cron = "0 * * * * ?")
	public void reportCurrentTime() {
		System.out.println("========================job running every minute================");
		Object object = restTemplate.getForObject("https://koinex.in/api/ticker", Object.class);
		Boolean isUnderLength = checkCollectionLength();

		if (!isUnderLength) {
			deleteData();
		}

		if (!ObjectUtils.isEmpty(object)) {
			KoinexCacheEntity koinexCacheEntity = new KoinexCacheEntity();
			koinexCacheEntity.setKoinexData(object);
			koinexCacheEntity.setKoinexDataTimeStamp(DateTime.now());
			mongoTemplate.save(koinexCacheEntity);
		}

		System.out.println("======================job running every minute======================");

	}

	private void deleteData() {
		System.out.println("==================delete the data==============");
		Query query = new Query();
		mongoTemplate.remove(query, KoinexCacheEntity.class);
		System.out.println("==================delete the data==============");
	}

	private Boolean checkCollectionLength() {
		System.out.println("=====================in checkCollectionLength======================");
		Boolean isUnderLength = true;
		Query query = new Query();
		long koinexCacheCount = mongoTemplate.count(query, KoinexCacheEntity.class);
		if (koinexCacheCount > 5) {
			isUnderLength = false;
		}
		System.out.println("=====================" + koinexCacheCount);
		return isUnderLength;
	}

}
