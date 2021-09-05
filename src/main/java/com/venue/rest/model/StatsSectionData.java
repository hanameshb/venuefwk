package com.venue.rest.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StatsSectionData {
	
    private String sectionName = "";
    private ArrayList<StatsKeyValues> keyValues = null;
    private ArrayList<LinkedHashMap<String, String>> sectionData = null;
    
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public ArrayList<StatsKeyValues> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(ArrayList<StatsKeyValues> keyValues) {
		this.keyValues = keyValues;
	}
	public ArrayList<LinkedHashMap<String, String>> getSectionData() {
		return sectionData;
	}
	public void setSectionData(ArrayList<LinkedHashMap<String, String>> sectionData) {
		this.sectionData = sectionData;
	}
}
