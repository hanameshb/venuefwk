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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * HomeStream.java
 * Purpose: This class is responsible for maintaining the getter and setter method for HomeStream.
 * @author eMbience
 * @version 1.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheerLeadersAggregatedFeedRequest {

	private String until = "";
	private String since = "";	
	private Integer noi=0;
		
	public Integer getNoi() {
		return noi;
	}
	public void setNoi(Integer noi) {
		this.noi = noi;
	}
	private ArrayList<CheerLeadersAggregatedResponse> data = null;
	public ArrayList<CheerLeadersAggregatedResponse> getData() {
		return data;
	}
	public void setData(ArrayList<CheerLeadersAggregatedResponse> data) {
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
			
}
