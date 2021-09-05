package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.StatsDAO;

@Controller
@RequestMapping("/stats")
public class StatsController {

	private static Logger logger = Logger.getLogger(StatsController.class);
	@Autowired
	StatsDAO statsDao;
	/**
	 * Method to get the team stats
	 */
	@RequestMapping(value={"getTeamStats"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getTeamStats()
	{
		logger.info("::in getTeamStats::");
		Object response = null;
		response = statsDao.getTeamStats();
		return response;
	}
	
	/**
	 * Method to get the player recent stats
	 */
	@RequestMapping(value={"getPlayerRecentStats"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getPlayerRecentStats(@RequestParam(value="nflgsisPlayerID", required=false) String nflgsisPlayerID)
	{
		logger.info("::in getPlayerRecentStats::");
		Object response = null;
		response = statsDao.getPlayerRecentStats(nflgsisPlayerID);
		return response;
	}
	
	/**
	 * Method to get the player historical stats
	 */
	@RequestMapping(value={"getPlayerHistoricalStats"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getPlayerHistoricalStats(@RequestParam(value="nflgsisPlayerID", required=false) String nflgsisPlayerID)
	{
		logger.info("::in getPlayerHistoricalStats::");
		Object response = null;
		response = statsDao.getPlayerHistoricalStats(nflgsisPlayerID);
		return response;
	}
}