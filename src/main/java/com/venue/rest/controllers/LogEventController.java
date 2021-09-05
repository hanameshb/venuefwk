package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.LogEventDAO;
import com.venue.rest.model.LogEventModel;

@Controller
@RequestMapping("/v3")
public class LogEventController {
	
	Logger logger = Logger.getLogger(LogEventController.class);
	@Autowired
	LogEventDAO logEventDao;
	@RequestMapping(value="LogEventService", method=RequestMethod.POST)
	public @ResponseBody Object logEventService(@RequestBody LogEventModel logEventModel){
		logger.info("::in logEventService::");
		Object response = null;
		response = logEventDao.logEventService(logEventModel);
		return response;
	}
}