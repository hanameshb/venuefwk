package com.venue.rest.model;

public class TicketsAccountConfig {
	
	private String client_id = "";
	private String dsn = "";
	private String tm_sdk_api_key = "";
	private String ios_sdk_api_key = "";
	private String android_sdk_api_key = "";
	private String socialShareText = "";
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getDsn() {
		return dsn;
	}
	public void setDsn(String dsn) {
		this.dsn = dsn;
	}
	public String getTm_sdk_api_key() {
		return tm_sdk_api_key;
	}
	public void setTm_sdk_api_key(String tm_sdk_api_key) {
		this.tm_sdk_api_key = tm_sdk_api_key;
	}
	public String getSocialShareText() {
		return socialShareText;
	}
	public void setSocialShareText(String socialShareText) {
		this.socialShareText = socialShareText;
	}
	public String getIos_sdk_api_key() {
		return ios_sdk_api_key;
	}
	public void setIos_sdk_api_key(String ios_sdk_api_key) {
		this.ios_sdk_api_key = ios_sdk_api_key;
	}
	public String getAndroid_sdk_api_key() {
		return android_sdk_api_key;
	}
	public void setAndroid_sdk_api_key(String android_sdk_api_key) {
		this.android_sdk_api_key = android_sdk_api_key;
	}
	
}
