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
 * ErrorMessages.java
 * Purpose: This class is responsible for maintaining the getter and setter method for Error Messages.
 * @author eMbience
 * @version 1.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessages {
	
	private String errorCode = "";
	private String errorMessage = "";
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
