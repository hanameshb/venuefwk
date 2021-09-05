package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.AppUserDAO;
import com.venue.rest.model.AppUserModel;


@Controller
@RequestMapping("/v2")
public class AppUserController {
	private static Logger logger = Logger.getLogger(AppUserDAO.class);
	@Autowired
	AppUserDAO appUserDao;
	/**
	 * Controller to create app user
	 */
	@RequestMapping(value="appUsers",method=RequestMethod.POST)
	public @ResponseBody Object createAppUser(@RequestBody AppUserModel request){
		logger.info(":::in createAppUser:::");
		Object response=null;
		response=appUserDao.createUser(request);
		return response;
	}

	/**
	 * Controller to update app user details
	 */
	@RequestMapping(value="appUsers/{appUserId}",method=RequestMethod.PUT)
	public @ResponseBody Object updateAppUserDetails(@PathVariable String appUserId,@RequestBody AppUserModel request){
		logger.info(":::in updateAppUserDetails:::"+appUserId);
		Object response=null;
		response=appUserDao.updateUser(appUserId,request);
		return response;
	}

	/**
	 * Controller to Get app user details
	 */
	@RequestMapping(value="appUsers/{appUserId}",method=RequestMethod.GET)
	public @ResponseBody Object getAppUserDetails(@PathVariable String appUserId){
		logger.info(":::in getAppUserDetails:::"+appUserId);
		Object response=null;
		response=appUserDao.getAppUser(appUserId);
		return response;
	}



	/**
	 * Controller to Get external user details
	 */
	@RequestMapping(value="/appUsers/{appUserId}/externalUserIds/",method=RequestMethod.GET)
	public @ResponseBody Object getExternalUserIds(@PathVariable String appUserId){
		logger.info(":::in getExternalUserIds:::"+appUserId);
		Object response=null;
		response=appUserDao.getExternalUserIds(appUserId);
		return response;
	}

	/**
	 * Controller to Get external user details
	 */
	@RequestMapping(value="/appUsers/{appUserId}/userProfile/",method=RequestMethod.GET)
	public @ResponseBody Object getUserProfile(@PathVariable String appUserId){
		logger.info(":::in getUserProfile:::"+appUserId);
		Object response=null;
		response=appUserDao.getUserProfileDetail(appUserId);
		return response;
	}

	/**
	 * Controller to Get user favorite news feed
	 */
	@RequestMapping(value={"/appUsers/{appUserId}/favorites/news"}, method = RequestMethod.GET)
	public @ResponseBody Object getFavoritefeed(@PathVariable String appUserId)
	{
		logger.info(":::in getFavoritefeed:::"+appUserId);
		Object responce=null;
		responce=appUserDao.getFavoritefeedPosts(appUserId);
		return responce;
	}
}
