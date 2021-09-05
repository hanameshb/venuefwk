package com.venue.rest.model;

import java.util.ArrayList;

public class CheerLeaderPhotosFeed
{
	  
	private String  id="";
	private String  title;
	private String  pubdate="";
	private String  link="";
	private ArrayList  photoUrl=null;
	  
	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(ArrayList photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}



