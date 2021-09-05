package com.venue.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;
import com.venue.rest.dao.JwtTokenDAO;
import com.venue.rest.model.JwtTokenBean;

@Controller
public class JwtTokenController {
	private static Logger logger = Logger.getLogger(JwtTokenController.class);
	private @Value("#{props1[jwtSecretKey]}") String jwtSecretKey;

	@Autowired
	JwtTokenDAO jwtTokenDao;
	/**
	 * Method to Generate JWT Token
	 */
	@RequestMapping(value={"/v2/token"}, method = RequestMethod.POST)
	public @ResponseBody Object getToken(@RequestBody JwtTokenBean jwtTokenBean) {
		logger.info("::inside getToken::");
		Object response = null;
		response = jwtTokenDao.getToken(jwtTokenBean,jwtSecretKey);	
		return response;
	}
}
