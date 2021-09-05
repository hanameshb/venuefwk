package com.venue.rest.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CheerleadersPhotosGalleryResponse
{

  private ArrayList<CheerLeadersPhotoGallaryFeed> cheerLeadersGalleryFeed =null;

		
	public ArrayList<CheerLeadersPhotoGallaryFeed> getCheerLeadersGalleryFeed() 
	{
		return cheerLeadersGalleryFeed;
	}
	public void setCheerLeadersGalleryFeed(
			ArrayList<CheerLeadersPhotoGallaryFeed> ls) 
	{
		this.cheerLeadersGalleryFeed = ls;
	}
	
}



