package com.venue.rest.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venue.rest.model.Menu;
import com.venue.rest.model.SubMenu;
import com.venue.rest.util.ErrorMessage;
import com.venue.rest.util.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.net.URI;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MenuDAO {
	
	private static Logger logger = Logger.getLogger(MenuDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;
	private RestTemplate restTemplate;
	
	
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	MenuDAO (){}
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
		this.restTemplate = new RestTemplate();;
    }
	MenuDAO(JdbcTemplate jdbcTemplateObject, RestTemplate restTemplate) {
		this.jdbcTemplateObject = jdbcTemplateObject;
		this.restTemplate = restTemplate;
	}
	
	/**
	 * Method to intialize DB
	 */
	@SuppressWarnings("resource")
	private void initializeDB() {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("venue_db.xml");
			DataSource dataSource = (DataSource) context.getBean("dataSourceVenue");
			this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to get Menu List response
	 */
	public Object getMenuList(String appUserId,String appId,String venueId) {
		logger.info("::in getMenuList DAO::" +appUserId);
		logger.info("::appUserId::" +appUserId);
		logger.info("::appId::" +appId);
		logger.info("::venueId::" +venueId);
		if(appUserId != null && !appUserId.equalsIgnoreCase("")){
			Object menuList = getMenuListResponse(appUserId,appId,venueId);
			if(menuList != null){
				return menuList;
			} else {
				errorCode = "500";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} else {
			errorCode = "1002";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to get Menu List response
	 */
	public Object getMenuList(String appUserId,String appId,String venueId, ArrayList<HashMap<Object, Object>> userCurrentPlaces, String segmentsUrl) {
		logger.info("::in getMenuList DAO::" +appUserId);
		logger.info("::appUserId::" +appUserId);
		logger.info("::appId::" +appId);
		logger.info("::venueId::" +venueId);
		if(appUserId != null && !appUserId.equalsIgnoreCase("")){
			Object menuList = getMenuListResponse(appUserId,appId,venueId,"",userCurrentPlaces, segmentsUrl);
			if(menuList != null){
				return menuList;
			} else {
				errorCode = "500";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} else {
			errorCode = "1002";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to get Menu List response
	 */
	public Object getMenuList(String appUserId, String appId, String venueId, String menuId,
			ArrayList<HashMap<Object, Object>> userCurrentPlaces, String segmentsUrl) {
		logger.info("::in getMenuList DAO::" + appUserId);
		logger.info("::appUserId::" + appUserId);
		logger.info("::appId::" + appId);
		logger.info("::venueId::" + venueId);
		Object menuList = getMenuListResponse(appUserId, appId, venueId, menuId, userCurrentPlaces, segmentsUrl);
		if (menuList != null) {
			return menuList;
		} else {
			errorCode = "500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}

	}
	
	/**
	 * Method to get Menu List from DB
	 */
	private Object getMenuListResponse(String appUserId, String appId, String venueId) {
		logger.info("::in getMenuListResponse::");
		ArrayList<Menu> menuList = new ArrayList<Menu>();
		ArrayList<SubMenu> subMenuList = null;
		HashMap<String, Object> menuMap = new HashMap<String, Object>();
		String lastUpdateTime = "";
 		SqlRowSet rst = null,rst1 = null;
 		
		try {
			rst = jdbcTemplateObject.queryForRowSet("select updated_time from tbl_hub_menu_last_updated where venue_id=? and app_id=?",
					new Object[]{venueId,appId});
			while(rst.next()) {
				lastUpdateTime = rst.getString("updated_time");
				logger.info("::lastUpdateTime::" +lastUpdateTime);
			}
			rst = jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list where venue_id=? and app_id=? order by sort_order_id",
					new Object[]{venueId,appId});
			while(rst.next()) {
				Menu menu = new Menu();
				menu.setMenu_id(rst.getString("menu_id"));
				menu.setKey(rst.getString("menu_key"));
				menu.setValue(rst.getString("value"));
				menu.setMenu_view_type(rst.getString("menu_view_type"));
				menu.setIcon(rst.getString("icon"));
				menu.setSelected_icon(rst.getString("selected_icon"));
				menu.setDeeplink_url(rst.getString("deeplink_url"));
				menu.setSort_order_id(rst.getString("sort_order_id"));
				
				rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id")+" order by sort_order_id");
				if(rst1 != null) {
					subMenuList = new ArrayList<SubMenu>();
				}
				while(rst1.next()) {
					SubMenu subMenu = new SubMenu();
					subMenu.setSub_menu_id(rst1.getString("sub_menu_id"));
					subMenu.setKey(rst1.getString("menu_key"));
					subMenu.setValue(rst1.getString("value"));
					subMenu.setIcon(rst1.getString("icon"));
					subMenu.setSelected_icon(rst1.getString("selected_icon"));
					subMenu.setDeeplink_url(rst1.getString("deeplink_url"));
					subMenu.setSort_order_id(rst1.getString("sort_order_id"));
					subMenu.setWeb_url(rst1.getString("web_url"));
					subMenu.setSignup_required(rst1.getString("signup_required"));
					subMenu.setIcon_2x(rst1.getString("icon_2x"));
					subMenu.setSelected_icon_2x(rst1.getString("selected_icon_2x"));
					subMenu.setWidth(rst1.getString("menu_width"));
					subMenu.setHeight(rst1.getString("menu_height"));
					subMenuList.add(subMenu);
				}
				if(subMenuList!=null){
					menu.setSubMenuList(subMenuList);
				}
				menuList.add(menu);
			}
			menuMap.put("lastupdatedtime", lastUpdateTime);
			menuMap.put("menulist", menuList);
		} catch(Exception e) {
			logger.info("::Exception in getMenuListResponse::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return menuMap;
	}
	
	/**
	 * Method to get Menu List from DB
	 */
	private Object getMenuListResponse(String appUserId, String appId, String venueId, String menuId,
									   ArrayList<HashMap<Object, Object>> userCurrentPlaces, String segmentsUrl) {

		logger.info("::in getMenuListResponse::");
		final List<Menu> menuList = new ArrayList<>();
		List<SubMenu> subMenuList = new LinkedList<>();
		final Map<String, Object> menuMap = new HashMap<>();
		final String lastUpdateTime;
		SqlRowSet hubMenuJoinRowSet;
		String subMenuId;
		String menuCalendarId;
		String subMenuCalendarId;
		String emp2UserId="";
 		
		try {
			final String hubMenuLastUpdateSqlQuery = "SELECT updated_time " +
					" FROM tbl_hub_menu_last_updated " +
					" WHERE app_id = ? AND venue_id = ?";
			hubMenuJoinRowSet = jdbcTemplateObject.queryForRowSet(hubMenuLastUpdateSqlQuery, new Object[]{appId,venueId});
			lastUpdateTime = hubMenuJoinRowSet != null && hubMenuJoinRowSet.next()
					? hubMenuJoinRowSet.getString("updated_time") : "";
			logger.info("::lastUpdateTime::" + lastUpdateTime);

			final String hubMenuInnerJoinSqlTemplate = "SELECT hml.menu_id, hml.venue_id, hml.app_id, hml.menu_calendar_id, " +
					"    hml.menu_key, hml.value, " +
					"    hml.menu_view_type, hml.icon, hml.selected_icon, hml.updated_time, " +
					"    hml.deeplink_url, hml.sort_order_id, hml.menu_start_time as menu_start_time , hml.menu_end_time as menu_end_time , mvt.name, hsml.sub_menu_id, hsml.sub_menu_calendar_id, " +
					"    hsml.menu_key as sub_menu_key, hsml.value as sub_value, hsml.icon as sub_icon, " +
					"    hsml.selected_icon as sub_selected_icon, hsml.deeplink_url as sub_deeplink_url, " +
					"    hsml.sort_order_id as sub_sort_order_id, hsml.web_url as sub_web_url, " +
					"    hsml.signup_required as sub_signup_required, hsml.icon_2x as sub_icon_2x, " +
					"    hsml.selected_icon_2x as sub_selected_icon_2x, hsml.menu_width as sub_menu_width, " +
					"    hsml.menu_height as sub_menu_height, hsml.menu_start_time as sub_menu_start_time ,hsml.menu_end_time as sub_menu_end_time, mpm1.place_segment_id as menu_place_segment_id, " +
					"    mpm1.place_segment_name as menu_place_segment_name, mpm1.negative as menu_negative," +
					"    mpm2.place_segment_id as sub_menu_place_segment_id, mpm2.place_segment_name as sub_menu_place_segment_name, " +
					"    mpm2.negative as sub_menu_negative, mam1.audience_segment_id as menu_audience_segment_id," +
					"    mam2.audience_segment_id as sub_menu_audience_segment_id " +
					" FROM tbl_hub_menu_list hml " +
					" LEFT JOIN tbl_hub_sub_menu_list hsml ON hsml.menu_id = hml.menu_id AND " +
					"    (((UTC_TIMESTAMP() BETWEEN hsml.menu_start_time AND hsml.menu_end_time) OR " +
					"    (hsml.menu_start_time='0000-00-00 00:00:00' and hsml.menu_end_time='0000-00-00 00:00:00') OR " +
					"    (UTC_TIMESTAMP() > hsml.menu_start_time and hsml.menu_end_time='0000-00-00 00:00:00')) AND hsml.sub_menu_status = 'publish')" +
					" LEFT JOIN tbl_menu_view_type mvt ON mvt.id = hml.menu_view_type " +
					" LEFT JOIN tbl_menu_place_map mpm1 ON mpm1.id = hml.menu_id AND mpm1.menu_type = 'menu' " +
					" LEFT JOIN tbl_menu_place_map mpm2 ON mpm2.id = hsml.sub_menu_id AND mpm2.menu_type = 'subMenu' " +
					" LEFT JOIN tbl_menu_audience_map mam1 ON mam1.id = hml.menu_id and mam1.menu_type = 'menu' " +
					" LEFT JOIN tbl_menu_audience_map mam2 ON mam2.id = hsml.sub_menu_id and mam2.menu_type = 'subMenu' " +
					" WHERE " +
					"    %s AND " +
					"    ((UTC_TIMESTAMP() BETWEEN hml.menu_start_time AND hml.menu_end_time) OR " +
					"    (hml.menu_start_time='0000-00-00 00:00:00' and hml.menu_end_time='0000-00-00 00:00:00') OR " +
					"    (UTC_TIMESTAMP() > hml.menu_start_time and hml.menu_end_time='0000-00-00 00:00:00')) AND" +
					"    hml.menu_status = 'publish'" +
					" ORDER BY hml.sort_order_id, hsml.sort_order_id";

			final String hubMenuInnerJoinSql;
			if (StringUtils.isEmpty(menuId)) {
				hubMenuInnerJoinSql = String.format(hubMenuInnerJoinSqlTemplate, "hml.app_id = ? AND hml.venue_id = ?");
				hubMenuJoinRowSet = jdbcTemplateObject.queryForRowSet(hubMenuInnerJoinSql, appId, venueId);
			} else {
				hubMenuInnerJoinSql = String.format(hubMenuInnerJoinSqlTemplate, "hml.app_id = ? AND hml.venue_id = ? AND hml.menu_id = ?");
				hubMenuJoinRowSet = jdbcTemplateObject.queryForRowSet(hubMenuInnerJoinSql, appId, venueId, menuId);
			}

			logger.info("::menuId::" +menuId);
			Menu menu = new Menu();

			List<String> invalidMenuList = new LinkedList<>();
			List<SubMenu> invalidSubMenuList = new LinkedList<>();
			List<String> notInPlaceList = new LinkedList<>();
			List<String> menuAudienceInvalidList = new LinkedList<>();
			List<String> audienceInvalidList = new LinkedList<>();
			
			if(appUserId != null && !appUserId.equalsIgnoreCase("")){
				emp2UserId = geteMp2UserId(appUserId);
			}

			String previousMenu = null;
			while(hubMenuJoinRowSet.next()) {

				menuId = hubMenuJoinRowSet.getString("menu_id");
				menuCalendarId = hubMenuJoinRowSet.getString("menu_calendar_id");

				int menuPlaceSegmentId = hubMenuJoinRowSet.getInt("menu_place_segment_id");
				String menuPlaceSegmentName = hubMenuJoinRowSet.getString("menu_place_segment_name");
				int menuAudienceSegmentId = hubMenuJoinRowSet.getInt("menu_audience_segment_id");
				boolean menuNegative = hubMenuJoinRowSet.getBoolean("menu_negative");

				boolean isValidMenu = checkPlaceAndCalendarSegments(menuId, menuPlaceSegmentId, menuPlaceSegmentName,
						menuNegative, appUserId, userCurrentPlaces, "menu", menuCalendarId);

				if (!isValidMenu) {
					invalidMenuList.add(menuId);
				}

				final Boolean keepMenuByAudience = keepByAudience(menuAudienceSegmentId, emp2UserId, segmentsUrl);
				if (keepMenuByAudience != null && !keepMenuByAudience) {
					menuAudienceInvalidList.add(menu.getMenu_id());
				}

				if (!menuId.equals(previousMenu)) {

					if (previousMenu != null && !invalidMenuList.contains(menu.getMenu_id())
						&& !menuAudienceInvalidList.contains(menu.getMenu_id())) {
						menuList.add(menu);
					}

					if (subMenuList != null && !subMenuList.isEmpty()) {
						menu.setSubMenuList(subMenuList.stream()
								.filter(subMenu -> !invalidSubMenuList.contains(subMenu))
								.filter(subMenu -> !notInPlaceList.contains(subMenu.getSub_menu_id()))
								.filter(subMenu -> !audienceInvalidList.contains(subMenu.getSub_menu_id()))
								.collect(Collectors.toList()));
						List<SubMenu> distinctSubMenuList = menu.getSubMenuList().stream()
								.filter(subMenu -> subMenu.getSub_menu_id()!=null)
								.filter(Utility.distinctByKey(subMenu -> subMenu.getSub_menu_id()))
								.collect(Collectors.toList());
						menu.setSubMenuList(distinctSubMenuList);
					}

					menu = new Menu();
					subMenuList = new ArrayList<>();

					menu.setMenu_id(menuId);
					menu.setKey(hubMenuJoinRowSet.getString("menu_key"));
					menu.setValue(hubMenuJoinRowSet.getString("value"));
					menu.setUpdatedTime(hubMenuJoinRowSet.getString("updated_time"));
					menu.setStartTime(hubMenuJoinRowSet.getString("menu_start_time") != null ? hubMenuJoinRowSet.getString("menu_start_time") : "");
					menu.setEndTime(hubMenuJoinRowSet.getString("menu_end_time") != null ? hubMenuJoinRowSet.getString("menu_end_time") : "");

					if (hubMenuJoinRowSet.getString("name") != null && !hubMenuJoinRowSet.getString("name").equals(""))
						menu.setMenu_view_type(hubMenuJoinRowSet.getString("name"));
					else
						menu.setMenu_view_type("");

					if (hubMenuJoinRowSet.getString("icon") != null && !hubMenuJoinRowSet.getString("icon").equals(""))
						menu.setIcon(hubMenuJoinRowSet.getString("icon"));
					else
						menu.setIcon("");

					if (hubMenuJoinRowSet.getString("selected_icon") != null && !hubMenuJoinRowSet.getString("selected_icon").equals(""))
						menu.setSelected_icon(hubMenuJoinRowSet.getString("selected_icon"));
					else
						menu.setSelected_icon("");

					if (hubMenuJoinRowSet.getString("deeplink_url") != null && !hubMenuJoinRowSet.getString("deeplink_url").equals(""))
						menu.setDeeplink_url(hubMenuJoinRowSet.getString("deeplink_url"));
					else
						menu.setDeeplink_url("");
					menu.setSort_order_id(hubMenuJoinRowSet.getString("sort_order_id"));
				}

				subMenuId = hubMenuJoinRowSet.getString("sub_menu_id");
				subMenuCalendarId = hubMenuJoinRowSet.getString("sub_menu_calendar_id");
				int subMenuPlaceSegmentId = hubMenuJoinRowSet.getInt("sub_menu_place_segment_id");
				String subMenuPlaceSegmentName = hubMenuJoinRowSet.getString("sub_menu_place_segment_name");
				int subMenuAudienceSegmentId = hubMenuJoinRowSet.getInt("sub_menu_audience_segment_id");
				boolean subMenuNegative = hubMenuJoinRowSet.getBoolean("sub_menu_negative");
				boolean isSubMenuValid = checkPlaceAndCalendarSegments(subMenuId, subMenuPlaceSegmentId,
						subMenuPlaceSegmentName, subMenuNegative, appUserId, userCurrentPlaces, "subMenu",
						subMenuCalendarId);

				SubMenu subMenu =getSubMenuFromRowset(hubMenuJoinRowSet);
				subMenuList.add(subMenu);

				if (!isSubMenuValid) {
					invalidSubMenuList.add(subMenu);
				}

				boolean notInPlace = userCurrentPlaces != null && !userCurrentPlaces.isEmpty() && (userCurrentPlaces
						.stream()
						.anyMatch(place -> place !=null && place.size() > 0 && place.get("placeName").equals(subMenuPlaceSegmentName)) && subMenuNegative);
				if (notInPlace) {
					notInPlaceList.add(subMenu.getSub_menu_id());
				}

				final Boolean keepSubMenuByAudience = keepByAudience(subMenuAudienceSegmentId, emp2UserId, segmentsUrl);
				if (keepSubMenuByAudience != null && !keepSubMenuByAudience) {
					audienceInvalidList.add(subMenu.getSub_menu_id());
				}

				previousMenu = menuId;
			}

			if (subMenuList != null && !subMenuList.isEmpty()) {
				menu.setSubMenuList(subMenuList.stream()
						.filter(subMenu -> !invalidSubMenuList.contains(subMenu))
						.filter(subMenu -> !notInPlaceList.contains(subMenu.getSub_menu_id()))
						.filter(subMenu -> !audienceInvalidList.contains(subMenu.getSub_menu_id()))
						.collect(Collectors.toList()));
				List<SubMenu> distinctSubMenuList = menu.getSubMenuList().stream()
						.filter(subMenu -> subMenu.getSub_menu_id()!=null)
						.filter(Utility.distinctByKey(subMenu -> subMenu.getSub_menu_id()))
						.collect(Collectors.toList());
				menu.setSubMenuList(distinctSubMenuList);
			}

			if (!invalidMenuList.contains(menu.getMenu_id()) && !menuAudienceInvalidList.contains(menu.getMenu_id())) {
				menuList.add(menu);
			}

			menuMap.put("lastupdatedtime", lastUpdateTime);
			menuMap.put("menulist", menuList);
		} catch(Exception e) {
			logger.info("::Exception in getMenuListResponse::", e);
			e.printStackTrace();
		}
		return menuMap;
	}
	/**
	 * Method to for subMenu from Result Set
	 * @param hubMenuJoinRowSet
	 * @return SubMenu
	 */
	private SubMenu getSubMenuFromRowset(SqlRowSet hubMenuJoinRowSet){
		SubMenu subMenu = new SubMenu();
		subMenu.setSub_menu_id(hubMenuJoinRowSet.getString("sub_menu_id"));
		subMenu.setKey(hubMenuJoinRowSet.getString("sub_menu_key"));
		subMenu.setValue(hubMenuJoinRowSet.getString("sub_value"));
		subMenu.setIcon(hubMenuJoinRowSet.getString("sub_icon"));
		subMenu.setSelected_icon(hubMenuJoinRowSet.getString("sub_selected_icon"));
		subMenu.setDeeplink_url(hubMenuJoinRowSet.getString("sub_deeplink_url"));
		subMenu.setSort_order_id(hubMenuJoinRowSet.getString("sub_sort_order_id"));
		subMenu.setWeb_url(hubMenuJoinRowSet.getString("sub_web_url"));
		subMenu.setSignup_required(hubMenuJoinRowSet.getString("sub_signup_required"));
		subMenu.setIcon_2x(hubMenuJoinRowSet.getString("sub_icon_2x"));
		subMenu.setSelected_icon_2x(hubMenuJoinRowSet.getString("sub_selected_icon_2x"));
		subMenu.setWidth(hubMenuJoinRowSet.getString("sub_menu_width"));
		subMenu.setHeight(hubMenuJoinRowSet.getString("sub_menu_height"));
		subMenu.setPlaceSegmentId(hubMenuJoinRowSet.getInt("sub_menu_place_segment_id"));
		subMenu.setPlaceSegmentName(hubMenuJoinRowSet.getString("sub_menu_place_segment_name"));
		subMenu.setStartTime(hubMenuJoinRowSet.getString("sub_menu_start_time") != null ? hubMenuJoinRowSet.getString("sub_menu_start_time") : "");
		subMenu.setEndTime(hubMenuJoinRowSet.getString("sub_menu_end_time") != null ? hubMenuJoinRowSet.getString("sub_menu_end_time") : "");
		return subMenu;
	}
	private Boolean keepByAudience(final int audienceSegmentId, final String emp2UserId, final String segmentsUrl) {

		Boolean keep = null;

		try {
			//check for audience restriction
			logger.info("::audienceSegmentId::" + audienceSegmentId);
			if(audienceSegmentId > 0 && emp2UserId != null) {
				if (!emp2UserId.equalsIgnoreCase("")) {
				logger.info("::audienceSegmentId is available::");

				//START - Call REST API in eMcards to verify if user belongs to AppBoy Segment
				ObjectMapper mapperObj = new ObjectMapper();
				Map<String, Object> inputMap = new HashMap<String, Object>();
				inputMap.put("audienceSegmentId", audienceSegmentId);
				inputMap.put("emp2UserId", emp2UserId);
				String body = mapperObj.writeValueAsString(inputMap);

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entity = new HttpEntity<String>(body, httpHeaders);

				//send request
				keep = restTemplate.postForObject(new URI(segmentsUrl), entity, boolean.class);
				logger.info("keep: " + keep);
				//END
				} else if(emp2UserId.equalsIgnoreCase("")) {
					keep = false;
				}
			} 
			
		} catch(Exception e) {
			logger.info("::Exception in checkAudience::"+e.getLocalizedMessage());
			e.printStackTrace();
		}

		return keep;

	}

	public boolean checkPlaceAndCalendarSegments(String id, int placeSegmentId, String placeSegmentName, boolean negative,
												 String appUserId, ArrayList<HashMap<Object, Object>> userCurrentPlaces,
												 String menuType, String calendarId) {
		logger.info("::in checkPlaceAndCalendarSegments::");
		SqlRowSet rst = null, rst1 = null, rst2 = null, rst3 = null;
		boolean positivePlaceRestriction = false;
		boolean negativePlaceRestriction = false;
		boolean timeRestriction = false;
		boolean timeMatched = true;
		boolean positivePlaceMatched = false;
		boolean negativePlaceMatched = true;
		boolean userMatched = true;
		String startDateTime = "";
		String endDateTime = "";
		String repeatType = "";
		String repeatWeeklyOn = "";
		if (userCurrentPlaces == null) {
			userCurrentPlaces = new ArrayList();
		}

		try {

			logger.info("::menuType::"+ menuType);
			//check for place restriction

			if(placeSegmentId > 0 && placeSegmentName != null && !placeSegmentName.equals("")) {
				if (!negative) {
					positivePlaceRestriction = true;
					for (HashMap<Object, Object> location : userCurrentPlaces) {
						if (location != null && location.size() > 0 && placeSegmentName.trim().equalsIgnoreCase(location.get("placeName").toString().trim())) {
							positivePlaceMatched = true;
						}
					}
				}
			}

			//check for calendar restriction
			if(calendarId != null) {
				if(menuType != null && menuType.equalsIgnoreCase("menu"))
					rst3 = jdbcTemplateObject.queryForRowSet("select * from tbl_menu_scheduler where menu_id=? and calendar_id=?",
							new Object[]{id, calendarId});
				if(menuType != null && menuType.equalsIgnoreCase("subMenu"))
					rst3 = jdbcTemplateObject.queryForRowSet("select * from tbl_menu_scheduler where sub_menu_id=? and calendar_id=?",
							new Object[]{id, calendarId});
				while(rst3.next()) {
					timeRestriction = true;
					startDateTime = rst3.getString("start_datetime");
					endDateTime = rst3.getString("end_datetime");
					repeatType = rst3.getString("repeat_type");
					repeatWeeklyOn = rst3.getString("repeat_weekly_on");

					String dayNames[] = new DateFormatSymbols().getWeekdays();
					Calendar date2 = Calendar.getInstance();
					String dayName = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
					logger.info("::Today is a::"+ dayName.substring(0, 2));

					if(repeatWeeklyOn.contains(dayName.substring(0, 2))) {
						logger.info("::day matched::");

						Date currentDate = new Date();
						logger.info("::currentDate::" +currentDate);
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						String currentTime = sdf.format(currentDate);
						logger.info("::currentTime::" +currentTime);
						if(isTimeBetweenTwoTimes(startDateTime, endDateTime, currentTime)) {
							logger.info("::Given time lies between two times::");
							timeMatched = true;
						}
						else
							logger.info("::Given time doesn't lies between two times::");
					}
				}
			}

			if(positivePlaceRestriction && !positivePlaceMatched) {
				userMatched = false;
			} else if (timeRestriction && !timeMatched) {
				userMatched = false;
			}
		} catch(Exception e) {
			logger.info("::Exception in checkPlaceAndAudienceSegments::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return userMatched;
	}
	
	public String geteMp2UserId(String appUserId) throws Exception {
		SqlRowSet rst = null;
		String emp2UserId = "";
		
		rst = jdbcTemplateObject.queryForRowSet("select emkit_user_id from tbl_app_user_detail where app_user_id=?",new Object[]{appUserId});
		if(rst.next()) {
			emp2UserId = rst.getString("emkit_user_id");
		}
		logger.info("::emp2UserId::" +emp2UserId);
		return emp2UserId;
	}
	
	public static boolean isTimeBetweenTwoTimes(String startTime, String endTime, String currentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if(startTime.matches(reg) && endTime.matches(reg) && currentTime.matches(reg)) {
            boolean valid  = false;
            //Start Time
            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);
            
            //Current Time
            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);
            
            //End Time
            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);
            
            if(endTime.compareTo(startTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }
            logger.info("::startTime::" +calendar1.getTime());
            logger.info("::endTime::" +calendar2.getTime());
            logger.info("::currentTime::" +calendar3.getTime());
            
            java.util.Date actualTime = calendar3.getTime();
            if((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime())==0) && actualTime.before(calendar2.getTime())){
                valid = true;
            }
            return valid;
        } else {
             throw new IllegalArgumentException("not a valid time, expecting HH:MM:SS format");
        }
    }
}
