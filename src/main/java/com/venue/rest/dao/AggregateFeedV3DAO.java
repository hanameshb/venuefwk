package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.Carousal;
import com.venue.rest.model.PhotoGallaryDetailsFeed;
import com.venue.rest.model.SocialStreamColorCodes;
import com.venue.rest.model.SocialStreamResponseV2;
import com.venue.rest.model.SocialStreamV2;
import com.venue.rest.util.ErrorMessage;
@Repository
public class AggregateFeedV3DAO {
	
	private static Logger logger = Logger.getLogger(AggregateFeedV3DAO.class);
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
	 * Method to get Aggregate Feed response in JSON format V3 Test.
	 * @param userCurrentPlaces
	 */
	public Object GetAggregateFeedV3Test(String appUserId,SocialStreamV2 homeStream,String type, HashMap<String, ArrayList<HashMap<Object, Object>>> userCurrentPlaces) {
        try
        {
    		logger.info("::in GetAggregateFeedV3Test::");

        	Object aggregatedfeed = getAggregateFeedDetailsLatestV3Test(appUserId,homeStream,type,userCurrentPlaces);
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
	
	/**
	 * Method to get Aggregate Feed response in JSON format V3 Test.
	 * @param userCurrentPlaces
	 */
	public Object getAggregateFeedDetailsLatestV3Test(String appUserId,SocialStreamV2 homeStream,String type, HashMap<String, ArrayList<HashMap<Object, Object>>> userCurrentPlaces) {
		logger.info(":::in getAggregateFeedDetailsLatestV3Test:::");
		ArrayList<Object> ls=null;
		SocialStreamResponseV2 homeStreamResponse = null;
		ArrayList<Carousal> carousalArray = new ArrayList<Carousal>();
		int maxCount = 0;
        String numberofitems="";
        SqlRowSet sqlrowset=null;
        SqlRowSet sqlrowsetDataUpdatedFlag=null;
        
		try{
			String start = "";
			String end = "";
			String until = "";
			String since = "";
			String source="";
			int noi=0;

			String headlinetime = "";

			until = homeStream.getUntil();
			since = homeStream.getSince();
			noi =homeStream.getNoi();
			headlinetime = homeStream.getHeadlinetime();
			if (noi>0)
			{
				noi=homeStream.getNoi();

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
			logger.info("getHomeStreamDetails:until::" +until);
			logger.info("getHomeStreamDetails:since::" +since);
			logger.info("getHomeStreamDetails:type::" +type);
			logger.info("getHomeStreamDetails:noi::" +noi);
			logger.info("getHomeStreamDetails:headlinetime::" +headlinetime);

			 if(since!=null && since.length()>0)
			 {
				 logger.info("::getHomeStreamDetails:::Case1::");
				 sqlrowsetDataUpdatedFlag =jdbcTemplateObject.queryForRowSet("select created_time from tbl_post_updated_time where created_time > '"+since+"' order by created_time desc limit 1");

				 	if(sqlrowsetDataUpdatedFlag.next()){
				 		homeStream.setDataUpdated(true);
				 		sqlrowsetDataUpdatedFlag=null;
				 	}
				 	
				 	if( type != null&& !type.equalsIgnoreCase("")){
				 		logger.info("::type is not empty::");
						if(type.equalsIgnoreCase("news")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description," +
									"mark_for_delete as delete_status from tbl_newsfeed)) as maintable " +
									"where delete_status !=1 and post_time > '"+since+"' group by post_time order by post_time desc");
						}else if(type.equalsIgnoreCase("photo")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									") as maintable " +
									"where delete_status !=1 and  post_time > '"+since+"' group by post_time order by post_time desc");
						}else if(type.equalsIgnoreCase("video")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master "+
									")) as maintable " +
									"where delete_status !=1 and  post_time > '"+since+"' group by post_time order by post_time desc");
						}else if(type.equalsIgnoreCase("audio")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
									",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status !=1 and  post_time > '"+since+"' group by post_time order by post_time desc");
						}else{
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
									",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
									",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status !=1 and  post_time > '"+since+"' group by post_time order by post_time desc");
						}
					}else{
						logger.info("::type is empty::");
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
								",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
								"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
								",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
								",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
								"where delete_status !=1 and  post_time > '"+since+"' group by post_time order by post_time desc");
					}
				 	logger.info("::sqlrowset in case1::" +sqlrowset);
				}else if(until!=null && until.length()>0)
				{
					logger.info("::getHomeStreamDetails:::Case2::");
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
					if( type != null&& !type.equalsIgnoreCase("")){
						logger.info("::type is not empty::");
						if(type.equalsIgnoreCase("news")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description, " +
									"mark_for_delete as delete_status from tbl_newsfeed)) as maintable " +
									"where delete_status != 1 and post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("photo")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									") as maintable " +
									"where delete_status !=1 and  post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("video")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master "+
									")) as maintable " +
									"where delete_status !=1 and  post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("audio")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
									",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status !=1 and  post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
						}else{
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description " +
									",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status !=1 and  post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
							}
					}else{
						logger.info("::type is empty::");
					sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
							"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
							"from((select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description " +
							",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
							"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
							"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
							",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
							"where delete_status !=1 and  post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
					}

					logger.info("::sqlrowset in case2::" +sqlrowset);

				}else
				{
					logger.info("::getHomeStreamDetails:::Case3::");
					/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title,post_picture_url,post_description," +
							"post_time,video_url,type,link from((select id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_news_feed) " +
							"UNION ALL (select id as postid,title as post_title,description as post_description,pubdate as post_time," +
							"link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_photo_gallaries) " +
							"UNION ALL  (select id as postid,title as post_title,description as post_description,pubdate as post_time," +
							"link as link,photo_url as post_picture_url,video_url as video_url,type as type from tbl_videos)) as maintable " +
							"group by post_time order by post_time desc limit "+noi+"");

					*/
					if( type != null&& !type.equalsIgnoreCase("")){
						logger.info("::type is not empty::");
						if(type.equalsIgnoreCase("news")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description, " +
									"mark_for_delete as delete_status from tbl_newsfeed)) as maintable " +
									"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("photo")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									") as maintable " +
									"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("video")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master "+
									")) as maintable " +
									"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
						}else if(type.equalsIgnoreCase("audio")){
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
									",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
						}else{
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
									",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
									"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
									",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
									"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
							}
					}else{
						logger.info("::type is empty::");
					sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
							"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
							"from((select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description" +
							",mark_for_delete as delete_status from tbl_newsfeed) UNION ALL (select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
							"UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
							"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master) UNION ALL  (select item_id as postid" +
							",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
							"where delete_status != 1 group by post_time order by post_time desc limit "+noi+"");
					}
					logger.info("::sqlrowset in case3::" +sqlrowset);
				}
			int rowCount=0;
			if(sqlrowset.first())
			{
				sqlrowset.last();
	        	rowCount = sqlrowset.getRow();
		     }
        	logger.info("::rowCount::" +rowCount);

			ls = new ArrayList<Object>();
			ArrayList<SocialStreamResponseV2> alt = new ArrayList<SocialStreamResponseV2>();
			ArrayList<SocialStreamColorCodes> colorCodeArray = new ArrayList<SocialStreamColorCodes>();
			if (rowCount > 0)
        	{
				logger.info("::rowCount is greater than 0::");
				sqlrowset.first();
	        	sqlrowset.previous();

				while(sqlrowset.next())
				{
					homeStreamResponse = new SocialStreamResponseV2();
					homeStreamResponse.setPost_id(sqlrowset.getString("postid"));
					//String  title1 = new String(sqlrowset.getString("post_title").getBytes("ISO-8859-1"));

					String  title1="",title2="";
					title1 = sqlrowset.getString("post_title");
					logger.info("::Title before::" +title1);
					if(title1!=null && title1.length()>0)
					{
						title1 = StringEscapeUtils.unescapeHtml(title1);
						title2 = new String(title1.getBytes("UTF-8"),"UTF-8");
					}
					logger.info("::Title after::" +title2);

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

					logger.info("::Link from db::" +sqlrowset.getString("link"));
					logger.info("::post time from db::" +sqlrowset.getString("post_time"));

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
					if(sqlrowset.getString("type")!=null &&!sqlrowset.getString("type").equalsIgnoreCase(""))
					homeStreamResponse.setUserFavoritedStatus(getFavoritedStatus(appUserId,sqlrowset.getString("postid"),sqlrowset.getString("type")));


					maxCount++;
					String post_time="";
					post_time= sqlrowset.getString("post_time");

					if(until!=null  && !until.equals(" ") && until.length()>0)
        			{
        	        	logger.info("::in if case1::");
    		        	logger.info("::rowCount::" +rowCount);
    		        	logger.info("::maxCount::" +maxCount);
    		        	logger.info("::post_time::" +post_time);

        				if(maxCount==rowCount)
	        			{
        					homeStream.setUntil(post_time);
        					homeStream.setSince("");

	        			}
        			}else if(since!=null  && !since.equals(" ") && since.length()>0)
        			{
        				logger.info("::in if case2::");
        				logger.info("::rowCount::" +rowCount);
    		        	logger.info("::maxCount::" +maxCount);

        				if(maxCount==1)
	        			{
        					homeStream.setSince(post_time);
        					homeStream.setUntil("");
	        			}
        			}else
        			{
        				logger.info("::in if case3::");
    		        	logger.info("::maxCount::" +maxCount);
	        			if(maxCount==1)
	        			{
	        				homeStream.setSince(post_time);
	        				logger.info("::untilTime::" +post_time);
	        			}
	        			else if(maxCount==rowCount)
	        			{
	        				homeStream.setUntil(post_time);
	        				logger.info("::sinceTime::" +post_time);
	        			}
        			}
					alt.add(homeStreamResponse);
				}

				homeStream.setData(alt);
				if(checkHeadlinesSendingV2(headlinetime))
				{
					logger.info("::headlines available::");
					carousalArray = getCarousalTest(appUserId,userCurrentPlaces);
					logger.info("::carousalArray size::" +carousalArray.size());
					homeStream.setHeadlines(carousalArray);
        		}
				ls.add(homeStream);
        	}
			else
			{
				logger.info("::rowCount is less than 0::");
				if(checkHeadlinesSendingV2(headlinetime))
				{
					logger.info("::headlines available::");
					carousalArray = getCarousalTest(appUserId,userCurrentPlaces);
					logger.info("::carousalArray size::" +carousalArray.size());
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
	
	
	/*
	 *   Method to get the video URl
	 *
	 */
	public String getVideoUrl(String itemId)
	{
        SqlRowSet rst=null;
        String videoUrl="";
		try
        {
			//String query = "select * from tbl_videos_media_content where item_id="+itemId+" and bitrate=486";
        	//changed query to fix empty video url issue on 07Apr2016 - Sandeep
			String query = "select * from tbl_videos_media_content where item_id="+itemId+" and (bitrate=486 || bitrate is null) and duration is not null";
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
	 *   Method to get the video Thumb
	 *
	 */
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
	public String getAudioUrl(String itemId)
	{
        SqlRowSet rst=null;
        String audioUrl="";
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
	 *   Method to get the getGalleryArray
	 *
	 */
	 public ArrayList<com.venue.rest.model.PhotoGallaryDetailsFeed> getGalleryArray(String itemId)
	 {
			String METHODNAME = "getGalleryArray()";
			logger.info(METHODNAME);
	        ArrayList<PhotoGallaryDetailsFeed> photoGalleryArray = new ArrayList<PhotoGallaryDetailsFeed>();
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
		 *   Method to get the gallery Thumb
		 *
		 */
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

		/**
		 * Method to get Favorited Status of each post.
		 */
		String getFavoritedStatus(String appUserId, String postId,
				String type) {
			// TODO Auto-generated method stub
			logger.info("::: in getFavoritedStatus:::");
			SqlRowSet rst=null;
			String userFavoritedStatus="0";
			try {
				rst=jdbcTemplateObject.queryForRowSet("select * from tbl_user_post_favorited_mapping where app_user_id='"+appUserId+"' and post_id="+postId+" and type='"+type+"'");
				if(rst.next()){
					if(rst.getBoolean("user_favorite_status"))
					userFavoritedStatus="1";
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.info(":::Exception in getFavoritedStatus:::"+e.getLocalizedMessage());
				e.printStackTrace();
			}
			return userFavoritedStatus;
		}
		
		public boolean checkHeadlinesSendingV2(String updatedtime)
		{
			logger.info("::in checkHeadlinesSendingV2::");
	        SqlRowSet rst=null;
	        boolean headlinesRequired=false;
	        String query="";
			logger.info("::updatedtime::" +updatedtime);
	        if(updatedtime=="" || updatedtime==null || updatedtime.equals("null"))
	        	return true;
	        
	        try
	        {
	        	if(updatedtime!=null && updatedtime!="" && updatedtime.length()>0)
	        	{
	        		query = "select * from tbl_headline_master where updated_time > '"+updatedtime+"'";
	        		logger.info("::query in checkHeadlinesSendingV2::" +query);
	        		rst = jdbcTemplateObject.queryForRowSet(query);
	        		if (rst.next())
	        		{
	        			headlinesRequired = true;
	        		}
	        	}
	        	logger.info("::headlinesRequired::" +headlinesRequired);
	        } catch (Exception e) {
	        	logger.error("::Exception in checkHeadlinesSendingV2::" +e);
	        	e.printStackTrace();
	        }
	        return headlinesRequired;
		}
		
		/*
		 *   Method to get the Carousal Array Test
		 *
		 */
		public ArrayList<com.venue.rest.model.Carousal> getCarousalTest(String appUserId, HashMap<String, ArrayList<HashMap<Object, Object>>> userCurrentPlaces)
		{
			logger.info("::in getCarousalTest::");
	        Carousal carousal=null;
	        SqlRowSet rst=null,rst1=null,rst2=null,rst3=null;
	        String memberType="default";
	        int gameDay=0;
	        ArrayList<Carousal> carousalArray = new ArrayList<Carousal>();
	        String query ="";
	        
			try {
				if(appUserId!=null&&!appUserId.equalsIgnoreCase("")){
					logger.info("::in if appUserId::");
					//query="select * from tbl_headline_master where now() BETWEEN headline_activetime_start AND headline_activetime_end and headline_status='publish' order by sort_order_id";
					query="select * from tbl_headline_master where UTC_TIMESTAMP() BETWEEN headline_activetime_start AND headline_activetime_end and headline_status='publish' order by sort_order_id";
					rst=jdbcTemplateObject.queryForRowSet(query);
					if(rst.next()){
						logger.info("::headline in between start and end::");
						rst=jdbcTemplateObject.queryForRowSet(query);
						while(rst.next()){
							logger.info("::place_segment_id::" +rst.getInt("place_segment_id"));
							logger.info("::audience_segment_id::" +rst.getInt("audience_segment_id"));
							if(rst.getInt("place_segment_id")>0){
								logger.info("::in if place_segment_id::");
								if(userCurrentPlaces.get("userCurrentPlaces")!=null && userCurrentPlaces.get("userCurrentPlaces").size()>0){
										String placeSegmentquery="select * from tbl_place_segments where place_segment_id=?";
										rst1=jdbcTemplateObject.queryForRowSet(placeSegmentquery,new Object[]{rst.getInt("place_segment_id")});
										while(rst1.next()){
											for(HashMap<Object, Object> location:userCurrentPlaces.get("userCurrentPlaces")){
												if(rst1.getString("place_name").equalsIgnoreCase(location.get("placeName").toString())){
													if(rst.getInt("audience_segment_id")>0){
													String audienceSegmentQuery="select * from tbl_audience_segments where audience_segment_id=?";
														rst2=jdbcTemplateObject.queryForRowSet(audienceSegmentQuery,new Object[]{rst.getInt("audience_segment_id")});
														if(rst2.next()){
															String ruleString=rst2.getString("rules_string").replaceAll("\\[", "(property_name='").replaceAll("\\]", "')");
															ruleString=ruleString.replaceAll(" eq ", "' and property_value='");
															ruleString=ruleString.replaceAll(" noteq ", "' and property_value<>'");
															logger.info("ruleString:::"+ruleString);
															rst3=jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and "+ruleString);
															if(rst3.next()){
																carousal = new Carousal();
																carousal.setUrl(rst.getString("headline_image"));
																carousal.setLink(rst.getString("headline_action_url"));
																carousal.setType(rst.getString("headline_action_type"));
																carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
																carousal.setHeadlinetime(getLatestUpdatedNewsTime());
																carousalArray.add(carousal);
															}
														}
													}else{
													carousal = new Carousal();
													carousal.setUrl(rst.getString("headline_image"));
													carousal.setLink(rst.getString("headline_action_url"));
													carousal.setType(rst.getString("headline_action_type"));
													carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
													carousal.setHeadlinetime(getLatestUpdatedNewsTime());
													carousalArray.add(carousal);
													}
												}
											}
										}
								}
							}else if(rst.getInt("audience_segment_id")>0){
								logger.info("::in if audience_segment_id::");
								String audienceSegmentQuery="select * from tbl_audience_segments where audience_segment_id=?";
								rst2=jdbcTemplateObject.queryForRowSet(audienceSegmentQuery,new Object[]{rst.getInt("audience_segment_id")});
								if(rst2.next()){
									String ruleString=rst2.getString("rules_string").replaceAll("\\[", "(property_name='").replaceAll("\\]", "')");
									ruleString=ruleString.replaceAll(" eq ", "' and property_value='");
									ruleString=ruleString.replaceAll(" noteq ", "' and property_value<>'");
									logger.info("ruleString:::"+ruleString);
									rst3=jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and "+ruleString);
									if(rst3.next()){
										carousal = new Carousal();
										carousal.setUrl(rst.getString("headline_image"));
										carousal.setLink(rst.getString("headline_action_url"));
										carousal.setType(rst.getString("headline_action_type"));
										carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
										carousal.setHeadlinetime(getLatestUpdatedNewsTime());
										carousalArray.add(carousal);
									}
								}
							}else{
								logger.info("::in else::");
								carousal = new Carousal();
								carousal.setUrl(rst.getString("headline_image"));
								carousal.setLink(rst.getString("headline_action_url"));
								carousal.setType(rst.getString("headline_action_type"));
								carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
								carousal.setHeadlinetime(getLatestUpdatedNewsTime());
								carousalArray.add(carousal);
							}
						}
					}

					query="select * from tbl_headline_master where headline_activetime_end='0000-00-00 00:00:00' and headline_activetime_end='0000-00-00 00:00:00' and headline_status='publish' order by sort_order_id";
					rst=jdbcTemplateObject.queryForRowSet(query);
					while(rst.next()){
						logger.info("::place_segment_id::" +rst.getInt("place_segment_id"));
						logger.info("::audience_segment_id::" +rst.getInt("audience_segment_id"));
						if(rst.getInt("place_segment_id")>0){
							logger.info("::in place_segment_id::");
							if(userCurrentPlaces.get("userCurrentPlaces")!=null && userCurrentPlaces.get("userCurrentPlaces").size()>0){
								String placeSegmentquery="select * from tbl_place_segments where place_segment_id=?";
								rst1=jdbcTemplateObject.queryForRowSet(placeSegmentquery,new Object[]{rst.getInt("place_segment_id")});
								while(rst1.next()){
									for(HashMap<Object, Object> location:userCurrentPlaces.get("userCurrentPlaces")){
										if(rst1.getString("place_name").equalsIgnoreCase(location.get("placeName").toString())){
											if(rst.getInt("audience_segment_id")>0){
											String audienceSegmentQuery="select * from tbl_audience_segments where audience_segment_id=?";
												rst2=jdbcTemplateObject.queryForRowSet(audienceSegmentQuery,new Object[]{rst.getInt("audience_segment_id")});
												while(rst2.next()){
													String ruleString=rst2.getString("rules_string").replaceAll("\\[", "(property_name='").replaceAll("\\]", "')");
													ruleString=ruleString.replaceAll(" eq ", "' and property_value='");
													ruleString=ruleString.replaceAll(" noteq ", "' and property_value<>'");
													logger.info("ruleString:::"+ruleString);
													rst3=jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and "+ruleString);
													if(rst3.next()){
														carousal = new Carousal();
														carousal.setUrl(rst.getString("headline_image"));
														carousal.setLink(rst.getString("headline_action_url"));
														carousal.setType(rst.getString("headline_action_type"));
														carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
														carousal.setHeadlinetime(getLatestUpdatedNewsTime());
														carousalArray.add(carousal);
													}
												}
											}else{
											carousal = new Carousal();
											carousal.setUrl(rst.getString("headline_image"));
											carousal.setLink(rst.getString("headline_action_url"));
											carousal.setType(rst.getString("headline_action_type"));
											carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
											carousal.setHeadlinetime(getLatestUpdatedNewsTime());
											carousalArray.add(carousal);
											}
										}
									}
								}
							}
						}else if(rst.getInt("audience_segment_id")>0){
							logger.info("::in audience_segment_id::");
							String audienceSegmentQuery="select * from tbl_audience_segments where audience_segment_id=?";
							rst2=jdbcTemplateObject.queryForRowSet(audienceSegmentQuery,new Object[]{rst.getInt("audience_segment_id")});
							if(rst2.next()){
								String ruleString=rst2.getString("rules_string").replaceAll("\\[", "(property_name='").replaceAll("\\]", "')");
								ruleString=ruleString.replaceAll(" eq ", "' and property_value='");
								ruleString=ruleString.replaceAll(" noteq ", "' and property_value<>'");
								logger.info("ruleString:::"+ruleString);
								rst3=jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and "+ruleString);
								if(rst3.next()){
									carousal = new Carousal();
									carousal.setUrl(rst.getString("headline_image"));
									carousal.setLink(rst.getString("headline_action_url"));
									carousal.setType(rst.getString("headline_action_type"));
									carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
									carousal.setHeadlinetime(getLatestUpdatedNewsTime());
									carousalArray.add(carousal);
								}
							}
						}else{
							logger.info("::in else::");
							carousal = new Carousal();
							carousal.setUrl(rst.getString("headline_image"));
							carousal.setLink(rst.getString("headline_action_url"));
							carousal.setType(rst.getString("headline_action_type"));
							carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
							carousal.setHeadlinetime(getLatestUpdatedNewsTime());
							carousalArray.add(carousal);
						}
					}
			}else{
				logger.info("::in else appUserId::");
				query="select * from tbl_headline_master where headline_activetime_end='0000-00-00 00:00:00' and headline_activetime_end='0000-00-00 00:00:00' and place_segment_id is null and audience_segment_id is null and headline_status='published' order by sort_order_id";
				rst=jdbcTemplateObject.queryForRowSet(query);
				while(rst.next()){
					carousal = new Carousal();
					carousal.setUrl(rst.getString("headline_image"));
					carousal.setLink(rst.getString("headline_action_url"));
					carousal.setType(rst.getString("headline_action_type"));
					carousal.setAction(rst.getString("headline_action_type").equalsIgnoreCase("link")?"1":"2");
					carousal.setHeadlinetime(getLatestUpdatedNewsTime());
					carousalArray.add(carousal);
				}
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
		    		query = "select max(updated_time) as lastest_updated_time from tbl_homescreen_carousel_v2";
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
    
  }
