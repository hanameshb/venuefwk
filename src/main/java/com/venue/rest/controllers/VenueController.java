package com.venue.rest.controllers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.VenueDAO;
import com.venue.rest.model.Venues;



@Controller
public class VenueController {
	private static Logger logger = Logger.getLogger(VenueController.class);
	@Autowired
	VenueDAO venueDao;
	@RequestMapping(value="/venue/{appId}", method=RequestMethod.GET)
	public @ResponseBody ArrayList<Venues>  getVenues(@PathVariable int appId){
		logger.info("::in getVenues ::");
		ArrayList<Venues>  response=null;
		response=venueDao.getVenues(appId);
		
		return response;
	}
	
	@RequestMapping(value="/venue/{appId}/{venueId}", method=RequestMethod.GET)
	public @ResponseBody Venues  getVenuesByID(@PathVariable int appId,@PathVariable int venueId){
		logger.info("::in getVenuesByID ::");
		Venues  response=null;
		response=venueDao.getVenuesByID(appId,venueId);
		
		return response;
	}
}
