package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.venue.rest.model.CheerLeadersAggregatedFeedRequest;
import com.venue.rest.model.CheerLeadersAggregatedResponse;
import com.venue.rest.model.CheerLeadersPhotoGallaryFeed;
import com.venue.rest.util.ErrorMessage;
import com.venue.rest.util.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
@Repository
public class CheerLeadersAggregatedFeedDAO 
{

	private static Logger logger = Logger.getLogger(CheerLeadersAggregatedFeedDAO.class);
		String errorCode = "";
		private JdbcTemplate jdbcTemplateObject=null;
		
		@Autowired
		@Qualifier("dataSourceVenue")
		DataSource dataSourceVenue;
		
		
		@PostConstruct
	    public void init() {
			jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
	    	
	    }

		/**
		 * Method to get Aggregate Feed response in JSON format.
		 */
		@SuppressWarnings("unchecked")
		public Object GetCheerLeadersAggregateFeed(CheerLeadersAggregatedFeedRequest clafr) {
	        try 
	        {
	    		logger.info("::in GetAggregateFeed:");

	        	Object claggregatedfeed = getCheerLeadersAggregateFeedDetailsLatest(clafr);
	        	if(claggregatedfeed!=null) 
	            {
	            	return claggregatedfeed;
	            }else
	            {
	            	errorCode="500";
	            	return ErrorMessage.getInstance().getErrorResponse(errorCode);
	            }
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			}
	    }
	
		
		
