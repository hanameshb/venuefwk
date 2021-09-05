package com.venue.rest.model;

import java.util.ArrayList;
import java.util.Hashtable;

public class DepthChartClubPositions 
{
	
	String position="";
    String displayPosition="";
    ArrayList<Hashtable<String, String>> depthChartClubPlayers=null;
    
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDisplayPosition() {
		return displayPosition;
	}
	public void setDisplayPosition(String displayPosition) {
		this.displayPosition = displayPosition;
	}
	public ArrayList<Hashtable<String, String>> getDepthChartClubPlayers() {
		return depthChartClubPlayers;
	}
	public void setDepthChartClubPlayers(
			ArrayList<Hashtable<String, String>> depthChartClubPlayers) {
		this.depthChartClubPlayers = depthChartClubPlayers;
	}
	

}
