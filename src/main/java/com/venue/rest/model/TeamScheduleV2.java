package com.venue.rest.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TeamScheduleV2 {

	private String week = "";
	private String gameDate = "";
	private String startTime = "";
	private String gameDateTime = "";
	private String ticketmasterUrl="";
	@JsonInclude(Include.NON_NULL)
	private HashMap<String, String> homeClub=null;
	@JsonInclude(Include.NON_NULL)
	private HashMap<String, String> visitClub=null;
	private String homeScore="";
	private String opponentScore="";


	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getGameDate() {
		return gameDate;
	}
	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getGameDateTime() {
		return gameDateTime;
	}
	public void setGameDateTime(String gameDateTime) {
		this.gameDateTime = gameDateTime;
	}
	public String getTicketmasterUrl() {
		return ticketmasterUrl;
	}
	public void setTicketmasterUrl(String ticketmasterUrl) {
		this.ticketmasterUrl = ticketmasterUrl;
	}
	public HashMap<String, String> getHomeClub() {
		return homeClub;
	}
	public void setHomeClub(HashMap<String, String> homeClub) {
		this.homeClub = homeClub;
	}
	public HashMap<String, String> getVisitClub() {
		return visitClub;
	}
	public void setVisitClub(HashMap<String, String> visitClub) {
		this.visitClub = visitClub;
	}
	public String getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(String homeScore) {
		this.homeScore = homeScore;
	}
	public String getOpponentScore() {
		return opponentScore;
	}
	public void setOpponentScore(String opponentScore) {
		this.opponentScore = opponentScore;
	}


}
