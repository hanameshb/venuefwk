package com.venue.rest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Matchers.eq;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.venue.rest.model.CommerceConfig;
import com.venue.rest.model.EventModel;
import com.venue.rest.model.EventNotifications;
import com.venue.rest.model.EventNotificationsV2;
import com.venue.rest.model.LoadedValueOffsetConfig;

@RunWith(MockitoJUnitRunner.class)
public class EventDAOTest {

	private static Logger logger = Logger.getLogger(EventDAOTest.class);
	
	private static final int appId = 1;
	private static final int venueId = 0;
	private static final String eventId = "1";
	private static final String eventStatus = "publish";
	private static final String externalEventId1 = "r470";
	private static final String externalEventId2 = "APP_MatchPlay";
	private static final String externalEventId3 = "4108";
	private static final String tagIds = "";
	private static final String categoryIds = "";
	private static final int page = 1;
	private static final int size = 500;
	private static final int offset = 0;
	private static final String startDate = "";
	private static final String stopDate = "";
	private static final String updatedSince = "";
	private static final String apiType = "singular"; //for single event "singular" and for multiple events ""
	private static final String commerceConfigLoadedValueStartOffset = "-2";
	private static final String commerceConfigLoadedValueEndOffset = "3";
	
	private EventDAO dao;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcNamedParamTemplate;
	private SqlRowSet sqlRowSetMock;
	private Map<SqlRowSet, MockRow> mockRows;
	
	@Before
	public void setUp() {
		jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		jdbcNamedParamTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
		// you need to add another constructor to EventDAO to accept an external JDBCTemplate instance rather because default constructor calls initializeDB internally
		// which tries to create a real JDBCTemplate from spring config. For testing EventDAO you don't want it doing that, instead you want to pass in a 'mocked' 
		// JDBCTemplate instance to EventDAO here instead.
		dao = new EventDAO(jdbcTemplate, jdbcNamedParamTemplate);
	    mockRows = new HashMap<SqlRowSet, MockRow>();
	}
	
