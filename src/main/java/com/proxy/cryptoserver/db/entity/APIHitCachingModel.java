package com.proxy.cryptoserver.db.entity;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "EXCHANGE_DATA_CACHE")
public class APIHitCachingModel {

	@Id
	@Field(value = "KOINEX_CACHE_ID")
	private String id;

	private DateTime dataTimeStamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getDataTimeStamp() {
		return dataTimeStamp;
	}

	public void setDataTimeStamp(DateTime dataTimeStamp) {
		this.dataTimeStamp = dataTimeStamp;
	}

	private List<ExchangeKeyListEntity> exchangeKeylist;

	public List<ExchangeKeyListEntity> getExchangeKeylist() {
		return exchangeKeylist;
	}

	public void setExchangeKeylist(List<ExchangeKeyListEntity> exchangeKeylist) {
		this.exchangeKeylist = exchangeKeylist;
	}

}
