package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.DepthChartDAO;

@Controller
@RequestMapping("/v2/depthchart")
public class DepthChartV2Controller {

	private static Logger logger = Logger.getLogger(DepthChartV2Controller.class);
	
	@Autowired
	DepthChartDAO depthChartDao;
	/**
	 * Method to get the depth chart V2.
	 */
	@RequestMapping(value={"getdepthchart"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getdepthchartV2() 
	{
		logger.info("::in getdepthchartV2:");
		Object o = null;
		o = depthChartDao.getDepthChartV2();
		logger.info("final out pit is"+o);
		return o;		
	}
}
