package com.venue.rest.controllers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.DepthChartDAO;
import com.venue.rest.dao.TeamStandingsDAO;
@Controller
@RequestMapping("/standings")
public class StandingsController {

	private static Logger logger = Logger.getLogger(StandingsController.class);
	/**
	 * Method to Get the Team Standings.
	 * @throws IOException 
	 */
	@Autowired
	TeamStandingsDAO  teamStandingsDao;
	@RequestMapping(value={"getstandings"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getstandings() throws IOException 
	{
		logger.info("::in getstandings:");
		Object o = null;
		o = teamStandingsDao.getTeamStandings();
		return o;		

	}
	
	
	
}
