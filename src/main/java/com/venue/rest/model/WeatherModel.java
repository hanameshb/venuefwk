package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WeatherModel {

	private String temperature="";
	private String stadiumName="";
	private String lowtemperature="";
	private String hightemperature="";
	private String weatherBackgroundImage="";
	private String weatherIcon="";


	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getStadiumName() {
		return stadiumName;
	}
	public void setStadiumName(String stadiumName) {
		this.stadiumName = stadiumName;
	}
	public String getLowtemperature() {
		return lowtemperature;
	}
	public void setLowtemperature(String lowtemperature) {
		this.lowtemperature = lowtemperature;
	}
	public String getHightemperature() {
		return hightemperature;
	}
	public void setHightemperature(String hightemperature) {
		this.hightemperature = hightemperature;
	}
	public String getWeatherBackgroundImage() {
		return weatherBackgroundImage;
	}
	public void setWeatherBackgroundImage(String weatherBackgroundImage) {
		this.weatherBackgroundImage = weatherBackgroundImage;
	}
	public String getWeatherIcon() {
		return weatherIcon;
	}
	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}



}
