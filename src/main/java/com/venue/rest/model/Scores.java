package com.venue.rest.model;

import java.util.List;

public class Scores
{
	private Integer gameKey=0;
	private Boolean homeTeamHasPossession=false;
	private String	lastPlayDescription="";
	private Integer visitorTeamScore=0;
	private Integer homeTeamScore=0;
	private String	phase="";
	private Integer down=0;
	private Integer distance=0;
	private String	yardLine="";
	private String	clockTime="";
	private String	gameDate="";
	private String	startTime="";
	private String	startTimeET="";
	private String  homeClubCode="";
	private String  homeClubName="";
	private String  homeClubNickName="";
	private String  homeClubCityName="";
	private String  homePrimaryColor="";
	private String  homeSecondaryColor="";
	private String  homeTeamColor="";
	private String  visitorClubCode="";
	private String  visitorClubName="";
	private String  visitorClubNickName="";
	private String  visitorClubCityName="";
	private String  visitorPrimaryColor="";
	private String  visitorSecondaryColor="";
	private String  visitorTeamColor="";
	private String  leadingReceiver="";
	private String  leadingReceiverFName="";
	private String  leadingReceiverLName="";
	private String  leadingReceiverClubCode="";
	private String  leadingReceiverAttempts="";
	private String  leadingReceiverYards="";
	private String  leadingReceiverTouchdowns="";
	private String  leadingPasser="";
	private String  leadingPasserFName="";
	private String  leadingPasserLName="";
	private String  leadingPasserClubCode="";
	private String  leadingPasserCompletions="";
	private String  leadingPasserAttempts="";
	private String  leadingPasserYards="";
	private String  leadingPasserTouchdowns="";
	private String  leadingRusher="";
	private String  leadingRusherFName="";
	private String  leadingRusherLName="";
	private String  leadingRusherClubCode="";
	private String  leadingRusherAttempts="";
	private String  leadingRusherYards="";
	private String  leadingRusherTouchdowns="";
	private String  isGoalToGo="";
	private String  currentPlayType="";
	private String  scoreQuarter="";
	private String  scoreClockTime="";
	private String  ScoreClubCode="";
	private String  scoreDescription="";
	private String  homeQ1Score="";
	private String  homeQ2Score="";
	private String  homeQ3Score="";
	private String  homeQ4Score="";
	private String  homeOTScore="";
	private String  visitorQ1Score="";
	private String  visitorQ2Score="";
	private String  visitorQ3Score="";
	private String  visitorQ4Score="";
	private String  visitorOTScore="";
		
	private List<RushingLeader>  RushingLeader = null;
	private List<KickingLeader>  KickingLeader = null;
	private List<PassingLeader>  PassingLeader  = null;
	private List<ReceivingLeader> ReceiveLeader = null;
	private List<TeamDefence>  TeamDefense = null;
	
	public List<TeamDefence> getTeamDefense() {
		return TeamDefense;
	}
	public void setTeamDefense(List<TeamDefence> list) {
		TeamDefense = list;
	}
	public List<RushingLeader> getRushingLeader() {
		return RushingLeader;
	}
	public void setRushingLeader(List<RushingLeader> list) {
		RushingLeader = list;
	}
	public List<KickingLeader> getKickingLeader() {
		return KickingLeader;
	}
	public void setKickingLeader(List<KickingLeader> list) {
		KickingLeader = list;
	}
	public List<PassingLeader> getPassingLeader() {
		return PassingLeader;
	}
	public void setPassingLeader(List<PassingLeader> list) {
		PassingLeader = list;
	}
	public List<ReceivingLeader> getReceiveLeader() {
		return ReceiveLeader;
	}
	public void setReceiveLeader(List<ReceivingLeader> list) {
		ReceiveLeader = list;
	}
	