	/**
	 * Test method to checkEventLookUpWithAppId
	 * @throws Exception
	 */
	@Test
	public void checkEventLookUpWithAppId() throws Exception {
		System.out.println("*******Executing test case for checkEventLookUpWithAppId********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
     	
        // define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_events where app_id=? and event_id=? and event_status= ?", new Object[]{appId, eventId,eventStatus});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","");
		occurrencesResult.get(0).put("stop","");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventModel eventModel = (EventModel) dao.getEventListResponse(appId, venueId, eventId, externalEventId1, externalEventId2, externalEventId3, 
        		tagIds, categoryIds, page, size, startDate, stopDate, apiType);
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventModel);
		assertEquals(eventModel.getExternalEventId1(), "r470");
		assertEquals(eventModel.getExternalEventId3(), "4108");
		assertEquals(eventModel.getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventModel.getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	
	/**
	 * Test method to checkEventLookUpWithAppIdAndExternalEventId1
	 * @throws Exception
	 */
	@Test
	public void checkEventLookUpWithAppIdAndExternalEventId1() throws Exception {
		System.out.println("*******Executing test case for checkEventLookUpWithAppIdAndExternalEventId1********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_events where app_id=? and external_event_id_1=? and event_status= ?", new Object[]{appId,externalEventId1,eventStatus});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","");
		occurrencesResult.get(0).put("stop","");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventModel eventModel = (EventModel) dao.getEventListResponse(appId, venueId, "", externalEventId1, externalEventId2, externalEventId3, 
        		tagIds, categoryIds, page, size, startDate, stopDate, apiType);
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventModel);
		assertEquals(eventModel.getExternalEventId1(), "r470");
		assertEquals(eventModel.getExternalEventId3(), "4108");
		assertEquals(eventModel.getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventModel.getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	
	/**
	 * Test method to checkEventLookUpWithAppIdAndExternalEventId2
	 * @throws Exception
	 */
	@Test
	public void checkEventLookUpWithAppIdAndExternalEventId2() throws Exception {
		System.out.println("*******Executing test case for checkEventLookUpWithAppIdAndExternalEventId2********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_events where app_id=? and external_event_id_2=? and event_status= ?", new Object[]{appId,externalEventId2,eventStatus});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","");
		occurrencesResult.get(0).put("stop","");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventModel eventModel = (EventModel) dao.getEventListResponse(appId, venueId, "", "", externalEventId2, externalEventId3, 
        		tagIds, categoryIds, page, size, startDate, stopDate, apiType);
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventModel);
		assertEquals(eventModel.getExternalEventId1(), "r470");
		assertEquals(eventModel.getExternalEventId3(), "4108");
		assertEquals(eventModel.getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventModel.getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	
	/**
	 * Test method to checkEventLookUpWithAppIdAndExternalEventId3
	 * @throws Exception
	 */
	@Test
	public void checkEventLookUpWithAppIdAndExternalEventId3() throws Exception {
		System.out.println("*******Executing test case for checkEventLookUpWithAppIdAndExternalEventId3********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from tbl_events where app_id=? and external_event_id_3=? and event_status= ?", new Object[]{appId,externalEventId3,eventStatus});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","");
		occurrencesResult.get(0).put("stop","");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventModel eventModel = (EventModel) dao.getEventListResponse(appId, venueId, "", "", "", externalEventId3, 
        		tagIds, categoryIds, page, size, startDate, stopDate, apiType);
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventModel);
		assertEquals(eventModel.getExternalEventId1(), "r470");
		assertEquals(eventModel.getExternalEventId3(), "4108");
		assertEquals(eventModel.getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventModel.getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	
	/**
	 * Test method to checkEventListWithAppId
	 * @throws Exception
	 */
	@Test
	public void checkEventListWithAppId() throws Exception {
		System.out.println("*******Executing test case for checkEventListWithAppId********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
		// Fake results for event page sql query
		List<Map<String,String>> eventPageResult = new ArrayList<Map<String,String>>();
		eventPageResult.add(new HashMap<String,String>());
		eventPageResult.get(0).put("total_pages","1");
		eventPageResult.get(0).put("total_count","10");
		doReturn(generateFakeRowSet(eventPageResult)).when(jdbcTemplate).queryForRowSet("select CEILING(sum(total_count)/500) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and evt.event_status= ?and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId, eventStatus,"1970-01-01 00:00:01", "2038-01-19 03:14:07", "1970-01-01 00:00:01", "2038-01-19 03:14:07"});
		
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
     	doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ?)) as events group by event_id order by start_datetime limit ?,?", new Object[]{appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, offset, size});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","2017-09-29 00:39:51");
		occurrencesResult.get(0).put("stop","2017-09-30 05:39:51");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventNotifications eventsList = (EventNotifications) dao.getEventListResponse(appId, venueId, "", "", "", "", 
        		tagIds, categoryIds, page, size, startDate, stopDate, "");
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventsList);
		assertEquals(eventsList.getEventList().get(0).getExternalEventId1(), "r470");
		assertEquals(eventsList.getEventList().get(0).getExternalEventId3(), "4108");
		assertEquals(eventsList.getEventList().get(0).getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventsList.getEventList().get(0).getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	
	/**
	 * Test method to checkEventListWithAppIdAndCategoryIdFilter
	 * @throws Exception
	 */
	@Test
	public void checkEventListWithAppIdAndCategoryIdFilter() throws Exception {
		System.out.println("*******Executing test case for checkEventListWithAppIdAndCategoryIdFilter********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
		// Fake results for event page sql query
		List<Map<String,String>> eventPageResult = new ArrayList<Map<String,String>>();
		eventPageResult.add(new HashMap<String,String>());
		eventPageResult.get(0).put("total_pages","1");
		eventPageResult.get(0).put("total_count","10");
		doReturn(generateFakeRowSet(eventPageResult)).when(jdbcTemplate).queryForRowSet("select CEILING(sum(total_count)/500) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2category cat on (evt.event_id=cat.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where cat.category_id in (1) and app_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId,eventStatus, "1970-01-01 00:00:01", "2038-01-19 03:14:07", "1970-01-01 00:00:01", "2038-01-19 03:14:07"});
		
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
     	doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2category where category_id in (1))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2category where category_id in (1)))) as events group by event_id order by start_datetime limit ?,?", new Object[]{appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07", eventStatus,appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, offset, size});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","test_cat");
		categoriesResult.get(0).put("category_id","1");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","2017-09-29 00:39:51");
		occurrencesResult.get(0).put("stop","2017-09-30 05:39:51");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventNotifications eventsList = (EventNotifications) dao.getEventListResponse(appId, venueId, "", "", "", "", 
        		tagIds, "1", page, size, startDate, stopDate, "");
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventsList);
		assertEquals(eventsList.getEventList().get(0).getCategories().get(0).getName(), "test_cat");
	}
	
