package com.venue.rest.model;

import java.sql.Timestamp;
public class PassingLeader
{
private String firstName="";
private String  lastName="";
private String  clubCode="";
private Integer completions=0;
private Integer attempts=0;
private Integer yards=0;
private Integer longest=0;
private Integer touchdowns=0;
private Integer sacks=0;
private Integer sackYards=0;
private Integer interceptions=0;
private Integer QBRating=0;


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
public Integer getCompletions() {
	return completions;
}
public void setCompletions(Integer completions) {
	this.completions = completions;
}
public Integer getAttempts() {
	return attempts;
}
public void setAttempts(Integer attempts) {
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
public Integer getSacks() {
	return sacks;
}
public void setSacks(Integer sacks) {
	this.sacks = sacks;
}
public Integer getSackYards() {
	return sackYards;
}
public void setSackYards(Integer sackYards) {
	this.sackYards = sackYards;
}
public Integer getInterceptions() {
	return interceptions;
}
public void setInterceptions(Integer interceptions) {
	this.interceptions = interceptions;
}
public Integer getQBRating() {
	return QBRating;
}
public void setQBRating(Integer qBRating) {
	QBRating = qBRating;
}

	
}



