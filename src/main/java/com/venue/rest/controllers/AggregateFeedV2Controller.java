package com.venue.rest.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.AggregateFeedV2DAO;
import com.venue.rest.model.SocialStream;
import com.venue.rest.model.SocialStreamResponseV2;
import com.venue.rest.model.SocialStreamV2;


@Controller
@RequestMapping("/v2")
public class AggregateFeedV2Controller {

	private static Logger logger = Logger.getLogger(AggregateFeedV2Controller.class);
	/**
	 * Method to Get the Aggregate Feed.
	 */

	@Autowired
	AggregateFeedV2DAO aggregateFeedV2Dao;
	/*@RequestMapping(value={"news"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
		public @ResponseBody Object getaggregatefeed(@RequestParam(value="appUserId", required=false) String appUserId,@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type,@RequestParam(value="headlinetime", required=false) String headlinetime)
		{
			logger.info("::in getaggregatefeed:");
			AggregateFeedV2DAO dao = AggregateFeedV2DAO.getInstance();
			SocialStreamV2 homeStream=new SocialStreamV2();
			homeStream.setUntil(until);
			homeStream.setSince(since);
			homeStream.setNoi(noi);
			homeStream.setHeadlinetime(headlinetime);
			Object o =  dao.GetAggregateFeedV2(appUserId,homeStream,type);
			return o;
		}*/
	
	@RequestMapping(value={"aggregate/getaggregatedata"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getaggregatefeed(@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type,@RequestParam(value="headlinetime", required=false) String headlinetime) 
	{
		logger.info("::in getaggregatedata::");
		SocialStream homeStream=new SocialStream();
		homeStream.setUntil(until);
		homeStream.setSince(since);
		homeStream.setNoi(noi);
		homeStream.setHeadlinetime(headlinetime);
		Object o =  aggregateFeedV2Dao.GetAggregateFeed(homeStream);	
		return o;
	}

	/**
	 * Method to Get the Feed by Post ID.
	 */
	@RequestMapping(value={"news/{postType}/{postId}"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getfeedForPost(@PathVariable String postType,@PathVariable int postId)
	{
		logger.info("::in getfeedForPost::");
		Object o =  aggregateFeedV2Dao.GetFeedForPostV2(postId,postType);
		return o;
	}

	/**
	 * Method to delete the Feed by Post ID.
	 */
	@RequestMapping(value={"news/{postType}/{postId}"}, method = RequestMethod.DELETE, produces = "APPLICATION/JSON")
	public @ResponseBody Object getaggregatefeed(@PathVariable String postType,@PathVariable int postId)
	{
		logger.info("::in getaggregatefeed::");
		Object responce=null;
		responce=aggregateFeedV2Dao.deletePost(postId,postType);
		return responce;
	}

	/**
	 * Method to update the Feed by Post ID.
	 */
	@RequestMapping(value={"news/{postType}/{postId}/{appUserId}"}, method = RequestMethod.PUT, produces = "APPLICATION/JSON")
	public @ResponseBody Object updatefeed(@PathVariable String postType,@PathVariable int postId,@PathVariable String appUserId,@RequestBody SocialStreamResponseV2 post)
	{
		logger.info("::in updatefeed::");
		Object responce=null;
		responce=aggregateFeedV2Dao.updatePost(postId,postType,appUserId,post);
		return responce;
	}


	/**
	 * Method to update the Feed favorite by Post ID.
	 */
	@RequestMapping(value={"appusers/{appUserId}/favorites/news/{postType}/{postId}"}, method = RequestMethod.PUT, produces = "APPLICATION/JSON")
	public @ResponseBody Object updateFavoritefeed(@PathVariable String appUserId,@PathVariable String postType,@PathVariable int postId,@RequestBody SocialStreamResponseV2 post)
	{
		logger.info("::in updateFavoritefeed::");
		Object responce=null;
		responce=aggregateFeedV2Dao.updatePost(postId,postType,appUserId,post);
		return responce;
	}


	@RequestMapping(value={"news"}, method = RequestMethod.POST, produces = "APPLICATION/JSON")
	public @ResponseBody Object getaggregatefeedTest(@RequestParam(value="appUserId", required=false) String appUserId,@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type,@RequestParam(value="headlinetime", required=false) String headlinetime,@RequestBody HashMap<String, ArrayList<HashMap<Object, Object>>> userCurrentPlaces)
	{
		logger.info("::in getaggregatefeedTest::");
		SocialStreamV2 homeStream=new SocialStreamV2();
		homeStream.setUntil(until);
		homeStream.setSince(since);
		homeStream.setNoi(noi);
		homeStream.setHeadlinetime(headlinetime);
		Object o =  aggregateFeedV2Dao.GetAggregateFeedV2Test(appUserId,homeStream,type,userCurrentPlaces);
		return o;
	}

}
