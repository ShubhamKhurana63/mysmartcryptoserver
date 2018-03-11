package com.proxy.cryptoserver.db.entity;

import java.util.List;

public class ExchangeKeyListEntity {

	private String key;

	private List<Object> dataList;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Object> getDataList() {
		return dataList;
	}

	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}

}
