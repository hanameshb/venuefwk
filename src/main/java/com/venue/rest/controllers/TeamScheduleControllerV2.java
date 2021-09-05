package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.TeamScheduleDAO;


@Controller
@RequestMapping("/v2")
public class TeamScheduleControllerV2 {
	private static Logger logger = Logger.getLogger(TeamScheduleControllerV2.class);
	private  @Value("#{props[MIALOGO]}") String miaLogo;

	@Autowired
	TeamScheduleDAO teamScheduleDao;
	/**
	 * Method to get the team schedule V2
	 */
	@RequestMapping(value={"teamschedule"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getScheduleV2()
	{
		logger.info("::in getSchedule::");
		Object response = null;
		response = teamScheduleDao.getTeamScheduleV2(miaLogo);
		return response;
	}

}
