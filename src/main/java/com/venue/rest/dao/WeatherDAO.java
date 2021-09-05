package com.venue.rest.dao;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.WeatherModel;
@Repository
public class WeatherDAO {

	private static Logger logger = Logger.getLogger(WeatherDAO.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;

	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }


	public Object getWeatherResponse() {
		// TODO Auto-generated method stub
		SqlRowSet rst=null;
		WeatherModel responseModel= new WeatherModel();
		try {
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_game_weather_report where DATE(game_date_time)=CURDATE()");
			if(rst.next()){
				responseModel.setTemperature(rst.getString("temperature"));
				responseModel.setStadiumName(rst.getString("game_site"));
				responseModel.setHightemperature(rst.getString("high_temperature"));
				responseModel.setLowtemperature(rst.getString("low_temperature"));
				responseModel.setWeatherBackgroundImage(rst.getString("weather_background_image"));
				responseModel.setWeatherIcon(rst.getString("weather_icon"));
			}else{
				//rst=jdbcTemplateObject.queryForRowSet("select * from tbl_game_weather_report where game_site='Sun Life Stadium'");
				rst=jdbcTemplateObject.queryForRowSet("select * from tbl_game_weather_report where game_site='Miami Gardens'");
				rst.next();
				responseModel.setTemperature(rst.getString("temperature"));
				responseModel.setStadiumName(rst.getString("game_site"));
				responseModel.setHightemperature(rst.getString("high_temperature"));
				responseModel.setLowtemperature(rst.getString("low_temperature"));
				responseModel.setWeatherBackgroundImage(rst.getString("weather_background_image"));
				responseModel.setWeatherIcon(rst.getString("weather_icon"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(":::Exception in getting Weather:::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return responseModel;
	}
}