	public Integer getGameKey() {
		return gameKey;
	}
	public void setGameKey(Integer gameKey) {
		this.gameKey = gameKey;
	}
	public Boolean getHomeTeamHasPossession() {
		return homeTeamHasPossession;
	}
	public void setHomeTeamHasPossession(Boolean homeTeamHasPossession) {
		this.homeTeamHasPossession = homeTeamHasPossession;
	}
	public String getLastPlayDescription() {
		return lastPlayDescription;
	}
	public void setLastPlayDescription(String lastPlayDescription) {
		this.lastPlayDescription = lastPlayDescription;
	}
	public Integer getVisitorTeamScore() {
		return visitorTeamScore;
	}
	public void setVisitorTeamScore(Integer visitorTeamScore) {
		this.visitorTeamScore = visitorTeamScore;
	}
	public Integer getHomeTeamScore() {
		return homeTeamScore;
	}
	public void setHomeTeamScore(Integer homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public Integer getDown() {
		return down;
	}
	public void setDown(Integer down) {
		this.down = down;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public String getYardLine() {
		return yardLine;
	}
	public void setYardLine(String yardLine) {
		this.yardLine = yardLine;
	}
	public String getClockTime() {
		return clockTime;
	}
	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
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
	public String getStartTimeET() {
		return startTimeET;
	}
	public void setStartTimeET(String startTimeET) {
		this.startTimeET = startTimeET;
	}
	public String getHomeClubCode() {
		return homeClubCode;
	}
	public void setHomeClubCode(String homeClubCode) {
		this.homeClubCode = homeClubCode;
	}
	public String getHomeClubName() {
		return homeClubName;
	}
	public void setHomeClubName(String homeClubName) {
		this.homeClubName = homeClubName;
	}
	public String getHomeClubNickName() {
		return homeClubNickName;
	}
	public void setHomeClubNickName(String homeClubNickName) {
		this.homeClubNickName = homeClubNickName;
	}
	public String getHomeClubCityName() {
		return homeClubCityName;
	}
	public void setHomeClubCityName(String homeClubCityName) {
		this.homeClubCityName = homeClubCityName;
	}
	public String getHomePrimaryColor() {
		return homePrimaryColor;
	}
	public void setHomePrimaryColor(String homePrimaryColor) {
		this.homePrimaryColor = homePrimaryColor;
	}
	public String getHomeSecondaryColor() {
		return homeSecondaryColor;
	}
	public void setHomeSecondaryColor(String homeSecondaryColor) {
		this.homeSecondaryColor = homeSecondaryColor;
	}
	public String getHomeTeamColor() {
		return homeTeamColor;
	}
	public void setHomeTeamColor(String homeTeamColor) {
		this.homeTeamColor = homeTeamColor;
	}
	public String getVisitorClubCode() {
		return visitorClubCode;
	}
	public void setVisitorClubCode(String visitorClubCode) {
		this.visitorClubCode = visitorClubCode;
	}
	public String getVisitorClubName() {
		return visitorClubName;
	}
	public void setVisitorClubName(String visitorClubName) {
		this.visitorClubName = visitorClubName;
	}
	public String getVisitorClubNickName() {
		return visitorClubNickName;
	}
	public void setVisitorClubNickName(String visitorClubNickName) {
		this.visitorClubNickName = visitorClubNickName;
	}
	public String getVisitorClubCityName() {
		return visitorClubCityName;
	}
	public void setVisitorClubCityName(String visitorClubCityName) {
		this.visitorClubCityName = visitorClubCityName;
	}
	public String getVisitorPrimaryColor() {
		return visitorPrimaryColor;
	}
	public void setVisitorPrimaryColor(String visitorPrimaryColor) {
		this.visitorPrimaryColor = visitorPrimaryColor;
	}
	public String getVisitorSecondaryColor() {
		return visitorSecondaryColor;
	}
	public void setVisitorSecondaryColor(String visitorSecondaryColor) {
		this.visitorSecondaryColor = visitorSecondaryColor;
	}
	public String getVisitorTeamColor() {
		return visitorTeamColor;
	}
	public void setVisitorTeamColor(String visitorTeamColor) {
		this.visitorTeamColor = visitorTeamColor;
	}
	public String getLeadingReceiver() {
		return leadingReceiver;
	}
	public void setLeadingReceiver(String leadingReceiver) {
		this.leadingReceiver = leadingReceiver;
	}
	public String getLeadingReceiverFName() {
		return leadingReceiverFName;
	}
	public void setLeadingReceiverFName(String leadingReceiverFName) {
		this.leadingReceiverFName = leadingReceiverFName;
	}
	public String getLeadingReceiverLName() {
		return leadingReceiverLName;
	}
	public void setLeadingReceiverLName(String leadingReceiverLName) {
		this.leadingReceiverLName = leadingReceiverLName;
	}
	public String getLeadingReceiverClubCode() {
		return leadingReceiverClubCode;
	}
	public void setLeadingReceiverClubCode(String leadingReceiverClubCode) {
		this.leadingReceiverClubCode = leadingReceiverClubCode;
	}
	public String getLeadingReceiverAttempts() {
		return leadingReceiverAttempts;
	}
	public void setLeadingReceiverAttempts(String leadingReceiverAttempts) {
		this.leadingReceiverAttempts = leadingReceiverAttempts;
	}
	public String getLeadingReceiverYards() {
		return leadingReceiverYards;
	}
	public void setLeadingReceiverYards(String leadingReceiverYards) {
		this.leadingReceiverYards = leadingReceiverYards;
	}
	public String getLeadingReceiverTouchdowns() {
		return leadingReceiverTouchdowns;
	}
	public void setLeadingReceiverTouchdowns(String leadingReceiverTouchdowns) {
		this.leadingReceiverTouchdowns = leadingReceiverTouchdowns;
	}
	public String getLeadingPasser() {
		return leadingPasser;
	}
	public void setLeadingPasser(String leadingPasser) {
		this.leadingPasser = leadingPasser;
	}
	public String getLeadingPasserFName() {
		return leadingPasserFName;
	}
	public void setLeadingPasserFName(String leadingPasserFName) {
		this.leadingPasserFName = leadingPasserFName;
	}
	public String getLeadingPasserLName() {
		return leadingPasserLName;
	}
	public void setLeadingPasserLName(String leadingPasserLName) {
		this.leadingPasserLName = leadingPasserLName;
	}
	public String getLeadingPasserClubCode() {
		return leadingPasserClubCode;
	}
	public void setLeadingPasserClubCode(String leadingPasserClubCode) {
		this.leadingPasserClubCode = leadingPasserClubCode;
	}
	public String getLeadingPasserCompletions() {
		return leadingPasserCompletions;
	}
	public void setLeadingPasserCompletions(String leadingPasserCompletions) {
		this.leadingPasserCompletions = leadingPasserCompletions;
	}
	public String getLeadingPasserAttempts() {
		return leadingPasserAttempts;
	}
	public void setLeadingPasserAttempts(String leadingPasserAttempts) {
		this.leadingPasserAttempts = leadingPasserAttempts;
	}
	public String getLeadingPasserYards() {
		return leadingPasserYards;
	}
	public void setLeadingPasserYards(String leadingPasserYards) {
		this.leadingPasserYards = leadingPasserYards;
	}
	public String getLeadingPasserTouchdowns() {
		return leadingPasserTouchdowns;
	}
	public void setLeadingPasserTouchdowns(String leadingPasserTouchdowns) {
		this.leadingPasserTouchdowns = leadingPasserTouchdowns;
	}
	public String getLeadingRusher() {
		return leadingRusher;
	}
	public void setLeadingRusher(String leadingRusher) {
		this.leadingRusher = leadingRusher;
	}
	public String getLeadingRusherFName() {
		return leadingRusherFName;
	}
	public void setLeadingRusherFName(String leadingRusherFName) {
		this.leadingRusherFName = leadingRusherFName;
	}
	public String getLeadingRusherLName() {
		return leadingRusherLName;
	}
	public void setLeadingRusherLName(String leadingRusherLName) {
		this.leadingRusherLName = leadingRusherLName;
	}
	public String getLeadingRusherClubCode() {
		return leadingRusherClubCode;
	}
	public void setLeadingRusherClubCode(String leadingRusherClubCode) {
		this.leadingRusherClubCode = leadingRusherClubCode;
	}
	public String getLeadingRusherAttempts() {
		return leadingRusherAttempts;
	}
	public void setLeadingRusherAttempts(String leadingRusherAttempts) {
		this.leadingRusherAttempts = leadingRusherAttempts;
	}
	public String getLeadingRusherYards() {
		return leadingRusherYards;
	}
	public void setLeadingRusherYards(String leadingRusherYards) {
		this.leadingRusherYards = leadingRusherYards;
	}
	public String getLeadingRusherTouchdowns() {
		return leadingRusherTouchdowns;
	}
	public void setLeadingRusherTouchdowns(String leadingRusherTouchdowns) {
		this.leadingRusherTouchdowns = leadingRusherTouchdowns;
	}
	public String getIsGoalToGo() {
		return isGoalToGo;
	}
	public void setIsGoalToGo(String isGoalToGo) {
		this.isGoalToGo = isGoalToGo;
	}
	public String getCurrentPlayType() {
		return currentPlayType;
	}
	public void setCurrentPlayType(String currentPlayType) {
		this.currentPlayType = currentPlayType;
	}
	public String getScoreQuarter() {
		return scoreQuarter;
	}
	public void setScoreQuarter(String scoreQuarter) {
		this.scoreQuarter = scoreQuarter;
	}
	public String getScoreClockTime() {
		return scoreClockTime;
	}
	public void setScoreClockTime(String scoreClockTime) {
		this.scoreClockTime = scoreClockTime;
	}
	public String getScoreClubCode() {
		return ScoreClubCode;
	}
	public void setScoreClubCode(String scoreClubCode) {
		ScoreClubCode = scoreClubCode;
	}
	public String getScoreDescription() {
		return scoreDescription;
	}
	public void setScoreDescription(String scoreDescription) {
		this.scoreDescription = scoreDescription;
	}
	public String getHomeQ1Score() {
		return homeQ1Score;
	}
	public void setHomeQ1Score(String homeQ1Score) {
		this.homeQ1Score = homeQ1Score;
	}
	public String getHomeQ2Score() {
		return homeQ2Score;
	}
	public void setHomeQ2Score(String homeQ2Score) {
		this.homeQ2Score = homeQ2Score;
	}
	public String getHomeQ3Score() {
		return homeQ3Score;
	}
	public void setHomeQ3Score(String homeQ3Score) {
		this.homeQ3Score = homeQ3Score;
	}
	public String getHomeQ4Score() {
		return homeQ4Score;
	}
	public void setHomeQ4Score(String homeQ4Score) {
		this.homeQ4Score = homeQ4Score;
	}
	public String getHomeOTScore() {
		return homeOTScore;
	}
	public void setHomeOTScore(String homeOTScore) {
		this.homeOTScore = homeOTScore;
	}
	public String getVisitorQ1Score() {
		return visitorQ1Score;
	}
	public void setVisitorQ1Score(String visitorQ1Score) {
		this.visitorQ1Score = visitorQ1Score;
	}
	public String getVisitorQ2Score() {
		return visitorQ2Score;
	}
	public void setVisitorQ2Score(String visitorQ2Score) {
		this.visitorQ2Score = visitorQ2Score;
	}
	public String getVisitorQ3Score() {
		return visitorQ3Score;
	}
	public void setVisitorQ3Score(String visitorQ3Score) {
		this.visitorQ3Score = visitorQ3Score;
	}
	public String getVisitorQ4Score() {
		return visitorQ4Score;
	}
	public void setVisitorQ4Score(String visitorQ4Score) {
		this.visitorQ4Score = visitorQ4Score;
	}
	public String getVisitorOTScore() {
		return visitorOTScore;
	}
	public void setVisitorOTScore(String visitorOTScore) {
		this.visitorOTScore = visitorOTScore;
	}
	
	
	
}



