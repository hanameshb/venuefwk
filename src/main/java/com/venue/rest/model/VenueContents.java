package com.venue.rest.model;

import java.util.ArrayList;

public class VenueContents {
	ArrayList<VenueStory> contentList = new ArrayList<VenueStory>();
	private int size = 0;
	private int number = 0;
	private int totalPages = 0;
	private int numberOfElements = 0;
	private int totalElements = 0;
	private boolean lastPage = false;
	
	public ArrayList<VenueStory> getContentList() {
		return contentList;
	}
	public void setContentList(ArrayList<VenueStory> contentList) {
		this.contentList = contentList;
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
	
	
}
