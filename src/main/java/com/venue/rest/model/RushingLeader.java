package com.venue.rest.model;

public class RushingLeader
{
	private String firstName="";
	private String lastName="";
	private String clubCode="";
	private String attempts="";
	private Integer yards=0;  
	private Integer longest=0;
	private Integer touchdowns=0;
	
	
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
	public String getAttempts() {
		return attempts;
	}
	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}
	public Integer getYards() {
		return yards;
	}
	public void setYards(Integer yards) {
		this.yards = yards;
	}
	public Integer getLongest() {
		return longest;
	}
	public void setLongest(Integer longest) {
		this.longest = longest;
	}
	public Integer getTouchdowns() {
		return touchdowns;
	}
	public void setTouchdowns(Integer touchdowns) {
		this.touchdowns = touchdowns;
	}
	
}



