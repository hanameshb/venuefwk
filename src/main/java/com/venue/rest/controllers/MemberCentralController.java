package com.venue.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.MemberCentralDao;

@Controller
@RequestMapping("/v2")
public class MemberCentralController {
	@Autowired
	MemberCentralDao memberCentralDao;

	/*@RequestMapping(value="memberCentralListSTM",method=RequestMethod.GET)
	public @ResponseBody Object getMemberCentralListSTM(@RequestParam(value="appUserId", required=false) String appUserId){
		Object response=null;
		try{
			File f=new File("/var/www/html/miamidolphinsstaticjsonfiles/MemberCental.JSON");
			int ch;
			if(f.exists()){
				FileInputStream r=new FileInputStream(f);
				StringBuffer s=new StringBuffer();
				 while( (ch = r.read()) != -1)
				        s.append((char)ch);

				//System.out.println(s);
				String data=s.toString();
				response=data.replaceAll("\\r\\n", "");
				r.close();
			}
			return response;
			}catch(Exception e){
				response="{}";
				e.printStackTrace();
				return response;
			}

	}*/


	/*@RequestMapping(value="memberCentralListStarter",method=RequestMethod.GET)
	public @ResponseBody Object getMemberCentralListNonSTM(@RequestParam(value="appUserId", required=false) String appUserId){
		Object response=null;
		try{
			File f=new File("/var/www/html/miamidolphinsstaticjsonfiles/NonSTMMemberCentral.JSON");
			int ch;
			if(f.exists()){
				FileInputStream r=new FileInputStream(f);
				StringBuffer s=new StringBuffer();
				 while( (ch = r.read()) != -1)
				        s.append((char)ch);

				//System.out.println(s);
				String data=s.toString();
				response=data.replaceAll("\\r\\n", "");
				r.close();
			}
			return response;
			}catch(Exception e){
				response="{}";
				e.printStackTrace();
				return response;
			}

	}*/


	@RequestMapping(value="memberCentralListSTM",method=RequestMethod.GET)
	public @ResponseBody Object getMemberCentralListSTMTest(@RequestParam(value="appUserId", required=false) String appUserId){
		Object response=null;
		try{
			response=memberCentralDao.getMemberCentralList(appUserId);
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
			response=memberCentralDao.getMemberCentralListNonSTM();
			return response;
		}catch(Exception e){
			response="{}";
			e.printStackTrace();
			return response;
		}
	}
}
