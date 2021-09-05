/****************************************************************************
 *   Copyright (c)2013 eMbience. All rights reserved.
 *
 *   THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF eMbience.
 *
 *   The copyright notice above does not evidence any actual or intended
 *   publication of such source code.
 *****************************************************************************/
package com.venue.rest.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * SocialStreamColorCodes.java
 * Purpose: This class is responsible for maintaining the getter and setter method for HomeStreamColorCodes.
 * @author eMbience
 * @version 1.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialStreamColorCodes 
{
	private String box2 = "";
	private String box3 = "";
	private String box4 = "";
	private String box5 = "";

	public String getBox2() {
		return box2;
	}
	public void setBox2(String box2) {
		this.box2 = box2;
	}
	public String getBox3() {
		return box3;
	}
	public void setBox3(String box3) {
		this.box3 = box3;
	}
	public String getBox4() {
		return box4;
	}
	public void setBox4(String box4) {
		this.box4 = box4;
	}
	public String getBox5() {
		return box5;
	}
	public void setBox5(String box5) {
		this.box5 = box5;
	}
	
			
}
