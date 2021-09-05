package com.venue.rest.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CheerLeadersAggregatedResponse
{
	private String post_id = "";
	private String post_title = "";
	private String post_description = "";
	private String post_htmldescription = "";
	private String post_time = "";
	private String post_picture_url = "";
	private String video_url="";
	private String type="";	
	private String link="";
	
	private ArrayList<CheerLeadersPhotoGallaryFeed> photogallery = null;
	
	public ArrayList<CheerLeadersPhotoGallaryFeed> getPhotogallery() {
		return photogallery;
	}
	public void setPhotogallery(ArrayList<CheerLeadersPhotoGallaryFeed> photogallery) {
		this.photogallery = photogallery;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String videoUrl) {
		video_url = videoUrl;
	}
	
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getPost_description() {
		return post_description;
	}
	public void setPost_description(String post_description) {
		this.post_description = post_description;
	}
	public String getPost_time() {
		return post_time;
	}
	public void setPost_time(String post_time) {
		this.post_time = post_time;
	}
	
	public String getPost_picture_url() {
		return post_picture_url;
	}
	public void setPost_picture_url(String post_picture_url) 
	{
		this.post_picture_url = post_picture_url;
	}
	public String getPost_htmldescription() {
		return post_htmldescription;
	}
	public void setPost_htmldescription(String postHtmldescription) {
		post_htmldescription = postHtmldescription;
	}
	
	
}



