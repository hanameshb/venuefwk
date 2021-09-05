package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.AggregateFeedDAO;
import com.venue.rest.model.SocialStream;
@Controller
@RequestMapping("/aggregate")
public class AggregateFeedController {

	private static Logger logger = Logger.getLogger(AggregateFeedController.class);
	/**
	 * Method to Get the Aggregate Feed.
	 */
	@Autowired
	AggregateFeedDAO aggregateFeedDAO;
	
	@RequestMapping(value={"getaggregatedata"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
		public @ResponseBody Object getaggregatefeed(@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type,@RequestParam(value="headlinetime", required=false) String headlinetime) 
		{
			logger.info("::in getaggregatefeed:");
			SocialStream homeStream=new SocialStream();
			homeStream.setUntil(until);
			homeStream.setSince(since);
			homeStream.setNoi(noi);
			homeStream.setHeadlinetime(headlinetime);
			Object o =  aggregateFeedDAO.GetAggregateFeed(homeStream);	
			return o;
		}
}
