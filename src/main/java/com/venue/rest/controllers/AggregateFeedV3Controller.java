package com.venue.rest.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.AggregateFeedV3DAO;
import com.venue.rest.model.SocialStreamV2;

@Controller
@RequestMapping("/v3")
public class AggregateFeedV3Controller {
	private static Logger logger = Logger.getLogger(AggregateFeedV3Controller.class);
	@Autowired
	AggregateFeedV3DAO aggregateFeedV3Dao;
	/*
	*method to get aggregate feeds with highlight videos
	*/
	@RequestMapping(value={"news"}, method = RequestMethod.POST, produces = "APPLICATION/JSON")
	public @ResponseBody Object getaggregatefeedTest(@RequestParam(value="appUserId", required=false) String appUserId,@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type,@RequestParam(value="headlinetime", required=false) String headlinetime,@RequestBody HashMap<String, ArrayList<HashMap<Object, Object>>> userCurrentPlaces)
	{
		logger.info("::in getaggregatefeedTest V3::");
		SocialStreamV2 homeStream=new SocialStreamV2();
		homeStream.setUntil(until);
		homeStream.setSince(since);
		homeStream.setNoi(noi);
		homeStream.setHeadlinetime(headlinetime);
		Object o =  aggregateFeedV3Dao.GetAggregateFeedV3Test(appUserId,homeStream,type,userCurrentPlaces);
		return o;
	}
}
