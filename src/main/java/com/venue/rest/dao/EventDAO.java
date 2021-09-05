package com.venue.rest.dao;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.vdurmont.emoji.EmojiParser;
import com.venue.rest.model.CategoriesModel;
import com.venue.rest.model.CommerceConfig;
import com.venue.rest.model.DestinationAddress;
import com.venue.rest.model.EventModel;
import com.venue.rest.model.EventModelV2;
import com.venue.rest.model.EventNotifications;
import com.venue.rest.model.EventNotificationsV2;
import com.venue.rest.model.EventOccurrence;
import com.venue.rest.model.LoadedValueOffsetConfig;
import com.venue.rest.model.PriceCodeAssets;
import com.venue.rest.model.TagsModel;
import com.venue.rest.model.TicketConfig;
import com.venue.rest.model.TicketsAccountConfig;
import com.venue.rest.model.TransportConfig;
import com.venue.rest.model.TransportServices;
import com.venue.rest.model.Tuple;
import com.venue.rest.model.Venue;
import com.venue.rest.model.VenuePlaces;
import com.venue.rest.model.VenueV2;
import com.venue.rest.model.WalletConfig;
import com.venue.rest.util.ErrorMessage;
import com.venue.rest.util.Utility;

@Repository
public class EventDAO {
	
	private static Logger logger = Logger.getLogger(EventDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;
	private NamedParameterJdbcTemplate jdbcNamedParamTemplate;

	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	@Autowired
	CacheManager cacheManager;
	
	EventDAO() {}
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
		jdbcNamedParamTemplate = new NamedParameterJdbcTemplate(dataSourceVenue);
    }
	
	EventDAO(JdbcTemplate jdbcTemplateObject, NamedParameterJdbcTemplate jdbcNamedParamTemplate){
		this.jdbcTemplateObject = jdbcTemplateObject;
		this.jdbcNamedParamTemplate = jdbcNamedParamTemplate;
	}
	
