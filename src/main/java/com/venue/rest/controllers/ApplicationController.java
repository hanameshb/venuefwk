package com.venue.rest.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.ApplicationDAO;
import com.venue.rest.model.Application;

@Controller
public class ApplicationController {
	private static Logger logger = LogManager.getLogger(ApplicationController.class);
	
	@Autowired
	ApplicationDAO applicationDAO;
	
	@RequestMapping(value="applications/v1", method = RequestMethod.GET)
	public @ResponseBody List<Application> fetchApplications() throws Exception {
		logger.info("::in fetchApplications::");
		return applicationDAO.fetchApplications();
	}
}