		/*
		 *   Method to get the List of CheerLeaders PhotoGallary Feed
		 *    
		 */
		@SuppressWarnings("unchecked")	
		 public ArrayList<com.venue.rest.model.CheerLeadersPhotoGallaryFeed> getCheerLeadersPhotoGalleryArray()
		 {
				String METHODNAME = "getCheerLeadersPhotoGalleryArray()";
				logger.info(METHODNAME);		
		        ArrayList photoGalleryArray = new ArrayList();
		        CheerLeadersPhotoGallaryFeed clgalleryDetails=null;
		        SqlRowSet rst=null;
				String query = "select * from tbl_dolphinscheerleaders_photo_gallery group by url";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					while (rst.next()) 
					{
						clgalleryDetails =new CheerLeadersPhotoGallaryFeed();
						clgalleryDetails.setPhotoUrl(rst.getString("url"));	        
						logger.info("Url:::"+rst.getString("url"));
			        	photoGalleryArray.add(clgalleryDetails);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return photoGalleryArray;
			}	 
		
		
		/*
		 *   Method to get the List of CheerLeaders PhotoGallary Feed
		 *    
		 */
		@SuppressWarnings("unchecked")	
		 public ArrayList<com.venue.rest.model.CheerLeadersPhotoGallaryFeed> getCheerLeadersPhotoGalleryArray(String itemId)
		 {
				String METHODNAME = "getCheerLeadersPhotoGalleryArray()";
				logger.info(METHODNAME);		
		        ArrayList photoGalleryArray = new ArrayList();
		        CheerLeadersPhotoGallaryFeed clgalleryDetails=null;
		        SqlRowSet rst=null;
				String query = "select * from tbl_dolphinscheerleaders_photos_mediacontent where item_id="+itemId+" group by url";
				try 
				{				
					rst = jdbcTemplateObject.queryForRowSet(query);
					while (rst.next()) 
					{
						clgalleryDetails =new CheerLeadersPhotoGallaryFeed();
						clgalleryDetails.setPhotoUrl(rst.getString("url"));	     
						clgalleryDetails.setTitle(rst.getString("gallery_title"));	        
						logger.info("Url:::"+rst.getString("url"));
			        	photoGalleryArray.add(clgalleryDetails);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return photoGalleryArray;
			}	 
		
		    public String RemoveHtmlTags(String StringwithHtmlTags)
		    {
		    	
		    	String regex = "<(\\S+)[^>]+?mso-[^>]*>.*?</\\1>";
		        String replacement = "";
		        StringwithHtmlTags = StringwithHtmlTags.replaceAll(regex, replacement);
		        logger.info(StringwithHtmlTags.replaceAll(regex, replacement));		        
		        return StringwithHtmlTags;		    	
		    }
		    
		    public static String removeHTML(String htmlString)
		    {
		          // Remove HTML tag from java String    
		        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
		        
		        // Remove Carriage return from java String
		        noHTMLString = noHTMLString.replaceAll("\\r", "");
		        String news2="",news="";
		        // Remove New line from java string and replace html break
		        noHTMLString = noHTMLString.replaceAll("\\n", " ");
		        if(noHTMLString.contains("brightcove.createExperiences();")) 
		        {
		            noHTMLString=noHTMLString.replaceAll("brightcove.createExperiences[()]+;", "");		        	  
		            noHTMLString=noHTMLString.replaceAll("&+[0-9]*;", "");
		            noHTMLString=noHTMLString.replaceAll("&+[a-z]*;", "");
		        }else
				{
					noHTMLString=noHTMLString.replaceAll("&#+[0-9]*;", "");
					noHTMLString=noHTMLString.replaceAll("&+[a-z]*;", "");
				}
		        
		        return noHTMLString;
		    }
		    
		    
		    /**
			 * Method to get Aggregate Feed response in JSON format.
			 */
			@SuppressWarnings("unchecked")
			public Object getCheerLeadersAggregateFeedDetailsLatest(CheerLeadersAggregatedFeedRequest clafr) {
				ArrayList<Object> ls=null;
				CheerLeadersAggregatedResponse cheerLeadersAggregatedResponse = null;
				int maxCount = 0;
		        String numberofitems="";
		        SqlRowSet sqlrowset=null;
				try{
					
					String start = "";
					String end = "";
					String until = "";
					String since = "";
					String type = "";
					String source="";
					int noi=0;
					until = clafr.getUntil();
					since = clafr.getSince();
					noi =clafr.getNoi();
					if (noi>0)
					{
						noi=noi;
						
					}else
					{
						ResourceBundle bundle = ResourceBundle.getBundle("db");        
			    		if(bundle.getString("noi")!=null)
			    		{
			    			numberofitems = bundle.getString("noi");
			    			
			    			if(numberofitems!=null && numberofitems.length()>0)
			    			{
			    				noi = Integer.parseInt(numberofitems);
			    			}	
			    		}
					}
					logger.info("getCheerLeadersAggregateFeedDetails:until"+until);
					logger.info("getCheerLeadersAggregateFeedDetails:since"+since);
					logger.info("getCheerLeadersAggregateFeedDetails:type"+type);
					logger.info("getCheerLeadersAggregateFeedDetails:noi"+noi); 
					 if(since!=null && since.length()>0)
					 {
							
							logger.info("getCheerLeadersAggregateFeedDetails:::Case1:");
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,type,link " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type" +
									" from tbl_dolphinscheerleaders_news_master) UNION ALL  (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,type as type from tbl_dolphinscheerleaders_photos_master)) as maintable " +
									"where post_time > "+since+" group by post_time order by post_time desc");
									
								*/
							
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,type,link,post_html_description " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description" +
									" from tbl_dolphinscheerleaders_news_master)) as maintable " +
									"where post_time > "+since+" group by post_time order by post_time desc");
									
							*/
							
							
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title,post_picture_url,post_description,post_time," +
									"type,link,post_html_description,video_url from(( select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description," +
									"video_url as video_url  from tbl_dolphinscheerleaders_news_master)  UNION ALL (select item_id as postid,name as post_title," +
									"short_desc as post_description,published_date as post_time,NULL as link,video_stillurl as post_picture_url,type as type," +
									"NULL as post_html_description,video_url as video_url from tbl_newsfeed_brightcove_videos)) as maintable where " +
									"post_time > '"+since+"' group by post_time order by post_time desc");
							

							logger.info("sqlrowset..."+sqlrowset);
			
						}else if(until!=null && until.length()>0) 
						{
							logger.info("getCheerLeadersAggregateFeedDetails:::Case2:");
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,type,link " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type " +
									"from tbl_dolphinscheerleaders_news_master) UNION ALL (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,type as type from tbl_dolphinscheerleaders_photos_master)) as maintable " +
									"where post_time < "+until+" group by post_time order by post_time desc limit "+noi+"");
									
								*/
							
							
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
							"post_picture_url,post_description,post_time,type,link,post_html_description " +
							"from((select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description " +
							"from tbl_dolphinscheerleaders_news_master)) as maintable " +
							"where post_time < "+until+" group by post_time order by post_time desc limit "+noi+"");
							*/
							
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title,post_picture_url,post_description,post_time," +
									"type,link,post_html_description,video_url from(( select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description," +
									"video_url as video_url  from tbl_dolphinscheerleaders_news_master)  UNION ALL (select item_id as postid,name as post_title," +
									"short_desc as post_description,published_date as post_time,NULL as link,video_stillurl as post_picture_url,type as type," +
									"NULL as post_html_description,video_url as video_url from tbl_newsfeed_brightcove_videos)) as maintable where " +
									"post_time < '"+until+"' group by post_time order by post_time desc limit "+noi+"");
						

