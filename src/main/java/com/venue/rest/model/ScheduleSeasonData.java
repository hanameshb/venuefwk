package com.venue.rest.model;

import java.util.ArrayList;

public class ScheduleSeasonData {
	
	private String header = "";
	private ArrayList<TeamSchedule> data = null;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ArrayList<TeamSchedule> getData() {
		return data;
	}
	public void setData(ArrayList<TeamSchedule> data) {
		this.data = data;
	}
}
