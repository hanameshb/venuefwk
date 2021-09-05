package com.venue.rest.model;

import java.util.ArrayList;

public class VenueV2 {
	private int venueId = 0;
	private String venueName = "";
	private ArrayList<Integer> emkitLocationId = null;
	private String gpsLatitude = "";
	private String gpsLongitude = "";
	private String venueAddress ="";
	ArrayList<VenuePlaces> placeNames = null;
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public ArrayList<Integer> getEmkitLocationId() {
		return emkitLocationId;
	}
	public void setEmkitLocationId(ArrayList<Integer> emkitLocationId) {
		this.emkitLocationId = emkitLocationId;
	}
	public String getGpsLatitude() {
		return gpsLatitude;
	}
	public void setGpsLatitude(String gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}
	public String getGpsLongitude() {
		return gpsLongitude;
	}
	public void setGpsLongitude(String gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}
	public String getVenueAddress() {
		return venueAddress;
	}
	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}
	public ArrayList<VenuePlaces> getPlaceNames() {
		return placeNames;
	}
	public void setPlaceNames(ArrayList<VenuePlaces> placeNames) {
		this.placeNames = placeNames;
	}
	
}
