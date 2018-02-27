package com.proxy.cryptoserver.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.proxy.cryptoserver.db.entity.KoinexCacheEntity;
@Component
public class DataRepoImpl implements DataRepo {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<KoinexCacheEntity> getData() {
		List<KoinexCacheEntity> koinexCacheEntityList = mongoTemplate.findAll(KoinexCacheEntity.class);
		return koinexCacheEntityList;
	}

}
