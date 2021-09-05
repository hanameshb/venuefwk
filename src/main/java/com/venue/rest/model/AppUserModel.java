package com.venue.rest.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AppUserModel {

	@JsonInclude(Include.NON_NULL)
	private String deviceKey;
	@JsonInclude(Include.NON_NULL)
	private HashMap<Object,Object> externalUserIds;
	@JsonInclude(Include.NON_NULL)
	private String appUserId;
	@JsonInclude(Include.NON_NULL)
	private ArrayList<HashMap<Object, Object>> userProfile;

	public String getDeviceKey() {
		return deviceKey;
	}
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}
	public HashMap<Object, Object> getExternalUserIds() {
		return externalUserIds;
	}
	public void setExternalUserIds(HashMap<Object, Object> externalUserIds) {
		this.externalUserIds = externalUserIds;
	}
	public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
	public ArrayList<HashMap<Object, Object>> getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(ArrayList<HashMap<Object, Object>> userProfile) {
		this.userProfile = userProfile;
	}


}
