package com.venue.rest.model;

import java.util.ArrayList;

public class EventNotificationsV2 {
	ArrayList<EventModelV2> eventList = new ArrayList<EventModelV2>();
	private int size = 0;
	private int number = 0;
	private int totalPages = 0;
	private int numberOfElements = 0;
	private int totalElements = 0;
	private boolean lastPage = false;
	private String updatedTime = "";
	public ArrayList<EventModelV2> getEventList() {
		return eventList;
	}
	public void setEventList(ArrayList<EventModelV2> eventList) {
		this.eventList = eventList;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	public int getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}
