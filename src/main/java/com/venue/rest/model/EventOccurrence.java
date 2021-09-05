package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EventOccurrence {
	
	private String eventStartDate = "";
	private String eventStopDate = "";
	
	@JsonInclude(Include.NON_DEFAULT)
	private String ticketURL = "";
	private boolean startTimeTBD = false;
	
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventStopDate() {
		return eventStopDate;
	}
	public void setEventStopDate(String eventStopDate) {
		this.eventStopDate = eventStopDate;
	}
	public String getTicketURL() {
		return ticketURL;
	}
	public void setTicketURL(String ticketURL) {
		this.ticketURL = ticketURL;
	}
	public boolean isStartTimeTBD() {
		return startTimeTBD;
	}
	public void setStartTimeTBD(boolean startTimeTBD) {
		this.startTimeTBD = startTimeTBD;
	}
}
