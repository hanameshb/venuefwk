package com.venue.rest.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StandingsSectionData {
	
    private String sectionName = "";
    private ArrayList<StandingsKeyValues> keyValues = null;
    private ArrayList<LinkedHashMap<String, String>> sectionData = null;
    
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}	
	public ArrayList<StandingsKeyValues> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(ArrayList<StandingsKeyValues> keyValues) {
		this.keyValues = keyValues;
	}
	public ArrayList<LinkedHashMap<String, String>> getSectionData() {
		return sectionData;
	}
	public void setSectionData(ArrayList<LinkedHashMap<String, String>> sectionData) {
		this.sectionData = sectionData;
	}
}
