package com.venue.rest.model;

import java.sql.Timestamp;

public class KickingLeader
{
	private String  firstName="";
	private String  lastName="";
	private String  clubCode="";
	private Integer XPs=0;
	private Integer FGs=0;
	private Integer attempts=0;
	private Integer points=0;
	private String  fieldGoalYards=""; 
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getClubCode() {
		return clubCode;
	}
	public void setClubCode(String clubCode) {
		this.clubCode = clubCode;
	}
	public Integer getXPs() {
		return XPs;
	}
	public void setXPs(Integer xPs) {
		XPs = xPs;
	}
	public Integer getFGs() {
		return FGs;
	}
	public void setFGs(Integer fGs) {
		FGs = fGs;
	}
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public String getFieldGoalYards() {
		return fieldGoalYards;
	}
	public void setFieldGoalYards(String fieldGoalYards) {
		this.fieldGoalYards = fieldGoalYards;
	}
	
	

	
	
	
	        
}



