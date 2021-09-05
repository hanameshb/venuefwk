package com.venue.rest.dao;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.venue.rest.model.Scores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class ScoresDAO 
{
	private static Logger logger = Logger.getLogger(ScoresDAO.class);
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
		 * Method to get Scores response in JSON format.
		 */
		/*@SuppressWarnings("unchecked")
		public List<Object> GetScores() {
	        try 
	        {
	    		logger.info("::in GetScores:");

	        	Object videos = GetScoreDetails();
	        	if(videos!=null) 
	            {
	            	return (List<Object>) videos;
	            }else
	            {
		            //return (List<Object>) ErrorMessage.getInstance().getErrorResponseList(errorCode);
	            }
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			}
	    }*/
		
		/**
		 * Method to get scores response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public List<Object> GetScoreDetails() 
		{
	    	List<Object> ls = null;
    		logger.info("::in GetVideosDetails:");
	    	SqlRowSet rst=null;
	        try
			{
	        	 Scores scores=null; 
	        	 ls = new ArrayList<Object>();
				 rst = jdbcTemplateObject.queryForRowSet("select * from tbl_videos") ;
				 System.out.println("GetVideosDetails:::");	
				 while(rst.next())
				 {
					 scores = new Scores();
		/*			 vf.setTitle(rst.getString("title"));
					 vf.setLink(rst.getString("link"));
					 vf.setDescription(rst.getString("description"));
					 vf.setPubDate(rst.getString("pubdate"));*/
					// ls.add(vf);
				 }
			}catch(Exception e)
			{
				e.printStackTrace();
				errorCode="500";
		        //return (List<Object>) ErrorMessage.getInstance().getErrorResponseList(errorCode);
			}
			
			return ls;
		}
		
		
}
