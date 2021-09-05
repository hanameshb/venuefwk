package com.venue.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.LogEventModel;
import com.venue.rest.util.ErrorMessage;

@Repository
public class LogEventDAO {

	Logger logger = Logger.getLogger(LogEventDAO.class);

	private static final int LATITUDE_BOUNDARY_LOWER = -90;
	private static final int LATITUDE_BOUNDARY_UPPER = 90;

	private static final int LONGITUDE_BOUNDARY_LOWER = -180;
	private static final int LONGITUDE_BOUNDARY_UPPER = 180;

	private String errorCode = "";
	JdbcTemplate jdbcTemplateObject = null;
	
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }

	
	/**
	 * Method to send log event service response in json
	 * @param logEventModel
	 * @return
	 */
	public Object logEventService(LogEventModel logEventModel) {
		logger.info("::in logEventService DAO::");
		if(insertLogEvents(logEventModel)){
			return ErrorMessage.getInstance().getErrorResponse("200");
		} else{
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to insert log events
	 * @param logEventModel
	 * @return
	 */
	public boolean insertLogEvents(final LogEventModel logEventModel) {
		logger.info("::in insertLogEvents DAO::");
		boolean insertStatus = false;
		int i = 0;
		Number eventId = 0;
		String eventsPlacesQuery = "";
		
		try {
			logger.info("::appUserId::" +logEventModel.getAppUserId());
			logger.info("::eventCategory::" +logEventModel.getEventCategory());
			logger.info("::eventType::" +logEventModel.getEventType());
			logger.info("::eventValue::" +logEventModel.getEventValue());
			logger.info("::screenReference::" +logEventModel.getScreenReference());
			logger.info("::sessionId::" +logEventModel.getSessionId());
			logger.info("::latitude::" +logEventModel.getLatitude());
			logger.info("::longitude::" +logEventModel.getLongitude());
			if(logEventModel.getAppUserId() != null && logEventModel.getAppUserId().length()>0 
					&& logEventModel.getEventCategory() != null && logEventModel.getEventCategory().length()>0
					&& logEventModel.getEventType() != null && logEventModel.getEventType().length()>0
					&& logEventModel.getEventValue() != null && logEventModel.getEventValue().length()>0){
				logger.info("::request data is available::");
				KeyHolder keyHolder = new GeneratedKeyHolder();

				final String latitudeStr = validateLatitudeValue(logEventModel.getLatitude());
				final String longitudeStr = validateLongitudeValue(logEventModel.getLongitude());
				
				final String eventsInsertQuery = "insert into tbl_user_events (app_user_id,event_category,event_type,event_value,screen_reference,session_id,latitude,longitude,created_time) values(?,?,?,?,?,?,?,?,now())";
				i = jdbcTemplateObject.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(eventsInsertQuery, new String[]{"id"});
						ps.setString(1, logEventModel.getAppUserId());
						ps.setString(2, logEventModel.getEventCategory());
						ps.setString(3, logEventModel.getEventType());
						ps.setString(4, logEventModel.getEventValue());
						ps.setString(5, logEventModel.getScreenReference());
						ps.setString(6, logEventModel.getSessionId());
						ps.setString(7, latitudeStr);
						ps.setString(8, longitudeStr);
						return ps;
					}
				},
				keyHolder);
				
				if(i>0) {
					logger.info("::event is inserted::");
					eventId = keyHolder.getKey();
					logger.info("::eventId generated::" +eventId);
					
					if(logEventModel.getPlaces() != null && logEventModel.getPlaces().size()>0) {
						logger.info("::places are available::");
						List<Object[]> insertParameters = new ArrayList<Object[]>();
						eventsPlacesQuery = "insert into tbl_user_events_places_map (event_id,place_name,created_time) values(?,?,now())";
						for (HashMap<String, String> places : logEventModel.getPlaces()) {
							insertParameters.add(new Object[] {eventId, places.get("place_name")});
						}
						if(insertParameters != null && insertParameters.size()>0)
							jdbcTemplateObject.batchUpdate(eventsPlacesQuery, insertParameters);
					}
				}
			} else {
				logger.info("::request data is not available::");
				errorCode = "1001";
			}
			if(i > 0) {
				logger.info("::insertion is success::");
				insertStatus = true;
			}
			logger.info("::insertStatus::" +insertStatus);
		} catch (Exception e) {
			logger.error("::Exception in insertLogEvents DAO::" +e.getLocalizedMessage());
			e.printStackTrace();
			errorCode = "500";
		}
		return insertStatus;
	}

	private String validateLatitudeValue(final String latitudeStr) {

		return validateCoordinateValue(latitudeStr, LATITUDE_BOUNDARY_LOWER, LATITUDE_BOUNDARY_UPPER,
				"latitude");

	}

	private String validateLongitudeValue(final String longitudeStr) {

		return validateCoordinateValue(longitudeStr, LONGITUDE_BOUNDARY_LOWER, LONGITUDE_BOUNDARY_UPPER,
				"longitude");

	}

	private String validateCoordinateValue(final String coordinateStrValue, final int lowerBoundary,
										   final int upperBoundary, final String coordinateName) {

		final String pointZero = "0.0";
		if (StringUtils.isBlank(coordinateStrValue)) {
			return pointZero;
		}

		try {

			final double longitude = Double.parseDouble(coordinateStrValue);
			if (longitude >= lowerBoundary && longitude <= upperBoundary) {
				return coordinateStrValue;
			}

			logger.warn(":: " + coordinateName + ": '" + coordinateStrValue + "' is out of coordinate valid range. ::");

			return pointZero;
		} catch (NumberFormatException e) {
			logger.warn(":: " + coordinateName + " has an invalid format " + coordinateStrValue + " ::", e);
		}

		return pointZero;

	}


}
