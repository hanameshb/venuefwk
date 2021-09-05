package com.venue.rest.dao;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.venue.rest.model.Carousal;
import com.venue.rest.model.PhotoGallaryDetailsFeed;
import com.venue.rest.model.SocialStream;
import com.venue.rest.model.SocialStreamColorCodes;
import com.venue.rest.model.SocialStreamResponse;
import com.venue.rest.util.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class AggregateFeedDAO 
{

	private static Logger logger = Logger.getLogger(AggregateFeedDAO.class);
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
		 * Method to get Aggregate Feed response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public Object GetAggregateFeed(SocialStream homeStream) {
	        try 
	        {
	    		logger.info("::in GetAggregateFeed:");

	        	Object aggregatedfeed = getAggregateFeedDetailsLatest(homeStream);
	        	if(aggregatedfeed!=null) 
	            {
	            	return  aggregatedfeed;
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
		 *   Method to get the getGalleryArray
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
				String query = "select * from tbl_photo_gallaries_media_thumbnail where item_id="+itemId+" and width=540 and height=360 group by url";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					while (rst.next()) 
					{
						galleryDetails =new PhotoGallaryDetailsFeed();
						galleryDetails.setTitle(rst.getString("gallery_title"));
						/*String url=rst.getString("url");
						if(url!=null && url.length()>0 && url.contains("jpg"))
						{
							url = url.substring(0, url.lastIndexOf("jpg")+3);
							galleryDetails.setPhotoUrl(url);
						}
						else if(url!=null && url.length()>0 && url.contains("JPG"))
						{
							url = url.substring(0, url.lastIndexOf("JPG")+3);
							galleryDetails.setPhotoUrl(url);
						}
						else
						{
							galleryDetails.setPhotoUrl(rst.getString("url"));
						}*/
						
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
		
		
		
		/*
		 *   Method to get the Carousal Array
		 *    
		 */
		@SuppressWarnings("unchecked")
		public ArrayList<com.venue.rest.model.Carousal> getCarousal()
		{
	        Carousal carousal=null;
	        SqlRowSet rst=null;
	        ArrayList carousalArray = new ArrayList();
			try
	        {	
	        	String query = "select carousel_img_url,type,action,headline_link_text from tbl_homescreen_carousel order by order_id";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					while (rst.next()) 
					{
						carousal = new Carousal();
						carousal.setUrl(rst.getString("carousel_img_url"));
						carousal.setLink(rst.getString("headline_link_text"));
						carousal.setType(rst.getString("type"));
						carousal.setAction(rst.getString("action"));
						carousal.setHeadlinetime(getLatestUpdatedNewsTime());
						carousalArray.add(carousal);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return carousalArray;
		}
		
		public String getLatestUpdatedNewsTime()
		{
			SqlRowSet rst=null;
	        String  lastest_updated_time="";
	        String query="";
	        try
	        {	
				try 
				{	
		    		query = "select max(updated_time) as lastest_updated_time from tbl_homescreen_carousel";
	        		rst = jdbcTemplateObject.queryForRowSet(query);
	        		if (rst.next()) 
	        		{
	        			lastest_updated_time = rst.getString("lastest_updated_time");
	        		}
	        		logger.info("getLatestUpdatedNewsTime:::lastest_updated_time::::"+lastest_updated_time);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return lastest_updated_time;
		}
		
		public boolean checkHeadlinesSending(String updatedtime)
		{
	        SqlRowSet rst=null;
	        boolean headlinesRequired=false;
	        String query="";
			logger.info("checkHeadlinesSending:::updatedtime::::"+updatedtime);
	        if(updatedtime=="" || updatedtime==null || updatedtime.equals("null")) return true;
	        
	        try
	        {	
				try 
				{	
					if(updatedtime!=null && updatedtime!="" && updatedtime.length()>0)
					{
		        		query = "select *  from tbl_homescreen_carousel where updated_time > '"+updatedtime+"'";
						logger.info("checkHeadlinesSending:::query::::"+query);
		        		rst = jdbcTemplateObject.queryForRowSet(query);
		        		if (rst.next()) 
		        		{
		        			headlinesRequired = true;
		        		}
					  }
					logger.info("checkHeadlinesSending:::headlinesRequired::::"+headlinesRequired);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return headlinesRequired;
		}
		
		
		/**
		 * Method to get Aggregate Feed response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public Object getAggregateFeedDetailsLatest(SocialStream homeStream) {
			ArrayList<Object> ls=null;
			SocialStreamResponse homeStreamResponse = null;
			ArrayList carousalArray = new ArrayList();
			int maxCount = 0;
	        String numberofitems="";
	        SqlRowSet sqlrowset=null;
			try{
				
				String start = "";
				String end = "";
				String until = "";
				String since = "";
				String type = "";
				String source="";				
				int noi=0;
				
				String headlinetime = "";
				
				until = homeStream.getUntil();
				since = homeStream.getSince();
				noi =homeStream.getNoi();
				headlinetime = homeStream.getHeadlinetime();
				if (noi>0)
				{
					noi=noi;
					
				}else
				{
					ResourceBundle bundle = ResourceBundle.getBundle("db");        
		    		if(bundle.getString("noi")!=null)
		    		{
		    			numberofitems = bundle.getString("noi");
		    			
		    			if(numberofitems!=null && numberofitems.length()>0)
		    			{
		    				noi = Integer.parseInt(numberofitems);
		    			}	
		    		}
				}
				logger.info("getHomeStreamDetails:until"+until);
				logger.info("getHomeStreamDetails:since"+since);
				logger.info("getHomeStreamDetails:type"+type);
				logger.info("getHomeStreamDetails:noi"+noi); 
				logger.info("getHomeStreamDetails:headlinetime"+headlinetime); 

				 if(since!=null && since.length()>0)
				 {
						
						logger.info("getHomeStreamDetails:::Case1:");
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
								" from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_photo_gallaries_master) " +
								"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_videos_master) UNION ALL  (select item_id as postid" +
								",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
								" from tbl_audio_master)) as maintable " +
								"where post_time > '"+since+"' group by post_time order by post_time desc");

						logger.info("sqlrowset..."+sqlrowset);
						logger.info("sqlrowset"+sqlrowset);		
					}else if(until!=null && until.length()>0) 
					{
						logger.info("getHomeStreamDetails:::Case2:");
						/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link " +
								"from((select id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type " +
								"from tbl_news_feed) UNION ALL (select id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_photo_gallaries)" +
								" UNION ALL  (select id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_videos)) as maintable " +
								"where post_time < "+until+" group by post_time order by post_time desc limit "+noi+"");
								 */
						
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description " +
								" from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_photo_gallaries_master) " +
								"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_videos_master) UNION ALL  (select item_id as postid" +
								",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description from tbl_audio_master)) as maintable " +
								"where post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");

						logger.info("sqlrowset..."+sqlrowset);
		
					}else 
					{
						logger.info("getHomeStreamDetails:::Case3:");
						/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title,post_picture_url,post_description," +
								"post_time,video_url,type,link from((select id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_news_feed) " +
								"UNION ALL (select id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_photo_gallaries) " +
								"UNION ALL  (select id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_videos)) as maintable " +
								"group by post_time order by post_time desc limit "+noi+"");
								
						*/ 
						
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
								" from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_photo_gallaries_master) " +
								"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description from tbl_videos_master) UNION ALL  (select item_id as postid" +
								",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description from tbl_audio_master)) as maintable " +
								"group by post_time order by post_time desc limit "+noi+"");
								logger.info("sqlrowset:::"+sqlrowset);
					}
					
				logger.info("sqlrowset:::"+sqlrowset);
				int rowCount=0;
				if(sqlrowset.first())
				{
					sqlrowset.last();
		        	rowCount = sqlrowset.getRow();
			     }
	        	logger.info("rowCount"+rowCount);

				ls = new ArrayList<Object>();
				ArrayList<SocialStreamResponse> alt = new ArrayList<SocialStreamResponse>();
				ArrayList<SocialStreamColorCodes> colorCodeArray = new ArrayList<SocialStreamColorCodes>();
				if (rowCount > 0) 
	        	{
					sqlrowset.first();
		        	sqlrowset.previous();
		        	
					while(sqlrowset.next()) 
					{
						homeStreamResponse = new SocialStreamResponse();
						homeStreamResponse.setPost_id(sqlrowset.getString("postid"));						
						//String  title1 = new String(sqlrowset.getString("post_title").getBytes("ISO-8859-1"));
						 
						String  title1="",title2="";
						title1 = sqlrowset.getString("post_title");
						logger.info("Title before ::::"+title1);
						if(title1!=null && title1.length()>0)
						{	
							title1 = StringEscapeUtils.unescapeHtml(title1);
							title2 = new String(title1.getBytes("UTF-8"),"UTF-8");						 
						}
						logger.info("Title after ::::"+title2);
						 
						homeStreamResponse.setPost_title(title2);
						
						String desc1="",desc2="",htmldesc1="",htmldesc2="";
						
						desc1 = sqlrowset.getString("post_description");
						
						if(desc1!=null && desc1.length()>0)
						{	
							desc1 = StringEscapeUtils.unescapeHtml(desc1);						
							desc2 = new String(desc1.getBytes("UTF-8"),"UTF-8");						 
						}
						homeStreamResponse.setPost_description(desc2);						
						htmldesc1 = sqlrowset.getString("post_html_description");

						if(htmldesc1!=null && htmldesc1.length()>0)
						{	
							htmldesc1 = new String(htmldesc1.getBytes("US-ASCII"));

							htmldesc1=htmldesc1.replaceAll("</?a href=[^>]+>", "");
							htmldesc1=htmldesc1.replaceAll("(</a>)+", "");
							htmldesc1=htmldesc1.replaceAll("</?img src=[^>]+>", "");
							htmldesc1=htmldesc1.replaceAll("(</img>)+", "");
							//htmldesc1 = StringEscapeUtils.unescapeHtml(htmldesc1);
							htmldesc2 = new String(htmldesc1.getBytes("UTF-8"),"UTF-8");
							
						}						
						homeStreamResponse.setPost_htmldescription(htmldesc2);						
						homeStreamResponse.setPost_time(sqlrowset.getString("post_time"));
						
						homeStreamResponse.setPost_picture_url(sqlrowset.getString("post_picture_url"));
						
						homeStreamResponse.setVideo_url(sqlrowset.getString("video_url"));
						homeStreamResponse.setAudio_url(sqlrowset.getString("audio_url"));
						
						homeStreamResponse.setType(sqlrowset.getString("type"));						
						homeStreamResponse.setLink(sqlrowset.getString("link"));
						
						logger.info("Link from db  is :::::::::::::"+sqlrowset.getString("link"));	
						logger.info("post time from db  is :::::::::::::"+sqlrowset.getString("post_time"));
						
						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("video"))						
						{	
							homeStreamResponse.setVideo_url(getVideoUrl(sqlrowset.getString("postid")));													
							homeStreamResponse.setPost_picture_url(getVideoThumb(sqlrowset.getString("postid")));
						}
						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("audio"))						
						{	
							homeStreamResponse.setAudio_url(getAudioUrl(sqlrowset.getString("postid")));													
							homeStreamResponse.setPost_picture_url(getAudioThumb(sqlrowset.getString("postid")));
						}												
						
						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("photo"))
						{	
							homeStreamResponse.setPhotogallery(getGalleryArray(sqlrowset.getString("postid")));
							homeStreamResponse.setPost_picture_url(getGalleryThumb(sqlrowset.getString("postid")));
						}
			
					
						maxCount++;					
						
						String post_time="";        			
						post_time= sqlrowset.getString("post_time");
						
						if(until!=null  && !until.equals(" ") && until.length()>0)
	        			{	
	        	        	logger.info("getHomeStreamDetails:::case1");
	    		        	logger.info("rowCount"+rowCount);
	    		        	logger.info("maxCount"+maxCount);
	    		        	logger.info("post_time"+post_time);

	        				if(maxCount==rowCount)
		        			{	
	        					homeStream.setUntil(post_time);
	        					homeStream.setSince("");

		        			}
	        			}else if(since!=null  && !since.equals(" ") && since.length()>0)
	        			{	
	        				logger.info("getHomeStreamDetails:::case2");
	        				logger.info("rowCount"+rowCount);
	    		        	logger.info("maxCount"+maxCount);
	    		        	
	        				if(maxCount==1)
		        			{	
	        					homeStream.setSince(post_time);
	        					homeStream.setUntil("");
		        			}
	        			}else
	        			{
	        				logger.info("getHomeStreamDetails:::case3");
	    		        	logger.info("maxCount"+maxCount);
		        			if(maxCount==1)
		        			{
		        				
		        				homeStream.setSince(post_time);
		        				logger.info("untilTime:::"+post_time);
		        			}
		        			else if(maxCount==rowCount)
		        			{
		        				homeStream.setUntil(post_time);
		        				logger.info("sinceTime:::"+post_time);
		        			}
	        			}
						
						alt.add(homeStreamResponse);
					}
					
					homeStream.setData(alt);
					if(checkHeadlinesSending(headlinetime))
					{
						carousalArray = getCarousal();
						homeStream.setHeadlines(carousalArray);
	        		}
					ls.add(homeStream);
	        	}
				else
				{
					if(checkHeadlinesSending(headlinetime))
					{
						carousalArray = getCarousal();
						homeStream.setHeadlines(carousalArray);
						ls.add(homeStream);
						
	        		}else
	        		{
	        			errorCode="1005";
	        			return  ErrorMessage.getInstance().getErrorResponse(errorCode);
	        		}
				}
				
			}catch(Exception e)
			{
				e.printStackTrace();
				errorCode="500";
		        return  ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
			
			
			return ls;
		}
		
		 public static String getUTFDecodedString(String othercharset) {
		        try {
		            byte[] otherBs = othercharset.getBytes("ISO-8859-1");
		            return new String(otherBs, "UTF-8");
		        } catch(UnsupportedEncodingException e) {
		            e.printStackTrace();
		        }
		        return othercharset;
		    }

		
		/*
		 *   Method to get the Audio URl
		 *    
		 */
		@SuppressWarnings("unchecked")
		public String getAudioUrl(String itemId)
		{
	        SqlRowSet rst=null;
	        String audioUrl="";
	        ArrayList carousalArray = new ArrayList();
			try
	        {	
	        	String query = "select * from tbl_audio_media_content where item_id="+itemId+"";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()) 
					{	
						audioUrl=rst.getString("url");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return audioUrl;
		}
		
		/*
		 *   Method to get the Audio Thumb
		 *    
		 */
		@SuppressWarnings("unchecked")
		public String getAudioThumb(String itemId)
		{
	        SqlRowSet rst=null;
	        String audioThumbUrl="";
			try
	        {	
	        	String query = " select * from tbl_audio_media_thumbnail where item_id="+itemId+" and  width is null and height is null";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()) 
					{	
						audioThumbUrl=rst.getString("url");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return audioThumbUrl;
		}
		
		
		/*
		 *   Method to get the Audio URl
		 *    
		 */
		@SuppressWarnings("unchecked")
		public String getVideoUrl(String itemId)
		{
	        SqlRowSet rst=null;
	        String videoUrl="";
			try
	        {	
	        	String query = "select * from tbl_videos_media_content where item_id="+itemId+" and bitrate=486";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()) 
					{	
						videoUrl=rst.getString("url");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return videoUrl;
		}
		
		/*
		 *   Method to get the Audio Thumb
		 *    
		 */
		@SuppressWarnings("unchecked")
		public String getVideoThumb(String itemId)
		{   SqlRowSet rst=null;
	        String videoThumbUrl="";			
	        try
	        {	
	        	String query = "select * from tbl_videos_media_thumbnail where item_id="+itemId+" and width=540 and height=360";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()) 
					{	
						videoThumbUrl=rst.getString("url");						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return videoThumbUrl;
		}
		
		
		/*
		 *   Method to get the Audio Thumb
		 *    
		 */
		@SuppressWarnings("unchecked")
		public String getGalleryThumb(String itemId)
		{   SqlRowSet rst=null;
	        String galleryThumbUrl="";			
	        try
	        {	
	        	String query = "select * from tbl_photo_gallaries_media_thumbnail where item_id="+itemId+" and width=540 and height=360";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()) 
					{	
						galleryThumbUrl=rst.getString("url");
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			 return galleryThumbUrl;
		}
		
		
		 	 

}
