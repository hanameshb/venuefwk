package com.venue.rest.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.vdurmont.emoji.EmojiParser;
import com.venue.rest.model.VenueContents;
import com.venue.rest.model.VenuePhoto;
import com.venue.rest.model.VenueStory;
import com.venue.rest.util.Utility;

@Repository
public class StoryDAO {
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject=null;
	private static Logger logger = Logger.getLogger(StoryDAO.class);
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    }
	
    public ArrayList<VenueStory> getStories(String requestType,String emkitAPIKey,String category,int published_age,String content_type) throws Exception {
		logger.info("::in getStories::");
		SqlRowSet rst=null;
		ArrayList<VenueStory> venueStoryList = new ArrayList<VenueStory>();
		HashMap<Integer, VenueStory> ordinalStoryMap = new HashMap<Integer, VenueStory>();
		
		logger.info("::emkitAPIKey::" +emkitAPIKey);
		logger.info("::category::" +category);
		logger.info("::published_age::" +published_age);
		logger.info("::content_type::" +content_type);
		String storyCategoryName="";
		Date publishDate=null;
		if(category != null && category.length()>0)
		{
			String[] categorytype=category.split(",");
			for(String type : categorytype)	
			{
				storyCategoryName=storyCategoryName+"'"+type+"',";
			}
			storyCategoryName=storyCategoryName.substring(0,storyCategoryName.length()-1);
			logger.info("::storyCategoryName::" +storyCategoryName);
		}
		
		if(published_age > 0)
		{
			Date currentDate = new Date();
	        // convert date to calendar
	        Calendar c = Calendar.getInstance();
	        c.setTime(currentDate);
	        
	        c.add(Calendar.DATE, -published_age);
	        publishDate = c.getTime();
	        logger.info("::publishDate::" +publishDate);
		}
		
		if((category != null && category.length()>0) &&  published_age <= 0 && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if category::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0","publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if(published_age > 0 && (category == null || category.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",publishDate,"publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if((content_type != null && content_type.length()>0) && published_age <= 0 && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.content_type =? and a.status=? ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",content_type,"publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if((category != null &&  category.length()>0) && published_age > 0 && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if category and published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",publishDate,"publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if((category != null &&  category.length()>0) && published_age <= 0 && (content_type != null && content_type.length()>0)){
			logger.info("::if category and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.content_type >=? and a.status=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",content_type,"publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if((content_type != null &&  content_type.length()>0) && published_age > 0 && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if content_type and published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.content_type =? ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",publishDate,"publish",content_type});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else if((category != null &&  category.length()>0) && published_age > 0 && (content_type != null &&  content_type.length()>0)){
			logger.info("::if category and published_age and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.content_type=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0",publishDate,"publish",content_type});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		else {
			logger.info("::in else::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? ORDER BY pubdate desc",new Object[]{emkitAPIKey,"0","publish"});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?",new Object[]{emkitAPIKey,"0","publish"});
			}
		}
		while(rst.next()){
			VenueStory venueStory = new VenueStory();
			venueStory.setVenue_id(rst.getString("venue_id"));
			venueStory.setEmkit_api_key(rst.getString("emkit_api_key"));
			venueStory.setStory_title(rst.getString("title"));
			venueStory.setStory_link(rst.getString("link"));
			if(rst.getString("pubdate")!=null) {
				venueStory.setStory_pubDate(rst.getString("pubdate"));
			}
			else
				venueStory.setStory_pubDate("0000-00-00 00:00:00");
			venueStory.setStory_picture_url(rst.getString("picture_url"));
			venueStory.setPhoto_url(rst.getString("photo_url"));
			venueStory.setStory_id(rst.getString("story_id"));
			venueStory.setStory_name(rst.getString("name"));
			if(rst.getString("html_description") != null) {
				venueStory.setStory_htmldescription(EmojiParser.parseToUnicode(rst.getString("html_description")));
			} else {
				venueStory.setStory_htmldescription(rst.getString("html_description"));
			}
			venueStory.setCategory_name(rst.getString("category_name"));
			venueStory.setStory_author(rst.getString("author"));
			venueStory.setContent_type(rst.getString("content_type"));
			venueStory.setVideo_content_url(rst.getString("video_content_url"));
			venueStory.setWeblink_url(rst.getString("weblink_url"));
			venueStory.setAudio_content_url(rst.getString("audio_content_url"));
			if(rst.getInt("ordinal_override") > 0) {
				ordinalStoryMap.put(rst.getInt("ordinal_override"), venueStory);
			} else {
				venueStoryList.add(venueStory);
			}
		}
		logger.info("::venueStoryList size::" +venueStoryList.size());
		logger.info("::ordinalStoryMap size::" +ordinalStoryMap.size());
		
		for(int ordinalOverride : ordinalStoryMap.keySet()) {
			venueStoryList.add(ordinalOverride-1, ordinalStoryMap.get(ordinalOverride));
		}
		logger.info("::venueStoryList final size::" +venueStoryList.size());
		return venueStoryList;
    }
    public Object getStoriesV2(String requestType,String emkitAPIKey,String category,int published_age,String content_type,String startDate,String stopDate,int page,int size,String updatedSince) throws Exception {
		logger.info("::in getStories::");
		SqlRowSet rst=null;
		ArrayList<VenueStory> venueStoryList = new ArrayList<VenueStory>();
		HashMap<Integer, VenueStory> ordinalStoryMap = new HashMap<Integer, VenueStory>();
		VenueContents venueContents=new VenueContents();
		logger.info("::emkitAPIKey::" +emkitAPIKey);
		logger.info("::category::" +category);
		logger.info("::published_age::" +published_age);
		logger.info("::content_type::" +content_type);
		String storyCategoryName="";
		String publishStartDate=null;
		String publishEndDate=null;
		SqlRowSet storyPageQuery = null;
		int offset = 0;
		int totalPages = 0;
		int totalElements = 0;
		if(!updatedSince.equals("0000-00-00 00:00:00"))
			updatedSince=Utility.getFormattedTimeStamp(updatedSince);
		logger.info("::updatedSince::" +updatedSince);
		if(size==0) {
			size = 500;
			offset = (page-1) * size;
		} 
		else {
			offset = (page-1) * size;
		}

		if(category != null && category.length()>0)
		{
			String[] categorytype=category.split(",");
			for(String type : categorytype)	
			{
				storyCategoryName=storyCategoryName+"'"+type+"',";
			}
			storyCategoryName=storyCategoryName.substring(0,storyCategoryName.length()-1);
			logger.info("::storyCategoryName::" +storyCategoryName);
		}
		if(!stopDate.isEmpty()) {
			logger.info("::in if stop date::"+ stopDate);
			 Date initStopDate =new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(stopDate);  
			 logger.info("::publishEndDate::" +initStopDate.toString());
			 publishEndDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(initStopDate);
			 logger.info("::publishStartDate::" +publishEndDate);
		}
		if(!startDate.isEmpty()) {
			logger.info("::in if Start date::"+startDate);
			Date initStartDate =new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(startDate);  
			 logger.info("::publishStartDate::" +initStartDate.toString());
			 publishStartDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(initStartDate);
			 logger.info("::publishStartDate::" +publishStartDate);
		}
		else if(published_age > 0)
		{
			logger.info("::in else when publish age::" +published_age);
			Date currentDate = new Date();
	        // convert date to calendar
	        Calendar c = Calendar.getInstance();
	        c.setTime(currentDate);
	     
	        c.add(Calendar.DAY_OF_MONTH, -published_age);
	        Date publishDate = c.getTime();
	        logger.info("::in else when publish age::" +c);
	        logger.info("::in else when publish age::" +c.getTime());
	        logger.info("::in else when publish age::" +publishDate);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			publishStartDate=sdf.format(publishDate);
			 logger.info("::publishStartDate::" +publishStartDate);
		}
		
		if((category != null && category.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if category::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ? and stry.category_name in ("+storyCategoryName+")) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince , offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((publishStartDate != null && publishStartDate.length()>0) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ?) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish", updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((content_type != null && content_type.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.content_type =? and cont.status=? and cont.updated_time > ?) as maintable", new Object[]{emkitAPIKey,"0",content_type,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.content_type =? and a.status=? and a.updated_time > ? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",content_type,"publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((publishEndDate != null && publishEndDate.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if publishEndDate::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate <=? and cont.status=? and cont.updated_time > ?) as maintable", new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate <=? and a.status=? and a.updated_time > ? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishStartDate != null && publishStartDate.length()>0) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if category and published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (content_type != null && content_type.length()>0)){
			logger.info("::if category and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.content_type =?  and cont.status=? and cont.updated_time > ? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",content_type,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.content_type =? and a.status=? and a.updated_time > ? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",content_type,"publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishEndDate != null && publishEndDate.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) && (content_type == null || content_type.equalsIgnoreCase(""))){
			logger.info("::if category and publishEndDate::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate <=?  and cont.status=? and cont.updated_time > ? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate <=? and a.status=? and a.updated_time > ?and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((content_type != null &&  content_type.length()>0) && (publishStartDate != null && publishStartDate.length()>0) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if content_type and published_age::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.content_type =?) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.content_type =? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ? ) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish", updatedSince,offset, size});
			}
		}
		else if((content_type != null &&  content_type.length()>0) && (publishEndDate != null && publishEndDate.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if content_type and publishEndDate::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate <=?  and cont.status=? and cont.updated_time > ? and cont.content_type =?) as maintable", new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince,content_type});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate <=? and a.status=? and a.updated_time > ? and a.content_type =? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince,content_type, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((publishStartDate != null &&  publishStartDate.length()>0) && (publishEndDate != null && publishEndDate.length()>0) &&  (content_type == null || content_type.equalsIgnoreCase("")) && (category == null || category.equalsIgnoreCase(""))){
			logger.info("::if publishStartDate and publishEndDate::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.pubdate <=?) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.pubdate <=? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishStartDate != null && publishStartDate.length()>0) &&  (publishEndDate == null || publishEndDate.equalsIgnoreCase(""))&& (content_type != null &&  content_type.length()>0)){
			logger.info("::if category and publishStartDate and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.content_type =? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.content_type=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?  and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishEndDate != null && publishEndDate.length()>0) &&  (publishStartDate == null || publishStartDate.equalsIgnoreCase(""))&& (content_type != null &&  content_type.length()>0)){
			logger.info("::if category and publishEndDate and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate <=?  and cont.status=? and cont.updated_time > ? and cont.content_type =? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince,content_type});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate <=? and a.status=? and a.updated_time > ? and a.content_type=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishEndDate,"publish",updatedSince,content_type, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishStartDate != null && publishStartDate.length()>0) &&  (content_type == null || content_type.equalsIgnoreCase(""))&& (publishEndDate != null &&  publishEndDate.length()>0)){
			logger.info("::if category and publishStartDate and publishEndDate::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.pubdate <=? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.pubdate <=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=?  and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category == null ||  category.equalsIgnoreCase("")) && (publishEndDate != null && publishEndDate.length()>0) &&  (publishStartDate != null || publishStartDate.length()>0) && (content_type != null &&  content_type.length()>0)){
			logger.info("::if publishStartDate and publishEndDate and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.content_type =? and cont.pubdate <=?) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type,publishEndDate});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.content_type=? and a.pubdate <=? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,content_type,publishEndDate, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ? ) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ?  limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else if((category != null &&  category.length()>0) && (publishStartDate != null && publishStartDate.length()>0) &&  (content_type != null && content_type.length()>0) && (publishEndDate != null &&  publishEndDate.length()>0)){
			logger.info("::if category and publishStartDate and publishEndDate and content_type::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.pubdate >=?  and cont.status=? and cont.updated_time > ? and cont.pubdate <=? and cont.content_type=? and stry.category_name in ("+storyCategoryName+")) as maintable", new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate,content_type});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.pubdate >=? and a.status=? and a.updated_time > ? and a.pubdate<=? and a.content_type=? and b.category_name in ("+storyCategoryName+") ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0",publishStartDate,"publish",updatedSince,publishEndDate,content_type, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		else {
			logger.info("::in else::");
			if(requestType.equals("story")){
				logger.info(":::in type story:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_story stry on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? ORDER BY pubdate desc limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}else if (requestType.equals("page")){
				logger.info(":::in type page:::");
				storyPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct cont.content_id) as total_count from tbl_venue_content cont join tbl_venue_page pge on (cont.content_id=stry.content_id) where cont.emkit_api_key=? and cont.mark_for_delete=? and cont.status=? and cont.updated_time > ?) as maintable;", new Object[]{emkitAPIKey,"0","publish",updatedSince});
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and a.updated_time > ? limit ?,?",new Object[]{emkitAPIKey,"0","publish",updatedSince, offset, size});
			}
		}
		while(rst.next()){
			VenueStory venueStory = new VenueStory();
			venueStory.setVenue_id(rst.getString("venue_id"));
			venueStory.setEmkit_api_key(rst.getString("emkit_api_key"));
			venueStory.setStory_title(rst.getString("title"));
			venueStory.setStory_link(rst.getString("link"));
			if(rst.getString("pubdate")!=null) {
				venueStory.setStory_pubDate(rst.getString("pubdate"));
			}
			else
				venueStory.setStory_pubDate("0000-00-00 00:00:00");
			venueStory.setStory_picture_url(rst.getString("picture_url"));
			venueStory.setPhoto_url(rst.getString("photo_url"));
			venueStory.setStory_id(rst.getString("story_id"));
			venueStory.setStory_name(rst.getString("name"));
			if(rst.getString("html_description") != null) {
				venueStory.setStory_htmldescription(EmojiParser.parseToUnicode(rst.getString("html_description")));
			} else {
				venueStory.setStory_htmldescription(rst.getString("html_description"));
			}
			venueStory.setCategory_name(rst.getString("category_name"));
			venueStory.setStory_author(rst.getString("author"));
			venueStory.setContent_type(rst.getString("content_type"));
			venueStory.setVideo_content_url(rst.getString("video_content_url"));
			venueStory.setWeblink_url(rst.getString("weblink_url"));
			venueStory.setAudio_content_url(rst.getString("audio_content_url"));
			venueStory.setStory_external_reference_id(rst.getObject("feed_id") != null ? String.valueOf(rst.getInt("feed_id")): "" );
			if(rst.getInt("ordinal_override") > 0) {
				ordinalStoryMap.put(rst.getInt("ordinal_override"), venueStory);
			} else {
				venueStoryList.add(venueStory);
			}
		}
		if(storyPageQuery != null && storyPageQuery.next()) {
			totalPages = storyPageQuery.getInt("total_pages");
			totalElements = storyPageQuery.getInt("total_count");
		}
		
		logger.info("::venueStoryList size::" +venueStoryList.size());
		logger.info("::ordinalStoryMap size::" +ordinalStoryMap.size());
		
		for(int ordinalOverride : ordinalStoryMap.keySet()) {
			venueStoryList.add(ordinalOverride-1, ordinalStoryMap.get(ordinalOverride));
		}
		if(venueStoryList != null && venueStoryList.size()>0) {
			venueContents.setContentList(venueStoryList);
			venueContents.setSize(size);
			venueContents.setNumber(page);
			venueContents.setTotalPages(totalPages);
			venueContents.setNumberOfElements(venueStoryList.size());
			venueContents.setTotalElements(totalElements);
			if(venueStoryList.size() < size)
				venueContents.setLastPage(true);
			else
				venueContents.setLastPage(false);
		}
		logger.info("::venueStoryList final size::" +venueStoryList.size());
		return venueContents;
    }
    public VenueStory getStoriesByID(String type,String emkitAPIKey,int storyId) {
		logger.info("::in getStories::");
		SqlRowSet rst=null;
		SqlRowSet rstPhotoGallery=null;
		List<VenuePhoto> photoGallery = null;
		VenuePhoto photo = null;
		VenueStory venueStory=new VenueStory();
		try {
			
			logger.info("::emkitAPIKey::" +emkitAPIKey);
			logger.info("::storyId::" +storyId);
			if(type.equals("story")){
				logger.info(":::in type story:::");
				rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_content a join tbl_venue_story b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and b.story_id=?",new Object[]{emkitAPIKey,"0","publish",storyId});
			}else if (type.equals("page")){
				logger.info(":::in type page:::");
				rst = jdbcTemplateObject.queryForRowSet("select *,'' as photo_url,b.page_id as story_id,'' as category_name from tbl_venue_content a join tbl_venue_page b on a.content_id=b.content_id where a.emkit_api_key=? and a.mark_for_delete=? and a.status=? and b.page_id=?",new Object[]{emkitAPIKey,"0","publish",storyId});
			}		
			while(rst.next()){	
				venueStory=new VenueStory();
				venueStory.setVenue_id(rst.getString("venue_id"));
				venueStory.setEmkit_api_key(rst.getString("emkit_api_key"));
				venueStory.setStory_title(rst.getString("title"));
				venueStory.setStory_link(rst.getString("link"));
				if(rst.getString("pubdate")!=null)
					venueStory.setStory_pubDate(rst.getString("pubdate"));
				else
					venueStory.setStory_pubDate("0000-00-00 00:00:00");
				venueStory.setStory_picture_url(rst.getString("picture_url"));
				venueStory.setPhoto_url(rst.getString("photo_url"));
				venueStory.setStory_id(rst.getString("story_id"));
				venueStory.setStory_name(rst.getString("name"));
				if(rst.getString("html_description") != null) {
					venueStory.setStory_htmldescription(EmojiParser.parseToUnicode(rst.getString("html_description")));
				} else {
					venueStory.setStory_htmldescription(rst.getString("html_description"));
				}
				venueStory.setCategory_name(rst.getString("category_name"));
				venueStory.setStory_author(rst.getString("author"));
				venueStory.setContent_type(rst.getString("content_type"));
				venueStory.setVideo_content_url(rst.getString("video_content_url"));
				venueStory.setWeblink_url(rst.getString("weblink_url"));
				venueStory.setAudio_content_url(rst.getString("audio_content_url"));
				
				if (rst.getString("content_type") != null && !rst.getString("content_type").equalsIgnoreCase("")
						&& rst.getString("content_type").equalsIgnoreCase("gallery")) {
					logger.info("::story is of type::" +rst.getString("content_type") +" -- with story id "+storyId);
					rstPhotoGallery = jdbcTemplateObject.queryForRowSet(
							"select * from tbl_venue_photo where content_id=?",
							new Object[] { rst.getString("content_id") });
					if (photoGallery == null) {
						photoGallery = new ArrayList<VenuePhoto>();
					}
					while (rstPhotoGallery.next()) {
						photo = new VenuePhoto();
						photo.setPhotoUrl(rstPhotoGallery.getString("url") !=null ? rstPhotoGallery.getString("url") : "");
						photo.setHeight(rstPhotoGallery.getString("height") != null ? rstPhotoGallery.getString("height") : "");
						photo.setWidth(rstPhotoGallery.getString("width") != null ? rstPhotoGallery.getString("width") : "");
						photoGallery.add(photo);
					}
					venueStory.setPhotoGallery(photoGallery);
				}
			}
		} catch (Exception e) {
			logger.info(":::Exception in getMapsbyLocationV2 DAO:::"+e.getLocalizedMessage());
			e.printStackTrace();
			
		}
	return venueStory;
	}
    
}
