package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.CheerLeadersAggregatedFeedDAO;
import com.venue.rest.model.CheerLeadersAggregatedFeedRequest;

@Controller
@RequestMapping("/cheerleadersaggregate")
public class CheerLeadersAggregateFeedController {

	private static Logger logger = Logger.getLogger(CheerLeadersAggregateFeedController.class);
	@Autowired
	CheerLeadersAggregatedFeedDAO cheerLeadersAggregatedFeedDao;
	/**
	 * Method to Get the Aggregate Feed.
	 */
	
	@RequestMapping(value={"getcheerleadersaggregate"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
		public @ResponseBody Object getCheerLedersAggregatedData(@RequestParam(value="noi", required=false) int noi,@RequestParam(value="until", required=false) String until,@RequestParam(value="since", required=false) String since,@RequestParam(value="type", required=false) String type) 
		{
			logger.info("::in getcheerleadersaggregate:");
			CheerLeadersAggregatedFeedRequest clafr=new CheerLeadersAggregatedFeedRequest();
			clafr.setUntil(until);
			clafr.setSince(since);
			clafr.setNoi(noi);			
			Object o = cheerLeadersAggregatedFeedDao.GetCheerLeadersAggregateFeed(clafr);	
			return o;
		}
}
