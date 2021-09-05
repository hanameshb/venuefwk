package com.venue.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.GetLatestGalleryDAO;
@Controller
@RequestMapping("/photogallery")
public class GetLatestGalleryController {

	private static Logger logger = Logger.getLogger(GetLatestGalleryController.class);
	@Autowired
	GetLatestGalleryDAO getLatestGalleryDao;
	/**
	 * Method to Get the Latest Photo Gallery.
	 */
	
	@RequestMapping(value={"getlatestgallery"}, method = RequestMethod.GET, produces = "APPLICATION/JSON")
		public @ResponseBody Object getLatestPhotoGallery(@RequestParam(value="id", required=false) String id) 
		{
			logger.info("::in getlatestgallery:");
			Object o =  getLatestGalleryDao.GetLatestPhotoGallery(id);	
			return o;
		}
}
