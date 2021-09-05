package com.venue.rest.model;

import java.util.ArrayList;

public class DepthCharClubFormations
{

	String formation="";
	ArrayList<DepthChartClubPositions> depthChartClubPositions=null;
	public String getFormation() {
		return formation;
	}
	public void setFormation(String formation) {
		this.formation = formation;
	}
	public ArrayList<DepthChartClubPositions> getDepthChartClubPositions() {
		return depthChartClubPositions;
	}
	public void setDepthChartClubPositions(
			ArrayList<DepthChartClubPositions> depthChartClubPositions) {
		this.depthChartClubPositions = depthChartClubPositions;
	}
	
}
