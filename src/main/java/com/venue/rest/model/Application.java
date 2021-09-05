package com.venue.rest.model;

public class Application {
	
	private long applicationId;
	private String applicationName;
	private String accountApiKey;
	
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getAccountApiKey() {
		return accountApiKey;
	}
	public void setAccountApiKey(String accountApiKey) {
		this.accountApiKey = accountApiKey;
	}
}