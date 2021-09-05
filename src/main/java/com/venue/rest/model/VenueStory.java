package com.venue.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonIgnoreProperties(ignoreUnknown=true)
public class VenueStory {
	private String venue_id = "";
	private String emkit_api_key= "";
	private String story_id = "";
	private String story_title = "";
	private String story_htmldescription = "";
	private String story_pubDate = "";
	private String story_picture_url = "";
	private String story_name = "";
	private String category_name="";
	private String story_link="";
	private String story_author="";
	private String content_type = "";
	private String video_content_url = "";
	private String weblink_url = "";
	private String photo_url = "";
	private String story_external_reference_id = "";
	private String audio_content_url = "";
	
	@JsonInclude(Include.NON_NULL)
	private List<VenuePhoto> photoGallery = null;
	
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	public String getVenue_id() {
		return venue_id;
	}
	public void setVenue_id(String venue_id) {
		this.venue_id = venue_id;
	}
	public String getEmkit_api_key() {
		return emkit_api_key;
	}
	public void setEmkit_api_key(String emkit_api_key) {
		this.emkit_api_key = emkit_api_key;
	}
	public String getStory_id() {
		return story_id;
	}
	public void setStory_id(String story_id) {
		this.story_id = story_id;
	}
	public String getStory_title() {
		return story_title;
	}
	public void setStory_title(String story_title) {
		this.story_title = story_title;
	}	
	public String getStory_htmldescription() {
		return story_htmldescription;
	}
	public void setStory_htmldescription(String story_htmldescription) {
		this.story_htmldescription = story_htmldescription;
	}
	public String getStory_pubDate() {
		return story_pubDate;
	}
	public void setStory_pubDate(String story_pubDate) {
		this.story_pubDate = story_pubDate;
	}
	public String getStory_picture_url() {
		return story_picture_url;
	}
	public void setStory_picture_url(String story_picture_url) {
		this.story_picture_url = story_picture_url;
	}	
	public String getStory_name() {
		return story_name;
	}
	public void setStory_name(String story_name) {
		this.story_name = story_name;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getStory_link() {
		return story_link;
	}
	public void setStory_link(String story_link) {
		this.story_link = story_link;
	}
	public String getStory_author() {
		return story_author;
	}
	public void setStory_author(String story_author) {
		this.story_author = story_author;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getVideo_content_url() {
		return video_content_url;
	}
	public void setVideo_content_url(String video_content_url) {
		this.video_content_url = video_content_url;
	}
	public String getWeblink_url() {
		return weblink_url;
	}
	public void setWeblink_url(String weblink_url) {
		this.weblink_url = weblink_url;
	}
	public String getStory_external_reference_id() {
		return story_external_reference_id;
	}
	public void setStory_external_reference_id(String story_external_reference_id) {
		this.story_external_reference_id = story_external_reference_id;
	}
	public List<VenuePhoto> getPhotoGallery() {
		return photoGallery;
	}
	public void setPhotoGallery(List<VenuePhoto> photoGallery) {
		this.photoGallery = photoGallery;
	}
	public String getAudio_content_url() {
		return audio_content_url;
	}
	public void setAudio_content_url(String audio_content_url) {
		this.audio_content_url = audio_content_url;
	}
}
