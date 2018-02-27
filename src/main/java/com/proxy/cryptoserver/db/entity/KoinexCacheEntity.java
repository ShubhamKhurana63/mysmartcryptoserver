package com.proxy.cryptoserver.db.entity;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "KOINEX_CACHE")
public class KoinexCacheEntity {
	@Id
	@Field(value = "KOINEX_CACHE_ID")
	private String id;

	@Field(value = "KOINEX_DATE_STAMP")
	private DateTime koinexDataTimeStamp;
	@Field(value = "KOINEX_DATA")
	private Object koinexData;

	public DateTime getKoinexDataTimeStamp() {
		return koinexDataTimeStamp;
	}

	public void setKoinexDataTimeStamp(DateTime koinexDataTimeStamp) {
		this.koinexDataTimeStamp = koinexDataTimeStamp;
	}

	public Object getKoinexData() {
		return koinexData;
	}

	public void setKoinexData(Object koinexData) {
		this.koinexData = koinexData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