	/**
	 * Method to get Event List JSON Response
	 */
	@Cacheable(value = "EventCache_8")
	public Object getEventList_8(int appId,int venueId,String tagIds,String categoryIds,int page,int size, String startDate, String stopDate) {
		logger.info("::in getEventList_8 DAO::");
		Object eventList = getEventListResponse(appId,venueId,"","","","",tagIds,categoryIds,page,size, startDate, stopDate, "");
		if(eventList != null){
			return eventList;
		}else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to get Event List JSON Response
	 */
	@Cacheable(value = "EventCache")
	public Object getEventList(int appId,int venueId,String tagIds,String categoryIds,int page,int size, String startDate, String stopDate) {
		logger.info("::in getEventList DAO::");
		Object eventList = getEventListResponse(appId,venueId,"","","","",tagIds,categoryIds,page,size, startDate, stopDate, "");
		if(eventList != null){
			return eventList;
		}else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to get Event LookUp JSON Response
	 */
	
	public Object getEventLookUp(int appId,String eventId, String externalEventId1, String externalEventId2, String externalEventId3) {
		logger.info("::in getEventLookUp DAO::");
		Object eventLookUp = getEventListResponse(appId, 0, eventId, externalEventId1, externalEventId2, externalEventId3, "", "", 1, 0, "", "", "singular");
		if(eventLookUp != null){
			return eventLookUp;
		}else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	public Object clearCache(int appId) {
		logger.info("::clearCache for appId::" +appId);
		clearCacheBasedOnKey(appId, "EventCache");
		return "{\"responseCode\":\"200\",\"status\":\"EventList Cache cleared Successfully\"}";
	}
	
	public Object clearCache_8(int appId) {
		logger.info("::clearCache_8 for appId::" +appId);
		clearCacheBasedOnKey(appId, "EventCache_8");
		return "{\"responseCode\":\"200\",\"status\":\"EventList clearCache for app id 8 cleared Successfully\"}";
	}
	
	/**
	 * Method to get Event Tags JSON Response
	 */
	public Object getEventTagsList(int appId,String name) {
		logger.info("::in getEventTagsList DAO::");
		Object eventTagsList = getEventTagsListResponse(appId,name);
		if(eventTagsList != null){
			return eventTagsList;
		}else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to get Event Categories JSON Response
	 */
	public Object getEventcategoriesList(int appId,String name) {
		logger.info("::in getEventTagsList DAO::");
		Object eventcategoriesList = getEventcategoriesListResponse(appId,name);
		if(eventcategoriesList != null){
			return eventcategoriesList;
		}else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	public Object getEventListResponse(int appId, int venueId, String eventId, String externalEventId1, String externalEventId2, 
			String externalEventId3, String tagIds, String categoryIds, int page, int size, String startDate, String stopDate, String apiType) {
		logger.info("::in getEventListResponse::");
		SqlRowSet eventPageQuery = null;
		SqlRowSet eventQuery;
		SqlRowSet lazyQuery;
		ArrayList<EventModel> eventList = new ArrayList<EventModel>();
		Map<Long, EventModel> eventLookup = new HashMap<Long, EventModel>();
		EventNotifications eventNotifications = null;
		EventModel eventModel = null;
		Map<Long, Venue> venueIdLookup = new HashMap<Long, Venue>();
		int offset = 0;
		int totalPages = 0;
		int totalElements = 0;
	
		if(size==0) {
			size = 500;
			offset = (page-1) * size;
		} 
		else {
			offset = (page-1) * size;
		}

		startDate = Utility.getFormattedTimeStamp(startDate);
		stopDate = Utility.getFormattedTimeStamp(stopDate);
		if (isEmpty(startDate)) {
			startDate = "1970-01-01 00:00:01";
		}
		if (isEmpty(stopDate)) {
			stopDate = "2038-01-19 03:14:07";
		}

		logger.info("::page::" +page);
		logger.info("::offset::" +offset);
		logger.info("::size::" +size);
		
		logger.info("::appId::" +appId);
		logger.info("::venueId::" +venueId);
		try {
			String query="";
			if(venueId > 0){
				if(tagIds != null && tagIds.length()> 0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where tag.tag_id in ("+tagIds+") and app_id=? and venue_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId, venueId, "publish", startDate, stopDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and venue_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, stopDate,"publish", appId, venueId, startDate, stopDate,"publish", offset, size});
				}
				else if(categoryIds != null && categoryIds.length()>0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and app_id=? and venue_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId, venueId,"publish", startDate, stopDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and venue_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, stopDate,"publish" , appId, venueId, startDate, stopDate,"publish", offset, size});
				}
				else
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId, venueId,"publish", startDate, stopDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and venue_id=? and event_start_datetime >= ? and event_end_datetime <= ?  and event_status= ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and start >= ? and stop <= ? and evt.event_status= ?)) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, stopDate,"publish" , appId, venueId, startDate, stopDate,"publish", offset, size});
				}
			}
			else{
				if(tagIds != null && tagIds.length()> 0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where tag.tag_id in ("+tagIds+") and app_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId,"publish", startDate, stopDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, stopDate,"publish" , appId, startDate, stopDate,"publish", offset, size});
				}
				else if(categoryIds != null && categoryIds.length()>0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and app_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId,"publish", startDate, stopDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, stopDate,"publish", appId, startDate, stopDate,"publish", offset, size});
				}
				else
				{
					if(eventId != null && !eventId.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and event_id=? and event_status= ?", new Object[]{appId, eventId,"publish"});
					else if(externalEventId1 != null && !externalEventId1.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_1=? and event_status= ?", new Object[]{appId, externalEventId1,"publish"});
					else if(externalEventId2 != null && !externalEventId2.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_2=? and event_status= ?", new Object[]{appId, externalEventId2,"publish"});
					else if(externalEventId3 != null && !externalEventId3.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_3=? and event_status= ?", new Object[]{appId, externalEventId3,"publish"});
					else {
						eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId,"publish", startDate, stopDate, startDate, stopDate});
						query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ?)) as events group by event_id order by start_datetime limit ?,?";
						logger.info("::query::" +query);
						eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, stopDate,"publish", appId, startDate, stopDate,"publish", offset, size});
					}
				}
			}
			
			if(eventPageQuery != null && eventPageQuery.next()) {
				totalPages = eventPageQuery.getInt("total_pages");
				totalElements = eventPageQuery.getInt("total_count");
			}
			
			logger.info("::totalPages::" +totalPages);
			logger.info("::totalElements::" +totalElements);
			if(apiType == null || apiType.equals(""))
				eventNotifications = new EventNotifications();

			while(eventQuery.next()) {
				eventModel = new EventModel();
				eventModel.setEventId(eventQuery.getInt("event_id"));
				eventModel.setEventTitle(eventQuery.getString("event_title"));
				eventModel.setEventShortDescription(eventQuery.getString("event_short_description"));
				
				if(eventQuery.getString("event_description_HTML") != null)
					eventModel.setEventDescriptionHTML(EmojiParser.parseToUnicode(eventQuery.getString("event_description_HTML")));
				
				eventModel.setWebUrl(eventQuery.getString("event_web_url"));
				eventModel.setEventStartDate(eventQuery.getString("event_start_datetime"));
				eventModel.setEventEndDate(eventQuery.getString("event_end_datetime"));
				
				if(eventQuery.getString("external_event_id_1") != null)
					eventModel.setExternalEventId1(eventQuery.getString("external_event_id_1"));
				if(eventQuery.getString("external_event_id_2") != null)
					eventModel.setExternalEventId2(eventQuery.getString("external_event_id_2"));
				if(eventQuery.getString("external_event_id_3") != null)
					eventModel.setExternalEventId3(eventQuery.getString("external_event_id_3"));
				
				if(eventQuery.getString("event_image_1") != null)
					eventModel.setEventImage1(eventQuery.getString("event_image_1"));
				if(eventQuery.getString("event_image_2") != null)
					eventModel.setEventImage2(eventQuery.getString("event_image_2"));
				if(eventQuery.getString("event_image_3") != null)
					eventModel.setEventImage3(eventQuery.getString("event_image_3"));
				
				if(eventQuery.getString("meta_data_1") != null)
					eventModel.setMetaData1(eventQuery.getString("meta_data_1"));
				if(eventQuery.getString("meta_data_2") != null)
					eventModel.setMetaData2(eventQuery.getString("meta_data_2"));
				if(eventQuery.getString("meta_data_3") != null)
					eventModel.setMetaData3(eventQuery.getString("meta_data_3"));
				eventLookup.put((long) eventModel.getEventId(), eventModel);

				Venue venue = venueIdLookup.get(eventQuery.getLong("venue_id"));

				if (venue == null) {
					venue = new Venue();
					venue.setPlaceNames(new ArrayList<VenuePlaces>());
					venueIdLookup.put(eventQuery.getLong("venue_id"), venue);
				}
				eventModel.setVenue(venue);
				eventModel.setWalletConfig(new WalletConfig());
				eventModel.setTicketMasterConfig(new TicketConfig());
				eventModel.getTicketMasterConfig().setPrice_code_assets(new ArrayList<PriceCodeAssets>());
				eventModel.getTicketMasterConfig().setBuyTicketsAccountManagerConfig(new TicketsAccountConfig());
				TransportConfig tc = new TransportConfig();
				tc.setDestinationAddress(new DestinationAddress());
				tc.setTransportServices(new ArrayList<TransportServices>());
				eventModel.setTransportRideshareConfig(tc);
				tc = new TransportConfig();
				tc.setDestinationAddress(new DestinationAddress());
				tc.setTransportServices(new ArrayList<TransportServices>());
				eventModel.setTransportNavigationTrafficConfig(tc);
				
				if(eventModel.getEventStartDate() != null && eventModel.getEventEndDate() != null) {
					EventOccurrence eo = new EventOccurrence();
					eo.setEventStartDate(eventModel.getEventStartDate());
					eo.setEventStopDate(eventModel.getEventEndDate());
					eventModel.getOccurrences().add(eo);
				}
				if(apiType == null || apiType.equals(""))
					eventList.add(eventModel);
			}
			
			MapSqlParameterSource eventIdParameters = new MapSqlParameterSource();
			eventIdParameters.addValue("ids", eventLookup.isEmpty() ? newArrayList(-1L) : eventLookup.keySet());
			//load up transports
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override", eventIdParameters);
			List<String> allowedTypes = Arrays.asList("1","2");
			while(lazyQuery.next()){
				if (lazyQuery.getString("transport_type_id") == null || !allowedTypes.contains(lazyQuery.getString("transport_type_id"))) {
					continue;
				}

				EventModel em = eventLookup.get(lazyQuery.getLong("event_id"));
				if (em != null) {
					TransportServices transportServices = null;
					if (lazyQuery.getString("service_type_id") != null) {
						transportServices = new TransportServices();
						transportServices.setService_type_id(lazyQuery.getInt("service_type_id"));
						transportServices.setService_type(lazyQuery.getString("service_type"));
						transportServices.setService_type_image_url(lazyQuery.getString("service_type_image_url"));
						transportServices.setDeeplink(lazyQuery.getString("deeplink_url"));
						transportServices.setWeburl_ios(lazyQuery.getString("weburl_ios"));
						transportServices.setWeburl_android(lazyQuery.getString("weburl_android"));		
					}

					if (lazyQuery.getString("transport_type_id").equals("1")) {
						if (isEmpty(em.getTransportRideshareConfig().getDestinationAddress().getAddress())) {
							em.getTransportRideshareConfig().setDestinationAddress(buildAddress(lazyQuery));
						}
						if (transportServices != null) {
							em.getTransportRideshareConfig().getTransportServices().add(transportServices);
						}
					}
					if (lazyQuery.getString("transport_type_id").equals("2")) {
						if (isEmpty(em.getTransportNavigationTrafficConfig().getDestinationAddress().getAddress())) {
							em.getTransportNavigationTrafficConfig().setDestinationAddress(buildAddress(lazyQuery));
						}
						if (transportServices != null) {
							em.getTransportNavigationTrafficConfig().getTransportServices().add(transportServices);
						}
					}
				}
			}

			//load up ticket master config
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_ticket_master_config where event_id in (:ids)", eventIdParameters);
			while(lazyQuery.next()){
				EventModel em = eventLookup.get(lazyQuery.getLong("event_id"));
				if (em != null) {
					PriceCodeAssets priceCodeAssets = new PriceCodeAssets();
					priceCodeAssets.setParent_price_code(lazyQuery.getString("parent_price_code"));
					priceCodeAssets.setPrice_code_verbose_description(lazyQuery.getString("price_code_verbose_description"));
					priceCodeAssets.setPrice_code_image_url(lazyQuery.getString("price_code_image_url"));
					em.getTicketMasterConfig().getPrice_code_assets().add(priceCodeAssets);
				}
			}

			//load up tickets account manager Config
			lazyQuery= jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_tickets_account_config where event_id in (:ids)",eventIdParameters);
			while(lazyQuery.next()){
				EventModel em = eventLookup.get(lazyQuery.getLong("event_id"));
				if (em != null) {
					em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setClient_id(lazyQuery.getString("client_id"));
					em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setDsn(lazyQuery.getString("dsn"));
					em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setTm_sdk_api_key(lazyQuery.getString("tm_sdk_api_key"));
					em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setSocialShareText(lazyQuery.getString("social_share"));
				}
			}

			//load up wallet
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_wallet_config where event_id in (:ids)", eventIdParameters);
			while(lazyQuery.next()) {
				EventModel em = eventLookup.get(lazyQuery.getLong("event_id"));
				if (em != null) {
					em.getWalletConfig().setDiscount_card_type(lazyQuery.getString("discount_card_type"));
					em.getWalletConfig().setDiscount_percentage(lazyQuery.getString("discount_percentage"));
					if (!isEmpty(lazyQuery.getString("copy_text")) && lazyQuery.getString("copy_text").contains("\\")) {
						em.getWalletConfig().setCopy_text(lazyQuery.getString("copy_text").replace("\\u00AE", "\u00AE"));
					} else {
						em.getWalletConfig().setCopy_text(lazyQuery.getString("copy_text"));
					}
				}
			}

			//load up venues		
			MapSqlParameterSource venueIdParameters = new MapSqlParameterSource();
			venueIdParameters.addValue("ids", venueIdLookup.isEmpty() ? newArrayList(-1L) : venueIdLookup.keySet());
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)", venueIdParameters);

			while(lazyQuery.next()) {
				Venue venue = venueIdLookup.get(lazyQuery.getLong("venue_id"));
				if (venue != null) {
					if (isEmpty(venue.getVenueName())) {
						venue.setVenueId(lazyQuery.getInt("venue_id"));
						venue.setVenueName(lazyQuery.getString("venue_name"));
						venue.setEmkitLocationId(lazyQuery.getInt("emkit_location_id"));
						venue.setGpsLatitude(lazyQuery.getString("gps_latitude"));
						venue.setGpsLongitude(lazyQuery.getString("gps_longitude"));
						venue.setVenueAddress(lazyQuery.getString("venue_address"));
					}

					if (!isEmpty(lazyQuery.getString("place_name"))) {
						VenuePlaces venuePlaces = new VenuePlaces();
						venuePlaces.setPlaceId(lazyQuery.getInt("place_id"));
						venuePlaces.setPlaceName(lazyQuery.getString("place_name"));
						venue.getPlaceNames().add(venuePlaces);
					}
				}
			}

			//load up cats
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)", eventIdParameters);
			while(lazyQuery.next()) {
				EventModel em;
				if ((em = eventLookup.get(lazyQuery.getLong("event_id"))) != null && !isEmpty(lazyQuery.getString("category_name")) && lazyQuery.getLong("category_id") > 0) {
					em.getCategories().add(new Tuple(lazyQuery.getString("category_name"), lazyQuery.getLong("category_id")));
				}
			}

			//load up tags
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)", eventIdParameters);
			while(lazyQuery.next()) {
				EventModel em;
				if ((em = eventLookup.get(lazyQuery.getLong("event_id"))) != null && !isEmpty(lazyQuery.getString("tag_name")) && lazyQuery.getLong("tag_id") > 0) {
					em.getTags().add(new Tuple(lazyQuery.getString("tag_name"), lazyQuery.getLong("tag_id")));
				}
			}
			
			//load up occurrences
			lazyQuery = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_events_occurrences where event_id in (:ids) order by start", eventIdParameters);
			while(lazyQuery.next()) {
				EventModel em;
				if ((em = eventLookup.get(lazyQuery.getLong("event_id"))) != null && !isEmpty(lazyQuery.getString("start")) && !isEmpty(lazyQuery.getString("stop"))) {
					if(em.getEventStartDate() == null && em.getEventEndDate() == null) {
						em.setEventStartDate(lazyQuery.getString("start"));
						em.setEventEndDate(lazyQuery.getString("stop"));
					}
					EventOccurrence eventOccurrence = new EventOccurrence();
					eventOccurrence.setEventStartDate(lazyQuery.getString("start"));
					eventOccurrence.setEventStopDate(lazyQuery.getString("stop"));
					eventOccurrence.setTicketURL(lazyQuery.getString("ticket_url"));
					em.getOccurrences().add(eventOccurrence);
				}
			}

			if(eventList != null && eventList.size()>0) {
				eventNotifications.setEventList(eventList);
				eventNotifications.setSize(size);
				eventNotifications.setNumber(page);
				eventNotifications.setTotalPages(totalPages);
				eventNotifications.setNumberOfElements(eventList.size());
				eventNotifications.setTotalElements(totalElements);
				if(eventList.size() < size)
					eventNotifications.setLastPage(true);
				else
					eventNotifications.setLastPage(false);
			}

		}catch(Exception e) {
			logger.error("::Exception in getEventListResponse", e);
			e.printStackTrace();
			errorCode = "500";
		}
		if(apiType == null || apiType.equals(""))
			return eventNotifications;
		else
			return eventModel;
	}

	private DestinationAddress buildAddress(SqlRowSet rowSet) {
		DestinationAddress destinationAddress = new DestinationAddress();
		destinationAddress.setGpsLatitude(rowSet.getString("gps_latitude"));
		destinationAddress.setGpsLongitude(rowSet.getString("gps_longitude"));
		destinationAddress.setName(rowSet.getString("name"));
		destinationAddress.setAddress(rowSet.getString("address"));
		return destinationAddress;
	}
	
	private Object getEventTagsListResponse(int appId,String name) {
		logger.info("::in getEventTagsListResponse::");
		SqlRowSet rst = null;
		TagsModel tagsModel = null;
		ArrayList<TagsModel> tagsList = new ArrayList<TagsModel>();

		try {
			logger.info("::appId::" +appId);
			logger.info("::name::" +name);
			if(appId != 0 && appId>0)
			{
				String query="";
				if(name != null && name.length()>0)
					query = "select * from tbl_tags where app_id=? and name like '%"+name+"%'";
				else
					query = "select * from tbl_tags where app_id=?";
				rst=jdbcTemplateObject.queryForRowSet(query,new Object[]{appId});
			
				while(rst != null && rst.next()) {
					tagsModel = new TagsModel();
					tagsModel.setTagId(rst.getInt("tag_id"));
					tagsModel.setName(rst.getString("name"));
					tagsList.add(tagsModel);
				}
			}
			else
			{
				errorCode = "1001";
			}
		} catch(Exception e) {
			logger.error("::Exception in getEventTagsListResponse::"+e.getLocalizedMessage());
			e.printStackTrace();
			errorCode = "500";
			tagsList=null;
			return tagsList;
		}
		return tagsList;
	}
	
	private Object getEventcategoriesListResponse(int appId,String name) {
		logger.info("::in getEventcategoriesListResponse::");
		SqlRowSet rst = null;
		CategoriesModel categoriesModel = null;
		ArrayList<CategoriesModel> categoriesList = new ArrayList<CategoriesModel>();

		try {
			logger.info("::appId::" +appId);
			logger.info("::name::" +name);
			if(appId != 0 && appId>0)
			{
				String query="";
				if(name != null && name.length()> 0)
					query = "select * from tbl_categories where app_id=? and name like '%"+name+"%'";
				else
					query = "select * from tbl_categories where app_id=?";
				rst=jdbcTemplateObject.queryForRowSet(query,new Object[]{appId});
			
				while(rst != null && rst.next()) {
					categoriesModel = new CategoriesModel();
					categoriesModel.setCategoryId(rst.getInt("category_id"));
					categoriesModel.setName(rst.getString("name"));
					categoriesList.add(categoriesModel);
				}
			}
			else
			{
				errorCode = "1001";
			}
		} catch(Exception e) {
			logger.error("::Exception in getEventTagsListResponse::"+e.getLocalizedMessage());
			e.printStackTrace();
			errorCode = "500";
			categoriesList=null;
			return categoriesList;
		}
		return categoriesList;
	}
	
	@Cacheable(value = "V2EventCache")
	public Object getV2EventList(int appId, int venueId, String eventId, String externalEventId1, String externalEventId2,String externalEventId3, String tagIds, 
			String categoryIds, int page, int size, String startDate, String stopDate, String apiType,String updatedSince,String lastModifiedDate) {

		logger.info("::in getV2EventList::");
		SqlRowSet eventPageQuery = null;
		SqlRowSet eventQuery;
		ArrayList<EventModelV2> eventList = new ArrayList<EventModelV2>();
		Map<Long, EventModelV2> eventLookup = new HashMap<Long, EventModelV2>();
		EventNotificationsV2 eventNotifications = null;
		EventModelV2 eventModel = null;
		Map<Long, VenueV2> venueIdLookup = new HashMap<Long, VenueV2>();
		int offset = 0;
		
		if(size==0) {
			size = 500;
			offset = (page-1) * size;
		} else {
			offset = (page-1) * size;
		}
		
		if(!updatedSince.equals("0000-00-00 00:00:00"))
			updatedSince=Utility.getFormattedTimeStamp(updatedSince);
		startDate = Utility.getFormattedTimeStamp(startDate);
		stopDate = Utility.getFormattedTimeStamp(stopDate);
		if (isEmpty(startDate)) {
			startDate = "1970-01-01 00:00:01";
		}
		if (isEmpty(stopDate)) {
			stopDate = "2038-01-19 03:14:07";
		}
		
		logger.info("::appId::" +appId);
		logger.info("::venueId::" +venueId);
		logger.info("::page::" +page);
		logger.info("::offset::" +offset);
		logger.info("::size::" +size);
		logger.info("::updatedSince::" +updatedSince);
		
		SqlRowSet appQuery = jdbcTemplateObject.queryForRowSet(
				"SELECT application_id AS app_id FROM tbl_applications_master WHERE application_id=?",
				new Object[] { appId });
		if (!appQuery.isBeforeFirst()) {
			return new EventNotificationsV2();
		}
		
		try {
			String query="";
			if(venueId > 0){
				if (tagIds != null && tagIds.length()> 0 && categoryIds != null && categoryIds.length()>0) {
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/5) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and tag.tag_id in ("+tagIds+") and app_id=? and venue_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId,venueId,"publish",updatedSince,lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and venue_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")) and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")) and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, startDate, stopDate,"publish", updatedSince, lastModifiedDate, appId, venueId, startDate, startDate, stopDate,"publish", updatedSince, lastModifiedDate, offset, size});
				}
				else if(tagIds != null && tagIds.length()> 0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where tag.tag_id in ("+tagIds+") and app_id=? and venue_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId, venueId, "publish", updatedSince, lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and venue_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, startDate, stopDate,"publish", updatedSince, lastModifiedDate, appId, venueId, startDate, startDate, stopDate,"publish", updatedSince, lastModifiedDate, offset, size});
				}
				else if(categoryIds != null && categoryIds.length()>0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and app_id=? and venue_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId, venueId,"publish",updatedSince, lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and venue_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, startDate, stopDate,"publish" ,updatedSince, lastModifiedDate, appId, venueId, startDate, startDate, stopDate,"publish", updatedSince,lastModifiedDate, offset, size});
				}
				else
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId, venueId,"publish",updatedSince,lastModifiedDate, startDate, startDate, stopDate, startDate,  startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and venue_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?  and event_status= ? and updated_time > ? and updated_time > ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ?)) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, venueId, startDate, startDate, stopDate,"publish" ,updatedSince, lastModifiedDate, appId, venueId, startDate, startDate, stopDate,"publish",updatedSince, lastModifiedDate, offset, size});
				}
			}
			else{
				if (tagIds != null && tagIds.length()> 0 && categoryIds != null && categoryIds.length()>0) {
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/5) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and tag.tag_id in ("+tagIds+") and app_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId,"publish",updatedSince,lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")) and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ?  and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")) and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, startDate, stopDate,"publish" ,updatedSince, lastModifiedDate, appId, startDate, startDate, stopDate,"publish",updatedSince, lastModifiedDate, offset, size});
				}
				else if(tagIds != null && tagIds.length()> 0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where tag.tag_id in ("+tagIds+") and app_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId,"publish",updatedSince,lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and evt.event_id in (select event_id from tbl_event2tag where tag_id in ("+tagIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, startDate, stopDate,"publish" ,updatedSince, lastModifiedDate, appId, startDate, startDate, stopDate,"publish",updatedSince, lastModifiedDate, offset, size});
				}
				else if(categoryIds != null && categoryIds.length()>0)
				{
					eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (" + categoryIds + ") and app_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId,"publish",updatedSince, lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
					query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ? and event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+"))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and evt.event_id in (select event_id from tbl_event2category where category_id in ("+categoryIds+")))) as events group by event_id order by start_datetime limit ?,?";
					logger.info("::query::" +query);
					eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, startDate, stopDate,"publish",updatedSince, lastModifiedDate, appId, startDate, startDate, stopDate,"publish",updatedSince, lastModifiedDate, offset, size});
				}
				else
				{
					if(eventId != null && !eventId.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and event_id=? and event_status= ? and updated_time > ? and updated_time > ? ", new Object[]{appId, eventId,"publish",updatedSince,lastModifiedDate});
					else if(externalEventId1 != null && !externalEventId1.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_1=? and event_status= ? and updated_time > ? and updated_time > ? ", new Object[]{appId, externalEventId1,"publish",updatedSince,lastModifiedDate});
					else if(externalEventId2 != null && !externalEventId2.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_2=? and event_status= ? and updated_time > ? and updated_time > ? ", new Object[]{appId, externalEventId2,"publish",updatedSince,lastModifiedDate});
					else if(externalEventId3 != null && !externalEventId3.equalsIgnoreCase(""))
						eventQuery = jdbcTemplateObject.queryForRowSet("select * from tbl_events where app_id=? and external_event_id_3=? and event_status= ? and updated_time > ? and updated_time > ? ", new Object[]{appId, externalEventId3,"publish",updatedSince,lastModifiedDate});
					else {
						eventPageQuery = jdbcTemplateObject.queryForRowSet("select CEILING(sum(total_count)/"+size+") as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId,"publish",updatedSince, lastModifiedDate, startDate, startDate, stopDate, startDate, startDate, stopDate});
						query = "select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ? and event_status= ? and updated_time > ? and updated_time > ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? )) as events group by event_id order by start_datetime limit ?,?";
						logger.info("::query::" +query);
						eventQuery = jdbcTemplateObject.queryForRowSet(query, new Object[]{appId, startDate, startDate, stopDate,"publish",updatedSince,lastModifiedDate, appId, startDate, startDate, stopDate,"publish",updatedSince,lastModifiedDate, offset, size});
					}
				}
			}
			
			while(eventQuery.next()) {
				eventModel = new EventModelV2();
				eventModel.setEventId(eventQuery.getInt("event_id"));
				eventModel.setEventTitle(eventQuery.getString("event_title"));
				eventModel.setEventShortDescription(eventQuery.getString("event_short_description"));
				
				if(eventQuery.getString("event_description_HTML") != null)
					eventModel.setEventDescriptionHTML(EmojiParser.parseToUnicode(eventQuery.getString("event_description_HTML")));
				
				eventModel.setWebUrl(eventQuery.getString("event_web_url"));
				eventModel.setEventStartDate(eventQuery.getString("event_start_datetime"));
				eventModel.setEventEndDate(eventQuery.getString("event_end_datetime"));
				eventModel.setEventLastModifiedDate(eventQuery.getString("updated_time"));
								
				if(eventQuery.getString("external_event_id_1") != null)
					eventModel.setExternalEventId1(eventQuery.getString("external_event_id_1"));
				if(eventQuery.getString("external_event_id_2") != null)
					eventModel.setExternalEventId2(eventQuery.getString("external_event_id_2"));
				if(eventQuery.getString("external_event_id_3") != null)
					eventModel.setExternalEventId3(eventQuery.getString("external_event_id_3"));
				
				if(eventQuery.getString("event_image_1") != null)
					eventModel.setEventImage1(eventQuery.getString("event_image_1"));
				if(eventQuery.getString("event_image_2") != null)
					eventModel.setEventImage2(eventQuery.getString("event_image_2"));
				if(eventQuery.getString("event_image_3") != null)
					eventModel.setEventImage3(eventQuery.getString("event_image_3"));
				
				if(eventQuery.getString("meta_data_1") != null)
					eventModel.setMetaData1(eventQuery.getString("meta_data_1"));
				if(eventQuery.getString("meta_data_2") != null)
					eventModel.setMetaData2(eventQuery.getString("meta_data_2"));
				if(eventQuery.getString("meta_data_3") != null)
					eventModel.setMetaData3(eventQuery.getString("meta_data_3"));
				eventLookup.put((long) eventModel.getEventId(), eventModel);

				VenueV2 venue = venueIdLookup.get(eventQuery.getLong("venue_id"));

				if (venue == null) {
					venue = new VenueV2();
					venue.setPlaceNames(new ArrayList<VenuePlaces>());
					venueIdLookup.put(eventQuery.getLong("venue_id"), venue);
				}
				eventModel.setVenue(venue);
				eventModel.setWalletConfig(new WalletConfig());
				eventModel.setTicketMasterConfig(new TicketConfig());
				eventModel.getTicketMasterConfig().setPrice_code_assets(new ArrayList<PriceCodeAssets>());
				eventModel.getTicketMasterConfig().setBuyTicketsAccountManagerConfig(new TicketsAccountConfig());
				TransportConfig tc = new TransportConfig();
				tc.setDestinationAddress(new DestinationAddress());
				tc.setTransportServices(new ArrayList<TransportServices>());
				eventModel.setTransportRideshareConfig(tc);
				tc = new TransportConfig();
				tc.setDestinationAddress(new DestinationAddress());
				tc.setTransportServices(new ArrayList<TransportServices>());
				eventModel.setTransportNavigationTrafficConfig(tc);
				
				if(eventModel.getEventStartDate() != null && eventModel.getEventEndDate() != null) {
					EventOccurrence eo = new EventOccurrence();
					eo.setEventStartDate(eventModel.getEventStartDate());
					eo.setEventStopDate(eventModel.getEventEndDate());
					eventModel.getOccurrences().add(eo);
				}
				
				eventModel.setCommerceConfig(new CommerceConfig());
				if(apiType == null || apiType.equals(""))
					eventList.add(eventModel);
			}

			addOtherInfoToEvents(appId, eventLookup, venueIdLookup);
			eventNotifications = prepareEventNotificationsV2(page, size, apiType, eventPageQuery, eventList);
		}catch(Exception e) {
			logger.error("::Exception in getV2EventList", e);
			e.printStackTrace();
			errorCode = "500";
		}
		if(apiType == null || apiType.equals(""))
			return eventNotifications;
		else
			return eventModel;
	}

	private EventNotificationsV2 prepareEventNotificationsV2(int page, int size, String apiType,
			SqlRowSet eventPageQuery, ArrayList<EventModelV2> eventList) {
		int totalPages = 0;
		int totalElements = 0;
		EventNotificationsV2 eventNotifications = null;
		if(eventPageQuery != null && eventPageQuery.next()) {
			totalPages = eventPageQuery.getInt("total_pages");
			totalElements = eventPageQuery.getInt("total_count");
		}
		
		logger.info("::totalPages::" +totalPages);
		logger.info("::totalElements::" +totalElements);
		if(apiType == null || apiType.equals(""))
			eventNotifications = new EventNotificationsV2();
		Utility uty = new Utility();
		String time = uty.determineServerTime();
		if(eventList != null && eventList.size()>0) {
			eventNotifications.setEventList(eventList);
			eventNotifications.setSize(size);
			eventNotifications.setNumber(page);
			eventNotifications.setTotalPages(totalPages);
			eventNotifications.setNumberOfElements(eventList.size());
			eventNotifications.setTotalElements(totalElements);
			eventNotifications.setUpdatedTime(time);
			if(eventList.size() < size)
				eventNotifications.setLastPage(true);
			else
				eventNotifications.setLastPage(false);
		}
		return eventNotifications;
	}

	private void addOtherInfoToEvents(int appId, Map<Long, EventModelV2> eventLookup, Map<Long, VenueV2> venueIdLookup) {
		logger.info("::adding other details to Events::");
		MapSqlParameterSource eventIdParameters = new MapSqlParameterSource();
		eventIdParameters.addValue("ids", eventLookup.isEmpty() ? newArrayList(-1L) : eventLookup.keySet());			
		//load up transports
		addTransportRideShareAndNavToEvents(eventLookup, eventIdParameters);
		//load up ticket master config
		addTicketMasterConfigToEvents(eventLookup, eventIdParameters);
		//load up wallet
		addWalletConfigToEvents(appId, eventLookup, eventIdParameters);
		//load up venues
		addVenueToEvents(venueIdLookup);
		//load up cats
		addCategoriesToEvents(eventLookup, eventIdParameters);
		//load up tags
		addTagsToEvents(eventLookup, eventIdParameters);
		//load up occurrences
		addOccurencesToEvents(eventLookup, eventIdParameters);
		//load up commerce config
		addLoadedValueOffsetToCommerceConfig(eventLookup, eventIdParameters);
	}

	private void addTransportRideShareAndNavToEvents(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding transport details to Events::");
		SqlRowSet transportRowset = jdbcNamedParamTemplate.queryForRowSet("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override", eventIdParameters);
		List<String> allowedTypes = Arrays.asList("1","2");
		while(transportRowset.next()){
			if (transportRowset.getString("transport_type_id") == null || !allowedTypes.contains(transportRowset.getString("transport_type_id"))) {
				continue;
			}

			EventModelV2 em = eventLookup.get(transportRowset.getLong("event_id"));
			if (em != null) {
				TransportServices transportServices = null;
				if (transportRowset.getString("service_type_id") != null) {
					transportServices = new TransportServices();
					transportServices.setService_type_id(transportRowset.getInt("service_type_id"));
					transportServices.setService_type(transportRowset.getString("service_type"));
					transportServices.setService_type_image_url(transportRowset.getString("service_type_image_url"));
					transportServices.setDeeplink(transportRowset.getString("deeplink_url"));
					transportServices.setWeburl_ios(transportRowset.getString("weburl_ios"));
					transportServices.setWeburl_android(transportRowset.getString("weburl_android"));		
				}

				if (transportRowset.getString("transport_type_id").equals("1")) {
					if (isEmpty(em.getTransportRideshareConfig().getDestinationAddress().getAddress())) {
						em.getTransportRideshareConfig().setDestinationAddress(buildAddress(transportRowset));
					}
					if (transportServices != null) {
						em.getTransportRideshareConfig().getTransportServices().add(transportServices);
					}
				}
				if (transportRowset.getString("transport_type_id").equals("2")) {
					if (isEmpty(em.getTransportNavigationTrafficConfig().getDestinationAddress().getAddress())) {
						em.getTransportNavigationTrafficConfig().setDestinationAddress(buildAddress(transportRowset));
					}
					if (transportServices != null) {
						em.getTransportNavigationTrafficConfig().getTransportServices().add(transportServices);
					}
				}
			}
		}
	}

	private void addTicketMasterConfigToEvents(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding ticket master config to Events::");
		SqlRowSet ticketMasterConfigRowset = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_ticket_master_config where event_id in (:ids)", eventIdParameters);
		while(ticketMasterConfigRowset.next()){
			EventModelV2 em = eventLookup.get(ticketMasterConfigRowset.getLong("event_id"));
			if (em != null) {
				PriceCodeAssets priceCodeAssets = new PriceCodeAssets();
				priceCodeAssets.setParent_price_code(ticketMasterConfigRowset.getString("parent_price_code"));
				priceCodeAssets.setPrice_code_verbose_description(ticketMasterConfigRowset.getString("price_code_verbose_description"));
				priceCodeAssets.setPrice_code_image_url(ticketMasterConfigRowset.getString("price_code_image_url"));
				em.getTicketMasterConfig().getPrice_code_assets().add(priceCodeAssets);
			}
		}
		addTicketAccountManagerConfigToTicketMasterConfig(eventLookup, eventIdParameters);
	}

	private void addTicketAccountManagerConfigToTicketMasterConfig(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding buy ticket account manager config to ticket master config::");
		SqlRowSet ticketManagerConfig = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_tickets_account_config where event_id in (:ids)",eventIdParameters);
		while(ticketManagerConfig.next()){
			EventModelV2 em = eventLookup.get(ticketManagerConfig.getLong("event_id"));
			if (em != null) {
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setClient_id(ticketManagerConfig.getString("client_id"));
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setDsn(ticketManagerConfig.getString("dsn"));
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setTm_sdk_api_key(ticketManagerConfig.getString("tm_sdk_api_key"));
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setIos_sdk_api_key(ticketManagerConfig.getString("ios_sdk_api_key"));
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setAndroid_sdk_api_key(ticketManagerConfig.getString("android_sdk_api_key"));
				em.getTicketMasterConfig().getBuyTicketsAccountManagerConfig().setSocialShareText(ticketManagerConfig.getString("social_share"));
			}
		}
	}

	private void addWalletConfigToEvents(int appId, Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding wallet config to Events::");
		SqlRowSet walletConfigRowset = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_wallet_config where event_id in (:ids)", eventIdParameters);
		while(walletConfigRowset.next()) {
			EventModelV2 em = eventLookup.get(walletConfigRowset.getLong("event_id"));
			if (em != null) {
				em.getWalletConfig().setDiscount_card_type(walletConfigRowset.getString("discount_card_type"));
				em.getWalletConfig().setDiscount_percentage(walletConfigRowset.getString("discount_percentage"));
				em.getWalletConfig().setMerchant_id(walletConfigRowset.getString("merchant_id"));
				if (!isEmpty(walletConfigRowset.getString("copy_text")) && walletConfigRowset.getString("copy_text").contains("\\")) {
					em.getWalletConfig().setCopy_text(walletConfigRowset.getString("copy_text").replace("\\u00AE", "\u00AE"));
				} else {
					em.getWalletConfig().setCopy_text(walletConfigRowset.getString("copy_text"));
				}
			}
		}
	}

	private void addVenueToEvents(Map<Long, VenueV2> venueIdLookup) {
		MapSqlParameterSource venueIdParameters = new MapSqlParameterSource();
		venueIdParameters.addValue("ids", venueIdLookup.isEmpty() ? newArrayList(-1L) : venueIdLookup.keySet());
		logger.debug("::adding Venue to Events::");
		SqlRowSet venueRowset = jdbcNamedParamTemplate.queryForRowSet("select master.*, map.place_id as place_id, map.place_name as place_name from tbl_venue_master master left join tbl_venue_place_map map on map.venue_id = master.venue_id where master.venue_id in (:ids)", venueIdParameters);

		while(venueRowset.next()) {
			VenueV2 venue = venueIdLookup.get(venueRowset.getLong("venue_id"));
			if (venue != null) {
				if (isEmpty(venue.getVenueName())) {
					venue.setVenueId(venueRowset.getInt("venue_id"));
					venue.setVenueName(venueRowset.getString("venue_name"));
					venue.setEmkitLocationId(new ArrayList<Integer>());
					venue.setGpsLatitude(venueRowset.getString("gps_latitude"));
					venue.setGpsLongitude(venueRowset.getString("gps_longitude"));
					venue.setVenueAddress(venueRowset.getString("venue_address"));
				}

				if (!isEmpty(venueRowset.getString("place_name"))) {
					VenuePlaces venuePlaces = new VenuePlaces();
					venuePlaces.setPlaceId(venueRowset.getInt("place_id"));
					venuePlaces.setPlaceName(venueRowset.getString("place_name"));
					venue.getPlaceNames().add(venuePlaces);
				}
			}
		}
		addemkitLocationIdToVenue(venueIdLookup, venueIdParameters);
	}

	private void addemkitLocationIdToVenue(Map<Long, VenueV2> venueIdLookup, MapSqlParameterSource venueIdParameters) {
		logger.debug("::adding emkitLocationId to Venue::");
		SqlRowSet emkitLocationIdRowset = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_venue_location_map where venue_id in (:ids)", venueIdParameters);
		while(emkitLocationIdRowset.next()) {
			VenueV2 venue = venueIdLookup.get(emkitLocationIdRowset.getLong("venue_id"));
			if (venue != null) {
				ArrayList<Integer> eMkitLocations=venue.getEmkitLocationId();
				eMkitLocations.add(emkitLocationIdRowset.getInt("emkit_location_id"));
			}
		}
	}

	private void addCategoriesToEvents(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding categories to Events::");
		SqlRowSet categoriesRowset = jdbcNamedParamTemplate.queryForRowSet("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)", eventIdParameters);
		while(categoriesRowset.next()) {
			EventModelV2 em;
			if ((em = eventLookup.get(categoriesRowset.getLong("event_id"))) != null && !isEmpty(categoriesRowset.getString("category_name")) && categoriesRowset.getLong("category_id") > 0) {
				em.getCategories().add(new Tuple(categoriesRowset.getString("category_name"), categoriesRowset.getLong("category_id")));
			}
		}
	}

	private void addTagsToEvents(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding tags to Events::");
		SqlRowSet tagsRowset = jdbcNamedParamTemplate.queryForRowSet("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)", eventIdParameters);
		while(tagsRowset.next()) {
			EventModelV2 em;
			if ((em = eventLookup.get(tagsRowset.getLong("event_id"))) != null && !isEmpty(tagsRowset.getString("tag_name")) && tagsRowset.getLong("tag_id") > 0) {
				em.getTags().add(new Tuple(tagsRowset.getString("tag_name"), tagsRowset.getLong("tag_id")));
			}
		}
	}

	private void addOccurencesToEvents(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding occurences to Events::");
		SqlRowSet occurencesRowset = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_events_occurrences where event_id in (:ids) order by start", eventIdParameters);
		while(occurencesRowset.next()) {
			EventModelV2 em;
			if ((em = eventLookup.get(occurencesRowset.getLong("event_id"))) != null && !isEmpty(occurencesRowset.getString("start")) && !isEmpty(occurencesRowset.getString("stop"))) {
				if(em.getEventStartDate() == null && em.getEventEndDate() == null) {
					em.setEventStartDate(occurencesRowset.getString("start"));
					em.setEventEndDate(occurencesRowset.getString("stop"));
				}
				EventOccurrence eventOccurrence = new EventOccurrence();
				eventOccurrence.setEventStartDate(occurencesRowset.getString("start"));
				eventOccurrence.setEventStopDate(occurencesRowset.getString("stop"));
				eventOccurrence.setTicketURL(occurencesRowset.getString("ticket_url"));
				eventOccurrence.setStartTimeTBD(occurencesRowset.getBoolean("start_time_tbd"));
				em.getOccurrences().add(eventOccurrence);
			}
		}
	}

	private void addLoadedValueOffsetToCommerceConfig(Map<Long, EventModelV2> eventLookup, MapSqlParameterSource eventIdParameters) {
		logger.debug("::adding LoadedValueOffset to CommerceConfig::");
		SqlRowSet loadedValueOffsetRowset = jdbcNamedParamTemplate.queryForRowSet("select * from tbl_event_commerce_config where event_id in (:ids)", eventIdParameters);
		LoadedValueOffsetConfig loadedValueOffsetConfig = null;
		EventModelV2 em = null;
		while(loadedValueOffsetRowset.next()) {
			if ((em = eventLookup.get(loadedValueOffsetRowset.getLong("event_id"))) != null && !isEmpty(loadedValueOffsetRowset.getString("end_date_offset"))) {
				loadedValueOffsetConfig = new LoadedValueOffsetConfig();
				loadedValueOffsetConfig.setOffset_start_in_hours(Integer.valueOf(loadedValueOffsetRowset.getString("start_date_offset").trim()));
				loadedValueOffsetConfig.setOffset_end_in_hours(Integer.valueOf(loadedValueOffsetRowset.getString("end_date_offset").trim()));
				em.getCommerceConfig().setLoadedValueOffsetConfig(loadedValueOffsetConfig);
			}
		}
	}
	
	public Object clearV2EventCache(int appId) {
		logger.info("::clearV2EventCache for appId::" +appId);
		clearCacheBasedOnKey(appId, "V2EventCache");
		return "{\"responseCode\":\"200\",\"status\":\"EventList V2EventCache cleared Successfully\"}";
	}
	
	private void clearCacheBasedOnKey(int appId, String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		ConcurrentHashMap<SimpleKey, Object> map = (ConcurrentHashMap<SimpleKey, Object>) cache.getNativeCache();
		for(SimpleKey cacheKey : map.keySet()) {
			if(cacheKey.toString().startsWith("SimpleKey ["+appId)) {
				cache.evict(cacheKey);
			}
		}
	}
}