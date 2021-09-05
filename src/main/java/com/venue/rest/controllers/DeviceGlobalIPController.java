package com.venue.rest.controllers;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v4")
public class DeviceGlobalIPController {
	private static Logger logger = Logger.getLogger(DeviceGlobalIPController.class);
	@RequestMapping(value="/deviceGlobalIP", method=RequestMethod.GET)
	public @ResponseBody Object processData(HttpServletRequest request) {
	        logger.info(request.getRemoteAddr());
	        return request.getRemoteAddr();
	    }
}