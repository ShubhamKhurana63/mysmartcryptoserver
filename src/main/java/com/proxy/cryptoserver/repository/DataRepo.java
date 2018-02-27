package com.proxy.cryptoserver.repository;

import java.util.List;

import com.proxy.cryptoserver.db.entity.KoinexCacheEntity;

public interface DataRepo {

	List<KoinexCacheEntity> getData();

}
