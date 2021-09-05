package com.venue.rest.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LogEventModel {
	
	private String appUserId = "";
	private String eventCategory = "";
	private String eventType = "";
	private String eventValue = "";
	private String screenReference = "";
	private String sessionId = "";
	private String latitude = "";
	private String longitude = "";
	private ArrayList<HashMap<String, String>> places = null;
	
	public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventValue() {
		return eventValue;
	}
	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}
	public String getScreenReference() {
		return screenReference;
	}
	public void setScreenReference(String screenReference) {
		this.screenReference = screenReference;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public ArrayList<HashMap<String, String>> getPlaces() {
		return places;
	}
	public void setPlaces(ArrayList<HashMap<String, String>> places) {
		this.places = places;
	}
}
