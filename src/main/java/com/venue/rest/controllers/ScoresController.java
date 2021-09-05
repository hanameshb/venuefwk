package com.venue.rest.controllers;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.ScoresDAO;
@Controller
@RequestMapping("/scores")
public class ScoresController {

	private static Logger logger = Logger.getLogger(ScoresController.class);
	/**
	 * Method to Get the Scores.
	 */
	
	/*@RequestMapping(value={"getscores"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
	public @ResponseBody Object getscores() 
	{
		logger.info("::in getscores:");
		ScoresDAO dao = ScoresDAO.getInstance();
		Object o = null;
		o = dao.GetScores();
		return o;		

	}*/
	
}
