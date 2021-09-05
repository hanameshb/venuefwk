package com.venue.rest.controllers;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.EventDAO;

@Controller
public class EventController {
	
	private static Logger logger = Logger.getLogger(EventController.class);
	@Autowired
	private EventDAO eventDao;
	
	/**
	 * Get event list based on appId
	 */
	@RequestMapping(value="/EventList/{appId}", method=RequestMethod.GET)
	public @ResponseBody Object getEventList(@PathVariable int appId,@RequestParam(value="tagIds",required=false) String tagIds,
			@RequestParam(value="categoryIds",required=false) String categoryIds,@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="size",required=false,defaultValue="0") int size,@RequestParam(value="start_date", required=false) String startDate,
											 @RequestParam(value="stop_date", required=false) String stopDate){
		logger.info("::in getEventList::");
		Object response = null;
		if(appId == 8)
			response = eventDao.getEventList_8(appId,0,tagIds,categoryIds,page,size, startDate, stopDate);
		else
			response = eventDao.getEventList(appId,0,tagIds,categoryIds,page,size, startDate, stopDate);
		return response;
	}

	/**
	 * Get event list based on appId and venueId
	 */
	@RequestMapping(value="/EventList/{appId}/{venueId}", method=RequestMethod.GET)
	public @ResponseBody Object getEventList(@PathVariable int appId,@PathVariable int venueId,@RequestParam(value="tagIds",required=false) String tagIds,
			@RequestParam(value="categoryIds",required=false) String categoryIds,@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="size",required=false,defaultValue="0") int size, @RequestParam(value="start_date", required=false) String startDate,
											 @RequestParam(value="stop_date", required=false) String stopDate){
		logger.info("::in getEventList with VenuID::");
		Object response = null;
		if(appId == 8)
			response = eventDao.getEventList_8(appId,venueId,tagIds,categoryIds,page,size,startDate,stopDate);
		else
			response = eventDao.getEventList(appId,venueId,tagIds,categoryIds,page,size,startDate,stopDate);
		return response;
	}
	
	/**
	 * Get v2 event list based on appId and venueId
	 */
	@RequestMapping(value={"/v2/EventList/{appId}","/v2/EventList/{appId}/{venueId}"}, method=RequestMethod.GET)
	public @ResponseBody Object getV2EventList(@PathVariable int appId,@PathVariable Map<String, String> pathVariablesMap,@RequestParam(value="tagIds",required=false) String tagIds,
			@RequestParam(value="categoryIds",required=false) String categoryIds,@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="size",required=false,defaultValue="0") int size, @RequestParam(value="start_date", required=false) String startDate,
			@RequestParam(value="stop_date", required=false) String stopDate,@RequestParam(value="updated_since", required=false,defaultValue="0000-00-00 00:00:00") String updatedSince,
			@RequestParam(value="last_modified_date", required=false,defaultValue="0000-00-00 00:00:00") String lastModifiedDate){
		logger.info("::in getV2EventList::");
		Object response = null;
		logger.info("::pathVariablesMap::"+pathVariablesMap);
		
		if(pathVariablesMap.containsKey("venueId"))	{
			logger.info("::in getV2EventList with venueId::");
			response = eventDao.getV2EventList(appId,Integer.parseInt(pathVariablesMap.get("venueId")),"","","","",tagIds,categoryIds,page,size,startDate,stopDate,"",updatedSince,lastModifiedDate);
		}else{
			logger.info("::in getV2EventList without venueId::");
			response = eventDao.getV2EventList(appId,0,"","","","",tagIds,categoryIds,page,size,startDate,stopDate,"",updatedSince,lastModifiedDate);
		}
		return response;
	}
	
	/**
	 * Get singular event based on eventId or externalEventId1 or externalEventId2 or externalEventId3
	 */
	@RequestMapping(value="/EventLookUp/{appId}", method=RequestMethod.GET)
	public @ResponseBody Object EventLookUp(@PathVariable int appId,@RequestParam(value="eventId",required=false) String eventId,
			@RequestParam(value="externalEventId1",required=false) String externalEventId1, @RequestParam(value="externalEventId2",required=false) String externalEventId2,
			@RequestParam(value="externalEventId3",required=false) String externalEventId3){
		logger.info("::in EventLookUp::");
		Object response = null;
		response = eventDao.getEventLookUp(appId, eventId, externalEventId1, externalEventId2, externalEventId3);
		return response;
	}
	
	/**
	 * Get event tags list
	 */
	@RequestMapping(value="/tags/{appId}", method=RequestMethod.GET)
	public @ResponseBody Object getEventTagsList(@PathVariable int appId,@RequestParam(value="name",required=false) String name){
		logger.info("::in getEventTagList::");
		Object response = null;
		response = eventDao.getEventTagsList(appId,name);
		return response;
	}
	
	/**
	 * Get event categories list
	 */
	@RequestMapping(value="/categories/{appId}", method=RequestMethod.GET)
	public @ResponseBody Object getEventcategoriesList(@PathVariable int appId,@RequestParam(value="name",required=false) String name){
		logger.info("::in getEventTagList::");
		Object response = null;
		response = eventDao.getEventcategoriesList(appId,name);
		return response;
	}
	
	@RequestMapping(value={"clearcache/{appId}"},method=RequestMethod.GET,produces ="APPLICATION/JSON")
	public @ResponseBody Object clearcache(@PathVariable int appId){
		logger.info("::in clearcache::");
		Object response=null;
		if(appId == 8)
			response= eventDao.clearCache_8(appId);
		else
			response= eventDao.clearCache(appId);
		return response;
	}
	
	@RequestMapping(value={"clearV2EventCache/{appId}"},method=RequestMethod.GET,produces ="APPLICATION/JSON")
	public @ResponseBody Object clearV2EventCache(@PathVariable int appId){
		logger.info("::in clearV2EventCache::");
		Object response=null;
		response= eventDao.clearV2EventCache(appId);
		return response;
	}
}
