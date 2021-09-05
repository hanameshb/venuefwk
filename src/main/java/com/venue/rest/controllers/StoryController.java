package com.venue.rest.controllers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.StoryDAO;
import com.venue.rest.model.VenueStory;


@Controller
public class StoryController {
	private static Logger logger = Logger.getLogger(StoryController.class);
	@Autowired
	StoryDAO storyDao;
	@RequestMapping(value="/{type}/{emkitAPIKey}", method=RequestMethod.GET)
	public @ResponseBody ArrayList<VenueStory> getStories(@PathVariable(value = "type") String type,@PathVariable String emkitAPIKey,
			@RequestParam (value="category", required=false)String category,@RequestParam (value="published_age", required=false,defaultValue="0")int published_age,
			@RequestParam (value="content_type", required=false)String content_type){
		logger.info("::in getStories ::");
		ArrayList<VenueStory> response=null;
		try{
			response=storyDao.getStories(type,emkitAPIKey,category,published_age,content_type);
		}catch(Exception e){
			logger.error("exception in getStories",e);
		}
		return response;
	}
	@RequestMapping(value="/V2/{type}/{emkitAPIKey}", method=RequestMethod.GET)
	public @ResponseBody Object getStories(@PathVariable(value = "type") String type,@PathVariable String emkitAPIKey,
			@RequestParam (value="category", required=false)String category,@RequestParam (value="published_age", required=false,defaultValue="0")int published_age,
			@RequestParam (value="content_type", required=false)String content_type,@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="size",required=false,defaultValue="0") int size,@RequestParam(value="start_publish_date", required=false) String startDate,
			@RequestParam(value="end_publish_date", required=false) String stopDate,@RequestParam(value="updated_since", required=false,defaultValue="0000-00-00 00:00:00") String updatedSince){
		logger.info("::in getStories V2::");
		Object response=null;
		try{
			response=storyDao.getStoriesV2(type,emkitAPIKey,category,published_age,content_type,startDate,stopDate,page,size,updatedSince);
		}catch(Exception e){
			logger.error("exception in getStories",e);
		}
		return response;
	}
	
	@RequestMapping(value="/{type}/{emkitAPIKey}/{story_id}", method=RequestMethod.GET)
	public @ResponseBody VenueStory getStoriesByID(@PathVariable(value = "type") String type,@PathVariable String emkitAPIKey,@PathVariable int story_id){
		logger.info("::in getStoriesByID ::");
		VenueStory response=null;
		response=storyDao.getStoriesByID(type,emkitAPIKey,story_id);
		
		return response;
	}
}
