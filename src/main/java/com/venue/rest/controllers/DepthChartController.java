package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.DepthChartDAO;

@Controller
@RequestMapping("/depthchart")
public class DepthChartController {

	private static Logger logger = Logger.getLogger(DepthChartController.class);
	
	@Autowired
	DepthChartDAO depthChartDao;
	/**
	 * Method to get the depth chart.
	 */
	@RequestMapping(value={"getdepthchart"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getdepthchart() 
	{
		logger.info("::in getdepthchart:");
		Object o = null;
		o = depthChartDao.getDepthChart();
		logger.info("final out pit is"+o);
		return o;		
	}
}
