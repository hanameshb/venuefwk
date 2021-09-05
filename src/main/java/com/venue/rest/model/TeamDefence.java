package com.venue.rest.model;

public class TeamDefence
{
	private String   clubCode="";
	private Integer  interceptions=0;
	private Integer  sacks=0;
	private Integer  fumbleRecoveries=0;
		
	public String getClubCode() {
		return clubCode;
	}
	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}
	public Integer getInterceptions() {
		return interceptions;
	}
	public void setInterceptions(Integer interceptions) {
		this.interceptions = interceptions;
	}
	public Integer getSacks() {
		return sacks;
	}
	public void setSacks(Integer sacks) {
		this.sacks = sacks;
	}
	public Integer getFumbleRecoveries() {
		return fumbleRecoveries;
	}
	public void setFumbleRecoveries(Integer fumbleRecoveries) {
		this.fumbleRecoveries = fumbleRecoveries;
	}
	

	        
}



