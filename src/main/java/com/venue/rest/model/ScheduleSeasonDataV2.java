package com.venue.rest.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ScheduleSeasonDataV2 {

	@JsonInclude(Include.NON_EMPTY)
	private String header = "";
	@JsonInclude(Include.NON_NULL)
	private ArrayList<TeamScheduleV2> schedule = null;

	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ArrayList<TeamScheduleV2> getSchedule() {
		return schedule;
	}
	public void setSchedule(ArrayList<TeamScheduleV2> schedule) {
		this.schedule = schedule;
	}
}
