package com.venue.rest.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private ArrayList<HashMap<Object, Object>> userCurrentPlaces = null;
	private String appUserId = "";
	
	public ArrayList<HashMap<Object, Object>> getUserCurrentPlaces() {
		return userCurrentPlaces;
	}
	public void setUserCurrentPlaces(
			ArrayList<HashMap<Object, Object>> userCurrentPlaces) {
		this.userCurrentPlaces = userCurrentPlaces;
	}
	public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
}
