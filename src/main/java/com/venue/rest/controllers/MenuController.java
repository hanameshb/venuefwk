package com.venue.rest.controllers;

import com.venue.rest.dao.MenuDAO;
import com.venue.rest.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v5")
public class MenuController {

	private static Logger logger = Logger.getLogger(MenuController.class);
	private @Value("#{props1[segmentsUrl]}") String segmentsUrl;
	@Autowired
	MenuDAO menuDao;
	/**
	 * Controller to get menu list
	 */
	@RequestMapping(value="/MenuList/{appId}/{venueId}", method=RequestMethod.GET)
	public @ResponseBody Object getMenuList(@RequestParam(value="appUserId",required=false) String appUserId,@PathVariable String appId,@PathVariable String venueId){
		logger.info("::in getMenuList::");
		Object responce = null;
		responce = menuDao.getMenuList(appUserId,appId,venueId);
		return responce;
	}
	
	/**
	 * Controller to get menu list
	 */
	@RequestMapping(value="/MenuList/{appId}/{venueId}", method=RequestMethod.POST)
	public @ResponseBody Object getMenuList(@PathVariable String appId, @PathVariable String venueId, @RequestBody User user){
		logger.info("::in getMenuList::");
		Object response = null;
		response = menuDao.getMenuList(user.getAppUserId(), appId, venueId, user.getUserCurrentPlaces(), segmentsUrl);
		return response;
	}
	
	/**
	 * Controller to get menu list
	 */
	@RequestMapping(value="/MenuList/{appId}/{venueId}/{menuId}", method=RequestMethod.POST)
	public @ResponseBody Object getMenuList(@PathVariable String appId, @PathVariable String venueId, @PathVariable String menuId, @RequestBody User user){
		logger.info("::in getMenuList::");
		Object response = null;
		response = menuDao.getMenuList(user.getAppUserId(), appId, venueId, menuId, user.getUserCurrentPlaces(), segmentsUrl);
		return response;
	}
}
