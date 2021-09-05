package com.venue.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.WeatherDAO;


@Controller
@RequestMapping("/v2")
public class WeatherController {

	@Autowired
	WeatherDAO weatherDao;
	@RequestMapping(value="/weather",method=RequestMethod.GET)
	public @ResponseBody Object getWeather(){
		Object response=null;
		response=weatherDao.getWeatherResponse();
		return response;
	}
}
