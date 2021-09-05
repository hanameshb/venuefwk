package com.venue.rest.dao;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.Venues;

@Repository
public class VenueDAO {
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject=null;
	private static Logger logger = Logger.getLogger(VenueDAO.class);
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }

    
    public ArrayList<Venues> getVenues(int appId) {
		logger.info("::in getVenues::");
		SqlRowSet rst=null;
		ArrayList<Venues> venusList=new ArrayList<Venues>();
		try {			
			logger.info("::appId::" +appId);
			rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_master master join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in(select venue_id from tbl_admin_users_venue_access_map where admin_user_id in(select admin_user_id from tbl_admin_users_application_access_map where app_id=?)) order by sort_order_id",new Object[]{appId});
			
			while(rst.next()){
				Venues venues=new Venues();
				venues.setVenue_id(rst.getString("venue_id"));
				venues.setVenue_name(rst.getString("venue_name"));
				venues.setEmkit_location_id(rst.getString("emkit_location_id"));
				venues.setGps_latitude(rst.getString("gps_latitude"));
				venues.setGps_longitude(rst.getString("gps_longitude"));
				venues.setVenue_address(rst.getString("venue_address"));
				venues.setThumbnail_image_url(rst.getString("thumbnail_image_url"));
				venues.setBanner_image_url(rst.getString("banner_image_url"));
				venues.setDescription(rst.getString("description"));
				venues.setContact(rst.getString("contact"));
				venues.setVenue_status(rst.getString("venue_status"));
				
				venusList.add(venues);
			}
		} catch (Exception e) {
			logger.info(":::Exception in getMapsbyLocationV2 DAO:::"+e.getLocalizedMessage());
			e.printStackTrace();
			errorCode="500";
			
		}
	return venusList;
	}
    public Venues getVenuesByID(int appId,int venueId) {
		logger.info("::in getVenuesByID::");
		SqlRowSet rst=null;
		Venues venues=new Venues();
		try {
			
			logger.info("::appId::" +appId);
			logger.info("::venueId::" +venueId);
			rst = jdbcTemplateObject.queryForRowSet("select * from tbl_venue_master master join tbl_venue_location_map locmap on master.venue_id = locmap.venue_id where master.venue_id in(select venue_id from tbl_admin_users_venue_access_map where  venue_id=? and admin_user_id in(select admin_user_id from tbl_admin_users_application_access_map where app_id=?))",new Object[]{venueId,appId});
			
			while(rst.next()){
				venues.setVenue_id(rst.getString("venue_id"));
				venues.setVenue_name(rst.getString("venue_name"));
				venues.setEmkit_location_id(rst.getString("emkit_location_id"));
				venues.setGps_latitude(rst.getString("gps_latitude"));
				venues.setGps_longitude(rst.getString("gps_longitude"));
				venues.setVenue_address(rst.getString("venue_address"));
				venues.setThumbnail_image_url(rst.getString("thumbnail_image_url"));
				venues.setBanner_image_url(rst.getString("banner_image_url"));
				venues.setDescription(rst.getString("description"));
				venues.setContact(rst.getString("contact"));
				venues.setVenue_status(rst.getString("venue_status"));			
				
			}
		} catch (Exception e) {
			logger.info(":::Exception in getMapsbyLocationV2 DAO:::"+e.getLocalizedMessage());
			e.printStackTrace();
			errorCode="500";
			
		}
	return venues;
	}
}