							logger.info("sqlrowset..."+sqlrowset);
			
						}else 
						{
							logger.info("getCheerLeadersAggregateFeedDetails:::Case3:");
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
									"post_picture_url,post_description,post_time,type,link " +
									"from((select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type from tbl_dolphinscheerleaders_news_master) " +
									"UNION ALL (select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
									"link as link,photo_url as post_picture_url,type as type from tbl_dolphinscheerleaders_photos_master)) as maintable " +
									"group by post_time order by post_time desc limit "+noi+"");*/
							
							
							/*sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
							"post_picture_url,post_description,post_time,type,link,post_html_description " +
							"from((select item_id as postid,title as post_title,description as post_description," +
							"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description " +
							"from tbl_dolphinscheerleaders_news_master) " +
							") as maintable " +
							"group by post_time order by post_time desc limit "+noi+"");
							*/
							
							sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title,post_picture_url,post_description,post_time," +
									"type,link,post_html_description,video_url from(( select item_id as postid,title as post_title,description as post_description," +
									"pubdate as post_time,link as link,photo_url as post_picture_url,type as type,html_description as post_html_description," +
									"video_url as video_url  from tbl_dolphinscheerleaders_news_master)  UNION ALL (select item_id as postid,name as post_title," +
									"short_desc as post_description,published_date as post_time,NULL as link,video_stillurl as post_picture_url,type as type," +
									"NULL as post_html_description,video_url as video_url from tbl_newsfeed_brightcove_videos)) as maintable group by post_time" +
									" order by post_time desc limit "+noi+"");
							
							logger.info("sqlrowset:::"+sqlrowset);
						}
						
					   logger.info("sqlrowset:::"+sqlrowset);
					   int rowCount=0;
					   if(sqlrowset.first())
					   {
						   sqlrowset.last();
						   rowCount = sqlrowset.getRow();
					   }
						logger.info("rowCount"+rowCount);
						
						ls = new ArrayList<Object>();
					ArrayList<CheerLeadersAggregatedResponse> alt = new ArrayList<CheerLeadersAggregatedResponse>();
					if (rowCount > 0) 
		        	{
						sqlrowset.first();
						sqlrowset.previous();
						
						while(sqlrowset.next()) 
						{
							cheerLeadersAggregatedResponse = new CheerLeadersAggregatedResponse();
							cheerLeadersAggregatedResponse.setPost_id(sqlrowset.getString("postid"));
							String title ="";
							title  =sqlrowset.getString("post_title");
							
							logger.info("Title Before::::"+title);
							
							//String title1 = URLDecoder.decode(new String(title.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
							
							//String  title1 = new String(title.getBytes("US-ASCII"));
							
						    //String  title2 = new String(title.getBytes("UTF-8"));
						    
						    //String output=new String(title.getBytes("US-ASCII"), "ISO-8859-1");
						    
						    //logger.info("Title US ASCII ::::"+title1);
						    
							//logger.info("Title UTF-8 ::::"+title2);
							
							//logger.info("Title US-ASCII to ISO-8859-1 output::::"+output);

						    //String  title2 = new String(title.getBytes("UTF-8"));
							
						    String title2="";
						    if(title!=null && title.length()>0)
						    {	
						    	title2 = StringEscapeUtils.unescapeHtml(title);
						    	title2 = new String(title2.getBytes("UTF-8"),"UTF-8");
						    }
							logger.info("Title after ::::"+title2);
							cheerLeadersAggregatedResponse.setPost_title(title2);
							
							String description = "Not Available";
							String description1 = "Not Available";
							
							description = sqlrowset.getString("post_description");							
							if(description!=null && description.length()>0)
							{
								description = new String(description.getBytes("US-ASCII"));
								description = Utility.stripCDATA(description);
								description = removeHTML(description);
								description = description.replaceAll("\\<.*?\\>", "");
								description = description.replaceAll("&lt;div style=\"display:none\"&gt; &lt;/div&gt;"," ");
								description = description.replaceAll(" &#8230;", "");
								description = description.replaceAll("&#160;", "");
								description = description.replaceAll("\\?", " ");
								description1 = new String(description.getBytes("UTF-8"),"UTF-8");
							}
							
							logger.info("Decription After Removing Html Tags"+description1);							
							cheerLeadersAggregatedResponse.setPost_description(description1);
							String htmlDesc="",htmlDesc1="";													
							htmlDesc = sqlrowset.getString("post_html_description");
							if(htmlDesc!=null && htmlDesc.length()>0)
							{	
								htmlDesc = new String(htmlDesc.getBytes("US-ASCII"));
								//htmlDesc = StringEscapeUtils.unescapeHtml(htmlDesc);
								logger.info("before html text::::::::+"+htmlDesc);								
								htmlDesc=htmlDesc.replaceAll("&#65533;", "");
								htmlDesc=htmlDesc.replaceAll("</?a href=[^>]+>", "");	
								htmlDesc=htmlDesc.replaceAll("(</a>)+", " ");
								htmlDesc=htmlDesc.replaceAll("</?img src=[^>]+>", "");
								htmlDesc=htmlDesc.replaceAll("(</img>)+", "");
								htmlDesc=htmlDesc.replaceAll("\\?", " ");
								htmlDesc=htmlDesc.replaceAll("</?img class=[^>]+>", "");
								htmlDesc=htmlDesc.replaceAll("</?object id=[^>]+>", "");
								htmlDesc=htmlDesc.replaceAll("(</object>)+", "");
								htmlDesc=htmlDesc.replaceAll("</?iframe src=[^>]+>", "");
								htmlDesc=htmlDesc.replaceAll("(</iframe>)+", "");
								htmlDesc=htmlDesc.replaceAll("<br />", "");
								//htmlDesc=htmlDesc.replaceAll("�", "");
								//htmlDesc=htmlDesc.replaceAll("�", "");
								//<!--<br />\ By use of this code snippet, I agree to the Brightcove Publisher T and C<br />\ found at https://accounts.brightcove.com/en/terms-and-conditions/.<br />\ --></p>
								
								//String videoObject = "<object id=\"myExperience3703269810001\" class=\"BrightcoveExperience\"><param name=\"bgcolor\" value=\"#FFFFFF\" /><param name=\"width\" value=\"587\" /><param name=\"height\" value=\"326\" /><param name=\"playerID\" value=\"331422630001\" /><param name=\"playerKey\" value=\"AQ~~,AAAAPLps31k~,TJzr-TEF9ZA1JGNOESkvUzXsi5uGeB1i\" /><param name=\"isVid\" value=\"true\" /><param name=\"isUI\" value=\"true\" /><param name=\"dynamicStreaming\" value=\"true\" /><param name=\"@videoPlayer\" value=\"3703269810001\" /></object>";
								//String videoObject1 ="<object id=\"myExperience3705011147001\" class=\"BrightcoveExperience\"><param name=\"bgcolor\" value=\"#FFFFFF\" /><param name=\"width\" value=\"587\" /><param name=\"height\" value=\"326\" /><param name=\"playerID\" value=\"331422630001\" /><param name=\"playerKey\" value=\"AQ~~,AAAAPLps31k~,TJzr-TEF9ZA1JGNOESkvUzXsi5uGeB1i\" /><param name=\"isVid\" value=\"true\" /><param name=\"isUI\" value=\"true\" /><param name=\"dynamicStreaming\" value=\"true\" /><param name=\"@videoPlayer\" value=\"3705011147001\" /></object>";
								//htmlDesc=htmlDesc.replace(videoObject1, "");
								
								
								logger.info("after replace html text::::::::+"+htmlDesc);
								htmlDesc = StringEscapeUtils.unescapeHtml(htmlDesc);
								htmlDesc1 = new String(htmlDesc.getBytes("UTF-8"));
								
								logger.info("after unescape html text::::::::+"+htmlDesc1);
								
							}
							//cheerLeadersAggregatedResponse.setPost_htmldescription(sqlrowset.getString("post_html_description"));
							cheerLeadersAggregatedResponse.setPost_htmldescription(htmlDesc1);							
							cheerLeadersAggregatedResponse.setVideo_url(sqlrowset.getString("video_url"));							
							cheerLeadersAggregatedResponse.setPost_time(sqlrowset.getString("post_time"));
							cheerLeadersAggregatedResponse.setPost_picture_url(sqlrowset.getString("post_picture_url"));
							cheerLeadersAggregatedResponse.setType(sqlrowset.getString("type"));						
							cheerLeadersAggregatedResponse.setLink(sqlrowset.getString("link"));
							
							if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("photo"))
							{	
								cheerLeadersAggregatedResponse.setPost_picture_url(getCheerLeadersGalleryThumb(sqlrowset.getString("postid")));
								cheerLeadersAggregatedResponse.setPhotogallery(getCheerLeadersPhotoGalleryArray(sqlrowset.getString("postid")));
							}
				
							String post_picture_url = sqlrowset.getString("post_picture_url");					
							maxCount++;			
							
							String post_time="";        			
							post_time= sqlrowset.getString("post_time");
							if(until!=null  && !until.equals(" ") && until.length()>0)
		        			{	
		        	        	logger.info("getCheerLeadersAggregateFeedDetails:::case1");
		    		        	logger.info("maxCount"+maxCount);
		    		        	logger.info("rowCount"+rowCount);

		        				if(maxCount==rowCount)
			        			{	
		        					clafr.setUntil(post_time);
		        					clafr.setSince("");

			        			}
		        			}else if(since!=null  && !since.equals(" ") && since.length()>0)
		        			{	
		        				logger.info("getCheerLeadersAggregateFeedDetails:::case2");
		        				logger.info("maxCount"+maxCount);
		    		        	logger.info("rowCount"+rowCount);
		        				if(maxCount==1)
			        			{	
		        					clafr.setSince(post_time);
		        					clafr.setUntil("");
			        			}
		        			}else
		        			{
		        				logger.info("getCheerLeadersAggregateFeedDetails:::case3");  
			        			if(maxCount==1)
			        			{
			        				
			        				clafr.setSince(post_time);
			        				logger.info("untilTime:::"+post_time);
			        			}
			        			else if(maxCount==rowCount)
			        			{
			        				clafr.setUntil(post_time);
			        				logger.info("sinceTime:::"+post_time);
			        			}
		        			}
							
							alt.add(cheerLeadersAggregatedResponse);
						}
						
						clafr.setData(alt);								
						ls.add(clafr);
		        	}else
		        	{
		        		errorCode="1005";
				        return ErrorMessage.getInstance().getErrorResponse(errorCode);
		        	}
					
				}catch(Exception e)
				{
					e.printStackTrace();
					errorCode="500";
			        return ErrorMessage.getInstance().getErrorResponse(errorCode);
				}				
				return ls;
			}
			
			/*
			 *   Method to get the Audio Thumb
			 *    
			 */
			@SuppressWarnings("unchecked")
			public String getCheerLeadersGalleryThumb(String itemId)
			{   SqlRowSet rst=null;
		        String galleryThumbUrl="";			
		        try
		        {	
		        	String query = "select * from tbl_dolphinscheerleaders_photos_mediacontent where item_id="+itemId+"";
					try 
					{				
						rst = jdbcTemplateObject.queryForRowSet(query);
						if(rst.next()) 
						{	
							galleryThumbUrl=rst.getString("url");
							
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				 return galleryThumbUrl;
			}
			
			
			public String removeBoldTags(CharSequence htmlString) {
				  Pattern patt = Pattern.compile("<object id=([^<]*)</object>");
				  Matcher m = patt.matcher(htmlString);
				  StringBuffer sb = new StringBuffer(htmlString.length());
				  while (m.find()) {
				    String text = m.group(1);
				    // ... possibly process 'text' ...
				    m.appendReplacement(sb, Matcher.quoteReplacement(text));
				  }
				  m.appendTail(sb);
				  return sb.toString();
				}
		 	 

}
