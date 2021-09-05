package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class LoadedValueOffsetConfig {

	private Integer offset_start_in_hours;
	private Integer offset_end_in_hours;
	
	public Integer getOffset_start_in_hours() {
		return offset_start_in_hours;
	}
	public void setOffset_start_in_hours(Integer offset_start_in_hours) {
		this.offset_start_in_hours = offset_start_in_hours;
	}
	public Integer getOffset_end_in_hours() {
		return offset_end_in_hours;
	}
	public void setOffset_end_in_hours(Integer offset_end_in_hours) {
		this.offset_end_in_hours = offset_end_in_hours;
	}
	
}
