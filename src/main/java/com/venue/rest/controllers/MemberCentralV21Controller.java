package com.venue.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.MemberCentralDao;

@Controller
@RequestMapping("/v21")
public class MemberCentralV21Controller {
	@Autowired
	MemberCentralDao memberCentralDao;
	@RequestMapping(value="memberCentralListSTM",method=RequestMethod.GET)
	public @ResponseBody Object getMemberCentralListSTMTest(@RequestParam(value="appUserId", required=false) String appUserId){
		Object response=null;
		try{
			response=memberCentralDao.getMemberCentralListV21(appUserId);
			return response;
		}catch(Exception e){
			response="{}";
			e.printStackTrace();
			return response;
		}
	}

	@RequestMapping(value="memberCentralListStarter",method=RequestMethod.GET)
	public @ResponseBody Object getMemberCentralListNonSTMTest(@RequestParam(value="appUserId", required=false) String appUserId){
		Object response=null;
		try{
			response=memberCentralDao.getMemberCentralListNonSTMV21();
			return response;
		}catch(Exception e){
			response="{}";
			e.printStackTrace();
			return response;
		}
	}
}
