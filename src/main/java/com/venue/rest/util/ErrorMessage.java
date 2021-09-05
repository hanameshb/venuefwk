/****************************************************************************
 *   Copyright (c)2013 eMbience. All rights reserved.
 *
 *   THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF eMbience.
 *
 *   The copyright notice above does not evidence any actual or intended
 *   publication of such source code.
 *****************************************************************************/
package com.venue.rest.util;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.venue.rest.model.SuccessMessage;
import com.venue.rest.model.ErrorMessages;

/**
 * ErrorMessage.java
 * Purpose: This class is responsible for returning error code and response in json format.
 * @author eMbience
 * @version 1.0
 */
public class ErrorMessage {
	
	private static ErrorMessage instance = null;
	//private JdbcTemplate jdbcTemplateObject;
	private static Logger logger = Logger.getLogger(ErrorMessage.class);
	//internal cache that holds all the error messages configured
	private static Map<String, String> errorList;
	
	public static ErrorMessage getInstance()
	{
		if(instance == null) {
			synchronized(ErrorMessage.class) {
				if(instance == null) {
					instance = new ErrorMessage();
				}
			}
		}
		return instance ;
	}
	
	public ErrorMessage()
	{
		try
		{
			//initializeDB();
			initializeMessages();
			
		}catch(Exception e)
		{
		  e.printStackTrace();	
		}
	}
	
	/**
	 * Method to get Error Message for given error code from table.
	 * @param errorCode
	 * @return String
	 */
	public String getErrorMessage(String errorCode)
	{
		String errorMessage = "";
    	//SqlRowSet rst = null;
		try
		{
			/*rst = jdbcTemplateObject.queryForRowSet("SELECT error_message from tbl_restapi_errormessages where error_id="+errorCode+"");
			if(rst.next())
			{	
				errorMessage = rst.getString("error_message");		
			}*/
			
			errorMessage = errorList.get(errorCode);
			if (errorMessage == null)
			{
				errorMessage = errorList.get("unknown");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return errorMessage;
	}
	
	/**
	 * Method to get error response object.
	 * @param errorCode
	 * @return error response object.
	 */
	public Object getErrorResponse(String errorCode)
	{
		String errorMessage = "";
		errorMessage = getErrorMessage(errorCode);
		if(errorCode.equalsIgnoreCase("200")){
			SuccessMessage successMessage=new SuccessMessage();
			successMessage.setResponseCode(errorCode);
			successMessage.setStatus(errorMessage);
			return successMessage;
		}else{
			ErrorMessages errorMessages = new ErrorMessages();
			errorMessages.setErrorCode(errorCode);
			errorMessages.setErrorMessage(errorMessage);
			return errorMessages;
		}
	}
	
	/**
	 * Method to get error response List.
	 * @param errorCode
	 * @return error response List.
	 */
	/*public ArrayList<Object> getErrorResponseList(String errorCode)
	{
		ArrayList ls = new ArrayList(); 
		String error_message="";
    	error_message = getErrorMessage(errorCode);
    	ErrorMessages errorMessages = new ErrorMessages();
    	errorMessages.setErrorCode(errorCode);
    	errorMessages.setErrorMessage(error_message);
    	ls.add(errorMessages);
    	return ls;
	}*/
	
	/*private void initializeDB() {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("dolphins_db.xml");
			DataSource dataSource = (DataSource) context.getBean("dataSourceDolphins");
			this.jdbcTemplateObject = new JdbcTemplate(dataSource);	
		} catch (Exception e) {
			logger.error("::Exception in initializing database::" +e);
		}
	}*/
	
	/*
	 * Loading all the error messages configured in dolphins_messages.xml
	 * 
	 */
	private void initializeMessages() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"venue_messages.xml"});
			errorList = (Map <String,String>) context.getBean("errorMessages");
			context.close();
			printMap(errorList);
		} catch (Exception e) {
			logger.error("::Exception in initializing error message::" +e);
		}
	}
	
	private static void printMap(Map<String, String> z) {
		
		System.out.println("---Printing Start----\n\n");
	    
	    for (Map.Entry<String, String> o : z.entrySet()) {
	    	System.out.println(o.getKey() + " => " + o.getValue().getClass() + " " + o.getValue());
	    }
	    
	    System.out.println("---Printing Complete----\n\n");
	}
}
