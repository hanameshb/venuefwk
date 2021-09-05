package com.venue.rest.model;

import java.util.ArrayList;
import java.util.List;

public class EventModelV2 {
	private int eventId = 0;
	private String eventTitle = "";
	private String eventShortDescription = "";
	private String eventDescriptionHTML = "";
	private String webUrl = "";
	private String eventStartDate = "";
	private String eventEndDate = "";
	private String externalEventId1 = "";
	private String externalEventId2 = "";
	private String externalEventId3 = "";
	private String eventImage1 = "";
	private String eventImage2 = "";
	private String eventImage3 = "";
	private String metaData1 = "";
	private String metaData2 = "";
	private String metaData3 = "";
	private String eventLastModifiedDate = "";
	private VenueV2 venue = null;
	private WalletConfig walletConfig = null;
	private TicketConfig ticketMasterConfig = null;
	private TransportConfig transportRideshareConfig = null;
	private TransportConfig transportNavigationTrafficConfig = null;
	private CommerceConfig commerceConfig;
	private List<Tuple> categories = new ArrayList();
	private List<Tuple> tags = new ArrayList();
	private List<EventOccurrence> occurrences = new ArrayList();
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventShortDescription() {
		return eventShortDescription;
	}
	public void setEventShortDescription(String eventShortDescription) {
		this.eventShortDescription = eventShortDescription;
	}
	public String getEventDescriptionHTML() {
		return eventDescriptionHTML;
	}
	public void setEventDescriptionHTML(String eventDescriptionHTML) {
		this.eventDescriptionHTML = eventDescriptionHTML;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public String getExternalEventId1() {
		return externalEventId1;
	}
	public void setExternalEventId1(String externalEventId1) {
		this.externalEventId1 = externalEventId1;
	}
	public String getExternalEventId2() {
		return externalEventId2;
	}
	public void setExternalEventId2(String externalEventId2) {
		this.externalEventId2 = externalEventId2;
	}
	public String getExternalEventId3() {
		return externalEventId3;
	}
	public void setExternalEventId3(String externalEventId3) {
		this.externalEventId3 = externalEventId3;
	}
	public String getEventImage1() {
		return eventImage1;
	}
	public void setEventImage1(String eventImage1) {
		this.eventImage1 = eventImage1;
	}
	public String getEventImage2() {
		return eventImage2;
	}
	public void setEventImage2(String eventImage2) {
		this.eventImage2 = eventImage2;
	}
	public String getEventImage3() {
		return eventImage3;
	}
	public void setEventImage3(String eventImage3) {
		this.eventImage3 = eventImage3;
	}
	public String getMetaData1() {
		return metaData1;
	}
	public void setMetaData1(String metaData1) {
		this.metaData1 = metaData1;
	}
	public String getMetaData2() {
		return metaData2;
	}
	public void setMetaData2(String metaData2) {
		this.metaData2 = metaData2;
	}
	public String getMetaData3() {
		return metaData3;
	}
	public void setMetaData3(String metaData3) {
		this.metaData3 = metaData3;
	}
	public String getEventLastModifiedDate() {
		return eventLastModifiedDate;
	}
	public void setEventLastModifiedDate(String eventLastModifiedDate) {
		this.eventLastModifiedDate = eventLastModifiedDate;
	}
	public VenueV2 getVenue() {
		return venue;
	}
	public void setVenue(VenueV2 venue) {
		this.venue = venue;
	}
	public WalletConfig getWalletConfig() {
		return walletConfig;
	}
	public void setWalletConfig(WalletConfig walletConfig) {
		this.walletConfig = walletConfig;
	}
	public TicketConfig getTicketMasterConfig() {
		return ticketMasterConfig;
	}
	public void setTicketMasterConfig(TicketConfig ticketMasterConfig) {
		this.ticketMasterConfig = ticketMasterConfig;
	}
	public TransportConfig getTransportRideshareConfig() {
		return transportRideshareConfig;
	}
	public void setTransportRideshareConfig(TransportConfig transportRideshareConfig) {
		this.transportRideshareConfig = transportRideshareConfig;
	}
	public TransportConfig getTransportNavigationTrafficConfig() {
		return transportNavigationTrafficConfig;
	}
	public void setTransportNavigationTrafficConfig(TransportConfig transportNavigationTrafficConfig) {
		this.transportNavigationTrafficConfig = transportNavigationTrafficConfig;
	}
	public List<Tuple> getCategories() {
		return categories;
	}
	public void setCategories(List<Tuple> categories) {
		this.categories = categories;
	}
	public List<Tuple> getTags() {
		return tags;
	}
	public void setTags(List<Tuple> tags) {
		this.tags = tags;
	}
	public List<EventOccurrence> getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(List<EventOccurrence> occurrences) {
		this.occurrences = occurrences;
	}
	public CommerceConfig getCommerceConfig() {
		return commerceConfig;
	}
	public void setCommerceConfig(CommerceConfig commerceConfig) {
		this.commerceConfig = commerceConfig;
	}

}
