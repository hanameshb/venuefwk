/****************************************************************************
 *   Copyright (c)2013 eMbience. All rights reserved.
 *
 *   THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF eMbience.
 *
 *   The copyright notice above does not evidence any actual or intended
 *   publication of such source code.
 *****************************************************************************/
package com.venue.rest.model;

import java.util.ArrayList;

public class Eventotifications {
	ArrayList<EventModel> eventList = new ArrayList<EventModel>();
	private int size = 0;
	private int number = 0;
	private int totalPages = 0;
	private int numberOfElements = 0;
	private int totalElements = 0;
	private boolean lastPage = false;

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

	public ArrayList<EventModel> getEventList() {
		return eventList;
	}
	public void setEventList(ArrayList<EventModel> eventList) {
		this.eventList = eventList;
	}
}
