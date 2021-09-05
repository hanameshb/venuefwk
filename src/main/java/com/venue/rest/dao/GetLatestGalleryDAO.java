package com.venue.rest.dao;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.PhotoGallaryDetailsFeed;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class GetLatestGalleryDAO 
{

	private static Logger logger = Logger.getLogger(GetLatestGalleryDAO.class);
		String errorCode = "";
		private JdbcTemplate jdbcTemplateObject=null;
		
		@Autowired
		@Qualifier("dataSourceVenue")
		DataSource dataSourceVenue;
		
		
		@PostConstruct
	    public void init() {
			jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
	    	
	    }

	    /**
		 * Method to get Latest Photo Gallery response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public Object GetLatestPhotoGallery(String id) {
	        try 
	        {
	    		logger.info("::in GetLatestPhotoGallery:");

	        	Object photogallery = getLatestPhotoGalleryDetails(id);
	        	if(photogallery!=null) 
	            {
	            	return  photogallery;
	            }else
	            {
	            	errorCode="500";
	            	return  ErrorMessage.getInstance().getErrorResponse(errorCode);
	            }
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			}
	    }
	
		
	   
		
		/*
		 *   Method to form Photo Gallery Array
		 * 
		 */
		@SuppressWarnings("unchecked")	
		 public ArrayList<com.venue.rest.model.PhotoGallaryDetailsFeed> getGalleryArray(String itemId)
		 {
				String METHODNAME = "getGalleryArray()";
				logger.info(METHODNAME);		
		        ArrayList photoGalleryArray = new ArrayList();
		        PhotoGallaryDetailsFeed galleryDetails=null;
		        SqlRowSet rst=null;
				String query = "select * from tbl_photo_gallaries_media_thumbnail where item_id="+itemId+" and width=540 and height=360 group by url order by updated_time desc";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					while (rst.next()) 
					{
						galleryDetails =new PhotoGallaryDetailsFeed();
						galleryDetails.setTitle(rst.getString("gallery_title"));
						galleryDetails.setPhotoUrl(rst.getString("url"));
						galleryDetails.setWidth(rst.getString("width"));
						galleryDetails.setHeight(rst.getString("height"));
						logger.info("Url:::"+rst.getString("url"));
			        	photoGalleryArray.add(galleryDetails);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return photoGalleryArray;
			}	 
		
		/**
		 * Method to get Latest Photo Gallery response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public Object getLatestPhotoGalleryDetails(String id) {
	        ArrayList<com.venue.rest.model.PhotoGallaryDetailsFeed> photogallery = new ArrayList<com.venue.rest.model.PhotoGallaryDetailsFeed>();
	        Hashtable ht = new Hashtable();
			try
			{	
				photogallery = getGalleryArray(id);				
				ht.put("photogallery", photogallery);
	        	
			}catch(Exception e)
			{
				e.printStackTrace();
				errorCode="500";
		        return  ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
			
			return ht;
		}
		
		
		 	 

}