	/**
	 * Test method to checkEventListWithAppIdAndTagIdFilter
	 * @throws Exception
	 */
	@Test
	public void checkEventListWithAppIdAndTagIdFilter() throws Exception {
		System.out.println("*******Executing test case for checkEventListWithAppIdAndTagIdFilter********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
		// Fake results for event page sql query
		List<Map<String,String>> eventPageResult = new ArrayList<Map<String,String>>();
		eventPageResult.add(new HashMap<String,String>());
		eventPageResult.get(0).put("total_pages","1");
		eventPageResult.get(0).put("total_count","10");
		doReturn(generateFakeRowSet(eventPageResult)).when(jdbcTemplate).queryForRowSet("select CEILING(sum(total_count)/500) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt join tbl_event2tag tag on (evt.event_id=tag.event_id) left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where tag.tag_id in (1) and app_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId,eventStatus, "1970-01-01 00:00:01", "2038-01-19 03:14:07", "1970-01-01 00:00:01", "2038-01-19 03:14:07"});
		
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
     	doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and event_start_datetime >= ? and event_end_datetime <= ? and event_status= ? and event_id in (select event_id from tbl_event2tag where tag_id in (1))) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and start >= ? and stop <= ? and evt.event_status= ? and evt.event_id in (select event_id from tbl_event2tag where tag_id in (1)))) as events group by event_id order by start_datetime limit ?,?", new Object[]{appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, appId, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, offset, size});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","test_tag");
		tagsResult.get(0).put("tag_id","1");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","2017-09-29 00:39:51");
		occurrencesResult.get(0).put("stop","2017-09-30 05:39:51");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventNotifications eventsList = (EventNotifications) dao.getEventListResponse(appId, venueId, "", "", "", "", 
        		"1", "", page, size, startDate, stopDate, "");
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventsList);
		assertEquals(eventsList.getEventList().get(0).getTags().get(0).getName(), "test_tag");
	}
	
	/**
	 * Test method to checkEventListWithAppIdAndVenueId
	 * @throws Exception
	 */
	@Test
	public void checkEventListWithAppIdAndVenueId() throws Exception {
		System.out.println("*******Executing test case for checkEventListWithAppIdAndVenueId********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
		// Fake results for event page sql query
		List<Map<String,String>> eventPageResult = new ArrayList<Map<String,String>>();
		eventPageResult.add(new HashMap<String,String>());
		eventPageResult.get(0).put("total_pages","1");
		eventPageResult.get(0).put("total_count","10");
		doReturn(generateFakeRowSet(eventPageResult)).when(jdbcTemplate).queryForRowSet("select CEILING(sum(total_count)/500) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and evt.event_status= ? and ((event_start_datetime >= ? and event_end_datetime <= ?) OR (start >= ? and stop <= ?))) as maintable", new Object[]{appId, 1,eventStatus, "1970-01-01 00:00:01", "2038-01-19 03:14:07", "1970-01-01 00:00:01", "2038-01-19 03:14:07"});
		
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
     	doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events where app_id=? and venue_id=? and event_start_datetime >= ? and event_end_datetime <= ?  and event_status= ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3 from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and start >= ? and stop <= ? and evt.event_status= ?)) as events group by event_id order by start_datetime limit ?,?", new Object[]{appId, 1, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, appId, 1, "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus, offset, size});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","10");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name,locmap.emkit_location_id as emkit_location_id from tbl_venue_master master left join tbl_venue_place_map map  on map.venue_id = master.venue_id join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","2017-09-29 00:39:51");
		occurrencesResult.get(0).put("stop","2017-09-30 05:39:51");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		EventNotifications eventsList = (EventNotifications) dao.getEventListResponse(appId, 1, "", "", "", "", 
        		tagIds, categoryIds, page, size, startDate, stopDate, "");
		
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventsList);
		assertEquals(eventsList.getEventList().get(0).getExternalEventId1(), "r470");
		assertEquals(eventsList.getEventList().get(0).getExternalEventId3(), "4108");
		assertEquals(eventsList.getEventList().get(0).getVenue().getVenueName(), "fake_venue");
		assertEquals(eventsList.getEventList().get(0).getVenue().getEmkitLocationId(), 10);
		assertEquals(eventsList.getEventList().get(0).getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventsList.getEventList().get(0).getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
	}
	@Test
	public void checkEventListWithAppIdAndVenueIdWithMultipleLocation() throws Exception {
		System.out.println("*******Executing test case for checkEventListWithAppIdAndVenueIdWithMultipleLocation********");
		// build the list of fake result sets that should be returned for each invocation of jdbcTemplate.queryFroRowSet() that getEventListResponse() performs
        
		// Fake results for event page sql query
		List<Map<String,String>> eventPageResult = new ArrayList<Map<String,String>>();
		eventPageResult.add(new HashMap<String,String>());
		eventPageResult.get(0).put("total_pages","1");
		eventPageResult.get(0).put("total_count","10");
		doReturn(generateFakeRowSet(eventPageResult)).when(jdbcTemplate).queryForRowSet("select CEILING(sum(total_count)/500) as total_pages,sum(total_count) as total_count from (select count(distinct evt.event_id) as total_count from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ? and (((event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?) OR ((start >= ? or ? <= stop) and stop <= ?))) as maintable", new Object[]{appId, 1,eventStatus,"0000-00-00 00:00:00", "0000-00-00 00:00:00","1970-01-01 00:00:01", "1970-01-01 00:00:01", "2038-01-19 03:14:07", "1970-01-01 00:00:01", "1970-01-01 00:00:01", "2038-01-19 03:14:07"});
		
        // Fake results for events sql query
		List<Map<String,String>> eventResult = new ArrayList<Map<String,String>>();
        eventResult.add(new HashMap<String,String>());
        eventResult.get(0).put("event_id","1");
        eventResult.get(0).put("event_title","fake_title_1");
     	eventResult.get(0).put("event_short_description","fake_desc_1");
     	eventResult.get(0).put("event_description_HTML","fake_desc_html_1");
     	eventResult.get(0).put("event_web_url","fake_web_url_1");
     	eventResult.get(0).put("event_start_datetime","2017-09-29 00:39:51");
     	eventResult.get(0).put("event_end_datetime","2017-09-30 05:39:51");
     	eventResult.get(0).put("external_event_id_1","r470");
     	eventResult.get(0).put("external_event_id_2","APP_MatchPlay");
     	eventResult.get(0).put("external_event_id_3","4108");
     	eventResult.get(0).put("event_image_1","");
     	eventResult.get(0).put("event_image_2","");
     	eventResult.get(0).put("event_image_3","");
     	eventResult.get(0).put("meta_data_1","");
     	eventResult.get(0).put("meta_data_2","");
     	eventResult.get(0).put("meta_data_3","");
     	eventResult.get(0).put("updated_time","2017-09-30 05:39:51");
     	eventResult.get(0).put("venue_id","1");
        // define the matcher that will assign this fake result set to the correct sql query execution
     	doReturn(generateFakeRowSet(eventResult)).when(jdbcTemplate).queryForRowSet("select * from ((select event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,event_start_datetime as event_start_datetime,event_end_datetime as event_end_datetime,event_start_datetime as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events where app_id=? and venue_id=? and (event_start_datetime >= ? or ? <= event_end_datetime) and event_end_datetime <= ?  and event_status= ? and updated_time > ? and updated_time > ?) UNION ALL (select evt.event_id as event_id,venue_id as venue_id,event_title as event_title,event_short_description as event_short_description,event_description_HTML as event_description_HTML,event_web_url as event_web_url,NULL as event_start_datetime,NULL as event_end_datetime,start as start_datetime,external_event_id_1 as external_event_id_1,external_event_id_2 as external_event_id_2,external_event_id_3 as external_event_id_3,event_image_1 as event_image_1,event_image_2 as event_image_2,event_image_3 as event_image_3,meta_data_1 as meta_data_1,meta_data_2 as meta_data_2,meta_data_3 as meta_data_3,updated_time as updated_time from tbl_events evt left join tbl_events_occurrences occ on (occ.event_id=evt.event_id) where app_id=? and venue_id=? and (start >= ? or ? <= stop) and stop <= ? and evt.event_status= ? and evt.updated_time > ? and evt.updated_time > ?)) as events group by event_id order by start_datetime limit ?,?", new Object[]{appId, 1, "1970-01-01 00:00:01", "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus,"0000-00-00 00:00:00","0000-00-00 00:00:00", appId, 1, "1970-01-01 00:00:01", "1970-01-01 00:00:01", "2038-01-19 03:14:07",eventStatus,"0000-00-00 00:00:00","0000-00-00 00:00:00", offset, size});
        
		// Fake results for event transports sql query
		List<Map<String,String>> transportResult = new ArrayList<Map<String,String>>();
		transportResult.add(new HashMap<String,String>());
		transportResult.get(0).put("transport_type_id","1");
		transportResult.get(0).put("event_id","1");
		transportResult.get(0).put("service_type_id","103");
		transportResult.get(0).put("service_type","fake_type");
		transportResult.get(0).put("service_type_image_url","fake_type_image_url");
		transportResult.get(0).put("deeplink_url","fake_deeplink_url");
		transportResult.get(0).put("weburl_ios","fake_weburl_ios");
		transportResult.get(0).put("weburl_android","fake_weburl_android");
		transportResult.get(0).put("ordinal_override","fake_ordinal_override");
		transportResult.get(0).put("gps_latitude","32.71573");
		transportResult.get(0).put("gps_longitude","-117.1610838");
		transportResult.get(0).put("name","test");
		transportResult.get(0).put("address","fake_address");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(transportResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select config.*, services.service_type_id as service_type_id, services.service_type as service_type, services.service_type_image_url as service_type_image_url, services.deeplink_url as deeplink_url, services.weburl_ios as weburl_ios, services.weburl_android as weburl_android, services.ordinal_override as ordinal_override from tbl_event_transport_config config left join tbl_event_transport_services services on (services.event_id=config.event_id and services.transport_type_id=config.transport_type_id) where config.event_id in (:ids) order by services.ordinal_override"), any(MapSqlParameterSource.class));

		// Fake results for event ticket master config sql query
		List<Map<String,String>> ticketMasterResult = new ArrayList<Map<String,String>>();
		ticketMasterResult.add(new HashMap<String,String>());
		ticketMasterResult.get(0).put("event_id","1");
		ticketMasterResult.get(0).put("parent_price_code","103");
		ticketMasterResult.get(0).put("price_code_verbose_description","fake_price_code_desc");
		ticketMasterResult.get(0).put("price_code_image_url","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketMasterResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_ticket_master_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event tickets account manager Config sql query
		List<Map<String,String>> ticketsAccountResult = new ArrayList<Map<String,String>>();
		ticketsAccountResult.add(new HashMap<String,String>());
		ticketsAccountResult.get(0).put("event_id","1");
		ticketsAccountResult.get(0).put("client_id","103");
		ticketsAccountResult.get(0).put("dsn","fake_price_code_desc");
		ticketsAccountResult.get(0).put("tm_sdk_api_key","fake_price_code");
		ticketsAccountResult.get(0).put("social_share","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(ticketsAccountResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_tickets_account_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event wallet sql query
		List<Map<String,String>> walletResult = new ArrayList<Map<String,String>>();
		walletResult.add(new HashMap<String,String>());
		walletResult.get(0).put("event_id","1");
		walletResult.get(0).put("discount_card_type","103");
		walletResult.get(0).put("discount_percentage","fake_price_code_desc");
		walletResult.get(0).put("copy_text","fake_price_code");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(walletResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_wallet_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesResult = new ArrayList<Map<String,String>>();
		venuesResult.add(new HashMap<String,String>());
		venuesResult.get(0).put("venue_id","1");
		venuesResult.get(0).put("venue_name","fake_venue");
		venuesResult.get(0).put("emkit_location_id","");
		venuesResult.get(0).put("gps_latitude","32.71573");
		venuesResult.get(0).put("gps_longitude","-117.1610838");
		venuesResult.get(0).put("venue_address","test");
		venuesResult.get(0).put("place_id","1");
		venuesResult.get(0).put("place_name","fake_place_name");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select master.*, map.place_id as place_id, map.place_name as place_name from tbl_venue_master master left join tbl_venue_place_map map on map.venue_id = master.venue_id where master.venue_id in (:ids)"), any(MapSqlParameterSource.class));
		
		// Fake results for event venues sql query
		List<Map<String,String>> venuesLocationResult = new ArrayList<Map<String,String>>();
		venuesLocationResult.add(new HashMap<String,String>());
		venuesLocationResult.get(0).put("venue_id","1");
		venuesLocationResult.get(0).put("emkit_location_id","79");
		venuesLocationResult.get(0).put("created_time","2018-01-03 10:02:38");
		venuesLocationResult.get(0).put("updated_time","2018-01-03 10:02:38");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(venuesLocationResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_venue_location_map where venue_id in (:ids)"), any(MapSqlParameterSource.class));

				
		//Fake results for event categories sql query
		List<Map<String,String>> categoriesResult = new ArrayList<Map<String,String>>();
		categoriesResult.add(new HashMap<String,String>());
		categoriesResult.get(0).put("event_id","1");
		categoriesResult.get(0).put("category_name","");
		categoriesResult.get(0).put("category_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(categoriesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, cat.category_id as category_id, cat.name as category_name from tbl_event2category evt left join tbl_categories cat on (cat.category_id = evt.category_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event tags sql query
		List<Map<String,String>> tagsResult = new ArrayList<Map<String,String>>();
		tagsResult.add(new HashMap<String,String>());
		tagsResult.get(0).put("event_id","1");
		tagsResult.get(0).put("tag_name","");
		tagsResult.get(0).put("tag_id","0");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(tagsResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select evt.event_id as event_id, tag.tag_id as tag_id, tag.name as tag_name from tbl_event2tag evt left join tbl_tags tag on (tag.tag_id = evt.tag_id) where evt.event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		//Fake results for event occurrences sql query
		List<Map<String,String>> occurrencesResult = new ArrayList<Map<String,String>>();
		occurrencesResult.add(new HashMap<String,String>());
		occurrencesResult.get(0).put("event_id","1");
		occurrencesResult.get(0).put("start","2017-09-29 00:39:51");
		occurrencesResult.get(0).put("stop","2017-09-30 05:39:51");
		// define the matcher that will assign this fake result set to the correct sql query execution
		doReturn(generateFakeRowSet(occurrencesResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_events_occurrences where event_id in (:ids) order by start"), any(MapSqlParameterSource.class));
		
		List<Map<String,String>> loadedValueOffsetConfigResult = new ArrayList<>();
		loadedValueOffsetConfigResult.add(new HashMap<String,String>());
		loadedValueOffsetConfigResult.get(0).put("event_id", eventId);
		loadedValueOffsetConfigResult.get(0).put("start_date_offset",commerceConfigLoadedValueStartOffset);
		loadedValueOffsetConfigResult.get(0).put("end_date_offset",commerceConfigLoadedValueEndOffset);
		doReturn(generateFakeRowSet(loadedValueOffsetConfigResult)).when(jdbcNamedParamTemplate).queryForRowSet(eq("select * from tbl_event_commerce_config where event_id in (:ids)"), any(MapSqlParameterSource.class));
		
		List<Map<String,String>> applicationIdResult = new ArrayList<>();
		applicationIdResult.add(new HashMap<String,String>());
		applicationIdResult.get(0).put("app_id", "1");
		doReturn(generateFakeRowSet(applicationIdResult)).when(jdbcTemplate).queryForRowSet("SELECT application_id AS app_id FROM tbl_applications_master WHERE application_id=?", new Object[] {appId});
		
		EventNotificationsV2 eventsList = (EventNotificationsV2) dao.getV2EventList(appId, 1, "", "", "", "", 
        		tagIds, categoryIds, page, size, startDate, stopDate, "","0000-00-00 00:00:00","0000-00-00 00:00:00");
		// assuming you've added fake sql result set data to get EventModel
		assertNotNull(eventsList);
		assertEquals(eventsList.getEventList().get(0).getExternalEventId1(), "r470");
		assertEquals(eventsList.getEventList().get(0).getExternalEventId3(), "4108");
		assertEquals(eventsList.getEventList().get(0).getVenue().getVenueName(), "fake_venue");
		assertSame(eventsList.getEventList().get(0).getVenue().getEmkitLocationId().get(0), 79);
		assertEquals(eventsList.getEventList().get(0).getTransportRideshareConfig().getTransportServices().get(0).getService_type(), "fake_type");
		assertEquals(eventsList.getEventList().get(0).getTicketMasterConfig().getPrice_code_assets().get(0).getParent_price_code(), "103");
		assertEquals(eventsList.getEventList().get(0).getCommerceConfig().getLoadedValueOffsetConfig().getOffset_start_in_hours(), Integer.valueOf(commerceConfigLoadedValueStartOffset));
		assertEquals(eventsList.getEventList().get(0).getCommerceConfig().getLoadedValueOffsetConfig().getOffset_end_in_hours(), Integer.valueOf(commerceConfigLoadedValueEndOffset));
	}
	
	private SqlRowSet generateFakeRowSet(List<Map<String,String>> data) {
		sqlRowSetMock = Mockito.mock(SqlRowSet.class);
		MockRow mockRowData = new MockRow(data);
		mockRows.put(sqlRowSetMock, mockRowData);
		
        doAnswer(new Answer<Boolean>() {
	        @Override
	        public Boolean answer(InvocationOnMock invocation) throws Throwable {
	        	//mockRows.get(invocation.getMock()).nextRow();
	            return mockRows.get(invocation.getMock()).nextRow();
	        }
        }).when(sqlRowSetMock).next();

	    doAnswer(new Answer<String>() {
	        @Override
	        public String answer(InvocationOnMock invocation) throws Throwable {
	            Object[] args = invocation.getArguments();
	            String name = ((String) args[0]);
	            return mockRows.get(invocation.getMock()).getColumn(name);
	        }

	        ;
	    }).when(sqlRowSetMock).getString(anyString());
	    
	    doAnswer(new Answer<Integer>() {
	        @Override
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	            Object[] args = invocation.getArguments();
	            String id = ((String) args[0]);
	            return Integer.parseInt(mockRows.get(invocation.getMock()).getColumn(id));
	        }

	        ;
	    }).when(sqlRowSetMock).getInt(anyString());
	    
	    doAnswer(new Answer<Long>() {
	        @Override
	        public Long answer(InvocationOnMock invocation) throws Throwable {
	            Object[] args = invocation.getArguments();
	            String id = ((String) args[0]);
	            return Long.parseLong(mockRows.get(invocation.getMock()).getColumn(id));
	        }

	        ;
	    }).when(sqlRowSetMock).getLong(anyString());
	    
	    doAnswer(new Answer<Boolean>() {
	        @Override
	        public Boolean answer(InvocationOnMock invocation) throws Throwable {
	            return true;
	        }

	        ;
	    }).when(sqlRowSetMock).isBeforeFirst();
	    
	    return sqlRowSetMock;
	}

	static class MockRow {
	    List<Map<String,String>> rowData;
	    int rowIndex = -1;

	    public MockRow(List<Map<String,String>> rowData) {
	    	this.rowData = rowData;
	    }

        public boolean nextRow() {
        	rowIndex++;
        	if(rowData.size() == rowIndex)
        		return false;
        	else
        		return true;
        }
        
	    public String getColumn(String name) {
	    	return rowData.get(rowIndex).get(name);
	    }
    }
}