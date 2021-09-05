/****************************************************************************
 *   Copyright (c)2013 eMbience. All rights reserved.
 *
 *   THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF eMbience.
 *
 *   The copyright notice above does not evidence any actual or intended
 *   publication of such source code.
 *****************************************************************************/
package com.venue.rest.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * SocialStream.java
 * Purpose: This class is responsible for maintaining the getter and setter method for Aggregated Feed.
 * @author eMbience
 * @version 1.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialStreamV2 {

	private String until = "";
	private String since = "";
	private Integer noi=0;
	private String headlinetime="";
	private Boolean dataUpdated=false;


	public Integer getNoi() {
		return noi;
	}
	public void setNoi(Integer noi) {
		this.noi = noi;
	}
	private ArrayList<SocialStreamResponseV2> data = null;

	private ArrayList<Carousal> headlines = null;


	public ArrayList<Carousal> getHeadlines() {
		return headlines;
	}
	public void setHeadlines(ArrayList<Carousal> headlines) {
		this.headlines = headlines;
	}
	public ArrayList<SocialStreamResponseV2> getData() {
		return data;
	}
	public void setData(ArrayList<SocialStreamResponseV2> data) {
		this.data = data;
	}
	public String getUntil() {
		return until;
	}
	public void setUntil(String until) {
		this.until = until;
	}
	public String getSince() {
		return since;
	}
	public void setSince(String since) {
		this.since = since;
	}
	public String getHeadlinetime() {
		return headlinetime;
	}
	public void setHeadlinetime(String headlinetime) {
		this.headlinetime = headlinetime;
	}
	public Boolean getDataUpdated() {
		return dataUpdated;
	}
	public void setDataUpdated(Boolean dataUpdated) {
		this.dataUpdated = dataUpdated;
	}




}
