package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.TeamScheduleDAO;

@Controller
@RequestMapping("/team")
public class TeamScheduleController {

	private static Logger logger = Logger.getLogger(TeamScheduleController.class);
	@Autowired
	TeamScheduleDAO teamScheduleDao;
	/**
	 * Method to get the team schedule
	 */
	@RequestMapping(value={"getSchedule"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getSchedule()
	{
		logger.info("::in getSchedule::");
		Object response = null;
		response = teamScheduleDao.getTeamSchedule();
		return response;
	}
	
	/**
	 * Method to get current game day URL
	 */
	@RequestMapping(value={"getCurrentGameDayURL"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getCurrentGameDayURL()
	{
		logger.info("::in getCurrentGameDayURL::");
		Object response = null;
		response = teamScheduleDao.getCurrentGameDayURL();
		return response;
	}
}