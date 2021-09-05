package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.AppUserModel;
import com.venue.rest.model.SocialStreamResponseV2;
import com.venue.rest.util.ErrorMessage;
@Repository
public class AppUserDAO {

	private static Logger logger = Logger.getLogger(AppUserDAO.class);
	String errorCode = "";
	private static AppUserDAO instance = null;
	private JdbcTemplate jdbcTemplateObject = null;

	@Autowired
	AggregateFeedV2DAO aggregateFeedV2Dao;
	
	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }


	/**
	 * Method to create user
	 */
	public Object createUser(AppUserModel request) {
		logger.info(":::in createUser:::");
		logger.info(":::inside createUserV2::" +request.toString());
		AppUserModel userResponse = createUserResponse(request);
		if(userResponse != null){
			return userResponse;
		} else{
			errorCode = "500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}

	/**
	 * Method to create user response
	 */
	private AppUserModel createUserResponse(AppUserModel request) {
		logger.info(":::in createUserResponse:::");
		int i = 0;
		int[] j;
		SqlRowSet rst = null, rst1 = null;
		AppUserModel userResponse = new AppUserModel();
		HashMap<Object, Object> addProperty = new HashMap<Object, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try{
			//Object json = objectMapper.readValue(objectMapper.writeValueAsString(request), Object.class);
			logger.info("::appUserId::" +request.getAppUserId());
			logger.info("::Request object as String::" +objectMapper.writeValueAsString(request));
			//logger.info("::User Profile size before adding property::" +request.getUserProfile().size());
			if(request.getUserProfile() != null && request.getUserProfile().size() > 0){
				logger.info("::in if user profile is not empty::");
				ArrayList<HashMap<Object, Object>> userProfile = request.getUserProfile();
				for(int k = 0; k < userProfile.size(); k++)
				{
					//logger.info("::k value in for loop::" +k);
					String valueString = "";
					for (Map.Entry<Object, Object> entry : userProfile.get(k).entrySet())
					{
						//logger.info("::looping through entry set::");
						String key = (String)entry.getKey();
						//logger.info("::key::" +key);
						if(key.equals("propertyName"))
						{
							if(entry.getValue().equals("Email"))
								valueString=(String)entry.getValue();
							//logger.info("::valueString::" +valueString);
						}
						if(key.equals("propertyValue"))
						{
							Object value = entry.getValue();
							//logger.info("::value::" +value);

							if(valueString.equals("Email") && (value.equals("") || value == null || value.equals("null")))
							{
								logger.info("::in if value is null or empty::" +value);
								addProperty.put("propertyName", "Signed User");
								addProperty.put("propertyValue", "No");
							}
							else if(valueString.equals("Email")){
								logger.info("::in else value is not null or empty::" +value);
								addProperty.put("propertyName", "Signed User");
								addProperty.put("propertyValue", "Yes");
							}
							valueString = "";
						}
					}
				}
			}
			else {
				logger.info("::in else user profile is empty::");
				addProperty.put("propertyName", "Signed User");
        		addProperty.put("propertyValue", "No");
        		if(request.getUserProfile() == null){
        			request.setUserProfile(new ArrayList<HashMap<Object,Object>>());
        		}
			}
			request.getUserProfile().add(addProperty);
			logger.info("::User Profile size after adding property::" +request.getUserProfile().size());
			
			if((request.getAppUserId() != null && !request.getAppUserId().equalsIgnoreCase(""))){
				logger.info("::appUserId is not empty::");
				i = jdbcTemplateObject.update("update tbl_app_user_detail set device_key=?,skidata_user_id=?,emkit_user_id=? where app_user_id=?", 
						new Object[]{request.getDeviceKey(),request.getExternalUserIds().get("skidataUserId"),request.getExternalUserIds().get("emkitUserId"),request.getAppUserId()});
				if(i>0) {
					userResponse.setAppUserId(request.getAppUserId());
					userResponse.setDeviceKey(request.getDeviceKey());
					userResponse.setExternalUserIds(request.getExternalUserIds());
				}
			} else {
				logger.info("::appUserId is empty::");
				String appUserId = "";
				logger.info("::ExternalUserIds::"+request.getExternalUserIds());
				
				//This is the condition where user logged in using SKIDATA so external user ids were given in request BUT checking whether user exists in our system or not.
				if(request.getExternalUserIds() != null && !request.getExternalUserIds().isEmpty() 
						&& request.getExternalUserIds().get("skidataUserId") != null && !request.getExternalUserIds().get("skidataUserId").equals("")) {
					logger.info("::externalUserIds are not empty::");
					if(request.getDeviceKey() != null && !request.getDeviceKey().equals(""))
						rst = jdbcTemplateObject.queryForRowSet("select app_user_id from tbl_app_user_detail where skidata_user_id="+request.getExternalUserIds().get("skidataUserId")+" and device_key='"+request.getDeviceKey()+"'");
					else
						rst = jdbcTemplateObject.queryForRowSet("select app_user_id from tbl_app_user_detail where skidata_user_id="+request.getExternalUserIds().get("skidataUserId"));
					if(rst.next()){
						logger.info("::appUserId is available for skidataUserId::" +request.getExternalUserIds().get("skidataUserId"));
						appUserId = rst.getString("app_user_id");
						logger.info("::appUserId from database::" +appUserId);
						if(request.getUserProfile() != null && request.getUserProfile().size()>0){
							logger.info("::userProfile is not empty::");
							for(HashMap<Object, Object> object:request.getUserProfile()){
								if(object.get("propertyName").toString().equalsIgnoreCase("email")){
									rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and property_name='"+object.get("propertyName")+"' and property_value='"+object.get("propertyValue")+"'");
								}
							}
							if(rst1.next()) {
								logger.info("::appUserId is available in user property detail table::");
								userResponse.setAppUserId(appUserId);
								userResponse.setDeviceKey(request.getDeviceKey());
								userResponse.setExternalUserIds(request.getExternalUserIds());
							} else {
								logger.info("::appUserId is not available in user property detail table::");
								logger.info("::This scenario wont happen::");
								
								userResponse = insertAppUserDetails(request);
							}
						}
					}
				    /// [asim] - This is the condition where user logged in using SKIDATA so external user ids were given in request BUT the user didn't exist in our system. fixing bug where user was not created in such situation; Functional scenario - signing into the app as a SKIDATA user for the first time
					else
					{
						logger.info("::appUserId is not available for skidataUserId::" +request.getExternalUserIds().get("skidataUserId"));
						
						if(request.getDeviceKey() != null && !request.getDeviceKey().equals("")) {
							logger.info("::deviceKey is present::");
							rst = jdbcTemplateObject.queryForRowSet("select app_user_id from tbl_app_user_detail where device_key='"+request.getDeviceKey()+"'");
							if(rst.next()){
								logger.info("::appUserId already exists in app user detail table::");
								appUserId = rst.getString("app_user_id");
								logger.info("::appUserId from database::" +appUserId);
								userResponse.setAppUserId(appUserId);
								userResponse.setDeviceKey(request.getDeviceKey());
								userResponse.setExternalUserIds(request.getExternalUserIds());
							} else {
								logger.info("::appUserId not exists in app user detail table::");
								userResponse = insertAppUserDetails(request);
							}
						} else {
							logger.info("::deviceKey is not present::");
							userResponse = insertAppUserDetails(request);
						}
					}
				}
			    /// [asim] - This is the condition where user skipped signing in
				else {
					logger.info("::externalUserIds are empty::");
					
					if(request.getDeviceKey() != null && !request.getDeviceKey().equals("")) {
						logger.info("::deviceKey is present::");
						rst = jdbcTemplateObject.queryForRowSet("select app_user_id from tbl_app_user_detail where device_key='"+request.getDeviceKey()+"'");
						if(rst.next()){
							logger.info("::appUserId already exists in app user detail table::");
							appUserId = rst.getString("app_user_id");
							logger.info("::appUserId from database::" +appUserId);
							userResponse.setAppUserId(appUserId);
							userResponse.setDeviceKey(request.getDeviceKey());
						} else {
							logger.info("::appUserId not exists in app user detail table::");
							userResponse = insertAppUserDetails(request);
						}
					} else {
						logger.info("::deviceKey is not present::");
						userResponse = insertAppUserDetails(request);
					}
				}
			}
		}catch(Exception e){
			logger.info("::Exception in creating app user::" +e.getLocalizedMessage());
			e.printStackTrace();
		}
		return userResponse;
	}
	
	/**
	 * Method to insert app user details
	 */
	public AppUserModel insertAppUserDetails(AppUserModel request) throws Exception {
		
		String appUserId = "";
		int i = 0;
		int[] j;
		AppUserModel userResponse = new AppUserModel();
		
		appUserId = generateUUID();
		logger.info("::new appUserID created::" +appUserId);
		
		//insert app user details
		if(request.getExternalUserIds() != null && !request.getExternalUserIds().isEmpty() 
				&& request.getExternalUserIds().get("skidataUserId") != null && !request.getExternalUserIds().get("skidataUserId").equals(""))
			i = jdbcTemplateObject.update("insert into tbl_app_user_detail (app_user_id,device_key,skidata_user_id,emkit_user_id,created_time) values(?,?,?,?,now())", 
					new Object[]{appUserId,request.getDeviceKey(),request.getExternalUserIds().get("skidataUserId"),request.getExternalUserIds().get("emkitUserId")});
		else if(request.getExternalUserIds() != null && !request.getExternalUserIds().isEmpty() 
				&& request.getExternalUserIds().get("emkitUserId") != null && !request.getExternalUserIds().get("emkitUserId").equals(""))
			i = jdbcTemplateObject.update("insert into tbl_app_user_detail (app_user_id,device_key,emkit_user_id,created_time) values(?,?,?,now())", 
					new Object[]{appUserId,request.getDeviceKey(),request.getExternalUserIds().get("emkitUserId")});
		else
			i = jdbcTemplateObject.update("insert into tbl_app_user_detail (app_user_id,device_key,created_time) values(?,?,now())", 
					new Object[]{appUserId,request.getDeviceKey()});
		
		//check for any user properties and insert
		if(request.getUserProfile() != null && request.getUserProfile().size()>0) {
			logger.info("::userProfile is not empty::");
			List<Object[]> userParameter = new ArrayList<Object[]>();
			for(HashMap<Object, Object> object : request.getUserProfile()) {
				userParameter.add(new Object[]{appUserId,object.get("propertyValue"),object.get("propertyName")});
			}
			j = jdbcTemplateObject.batchUpdate("insert into tbl_user_detail(app_user_id,property_value,property_name,created_time) values(?,?,?,now())", userParameter);
			if(j.length>0) {
				userResponse.setUserProfile(request.getUserProfile());
			}
		}
		
		if(i>0) {
			userResponse.setAppUserId(appUserId);
			userResponse.setDeviceKey(request.getDeviceKey());
			userResponse.setExternalUserIds(request.getExternalUserIds());
		}
		return userResponse;
	}
	
	/**
	 * Method to create unique user ID
	 */
	public static String generateUUID() {
		Calendar currentDate = Calendar.getInstance();
	    Long time = currentDate.getTimeInMillis();
	    String s;
	    s=Long.toHexString(time);
		return s;
	}
	
	/**
	 * Method to update and get user response
	 */
	public Object updateUser(String appUserId,AppUserModel request) {
		logger.info(":::in updateUser:::" +appUserId);
		logger.info(":::inside updateUserV2::" +request.toString());
		Object UserDetail=updateUserDetails(appUserId,request);
		if(UserDetail!=null){
			return UserDetail;
		}else{
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to update user details
	 */
	private Object updateUserDetails(String appUserId,AppUserModel request) {
		logger.info(":::in updateUserDetails:::"+appUserId);
		int[] i;
		SqlRowSet rst=null;
		AppUserModel userDetailResponse=new AppUserModel();
		List<Object[]> userParameter = new ArrayList<Object[]>();
		HashMap<Object, Object> addProperty = new HashMap<Object, Object>();
		int skidataUserId=0;
		String emkitUserId="";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			logger.info("::Request object as String::"+objectMapper.writeValueAsString(request));
			//logger.info("::User Profile size before adding property::" +request.getUserProfile().size());
			if(request.getUserProfile()!=null && request.getUserProfile().size() > 0){
				logger.info("::in if user profile is not empty::");
				ArrayList<HashMap<Object, Object>> userProfile = request.getUserProfile();
				for(int k = 0; k < userProfile.size(); k++)
				{
					logger.info("::k value in for loop::" +k);
					String valueString = "";
					for (Map.Entry<Object, Object> entry : userProfile.get(k).entrySet())
					{
						logger.info("::looping through entry set::");
						String key = (String)entry.getKey();
						logger.info("::key::" +key);
						if(key.equals("propertyName"))
						{
							if(entry.getValue().equals("Email"))
								valueString=(String)entry.getValue();
							logger.info("::valueString::" +valueString);
						}
						if(key.equals("propertyValue"))
						{
							Object value = entry.getValue();
							logger.info("::value::" +value);

							if(valueString.equals("Email") && (value.equals("") || value == null || value.equals("null")))
							{
								logger.info("::in if value is null or empty::" +value);
								addProperty.put("propertyName", "Signed User");
								addProperty.put("propertyValue", "No");
							}
							else if(valueString.equals("Email")){
								logger.info("::in else value is not null or empty::" +value);
								addProperty.put("propertyName", "Signed User");
								addProperty.put("propertyValue", "Yes");
							}
							valueString = "";
						}
					}
				}
			}
			else {
				logger.info("::in else user profile is empty::");
				addProperty.put("propertyName", "Signed User");
        		addProperty.put("propertyValue", "No");
        		if(request.getUserProfile()==null){
        			request.setUserProfile(new ArrayList<HashMap<Object,Object>>());
        		}
			}
			request.getUserProfile().add(addProperty);
			logger.info("::User Profile size after adding property::" +request.getUserProfile().size());
			logger.info(":::inside createUserResponse::" +request.toString());
			String userCheckQuery="select * from tbl_app_user_detail where app_user_id=?";
			rst=jdbcTemplateObject.queryForRowSet(userCheckQuery,new Object[]{appUserId});
			if(rst.next()){
				logger.info(":::app user exists:::");
				userDetailResponse.setAppUserId(appUserId);
				userDetailResponse.setDeviceKey(request.getDeviceKey());
				userDetailResponse.setExternalUserIds(request.getExternalUserIds());
				//START DOL-795
				HashMap<Object, Object> externalUserIds=request.getExternalUserIds();
				if(externalUserIds.size()> 0)
				{
					for(Map.Entry<Object, Object> entry : externalUserIds.entrySet()){
						logger.info(":::skidataUserId:::"+entry.getKey());
						//logger.info(":::skidataUserId:::"+entry.getValue());
						if(entry.getKey().equals("skidataUserId"))
						{
							logger.info(":::skidataUserId:::"+entry.getValue());
							if(!entry.getValue().equals("null") ){
								logger.info(":::in if skidataUserId:::");
								skidataUserId=Integer.parseInt(String.valueOf(entry.getValue()));
							}
							logger.info(":::skidataUserId INT VALUE:::"+skidataUserId);
						}
						if(entry.getKey().equals("emkitUserId"))
						{
							logger.info(":::emkitUserId:::"+entry.getValue());
							if(entry.getValue() != null && !entry.getValue().equals(null) && !entry.getValue().equals("")){
								logger.info(":::in if emkitUserId:::");
								emkitUserId=String.valueOf(entry.getValue());
							}
							logger.info(":::emkitUserId Value:::"+emkitUserId);
						}
					}
					jdbcTemplateObject.update("update tbl_app_user_detail set skidata_user_id=?,emkit_user_id=? where app_user_id=?",new Object[]{skidataUserId,emkitUserId,appUserId});
				}
				//END DOL-795
				if(request.getUserProfile()!=null && request.getUserProfile().size()>0){
					logger.info(":::user details object exists:::");
					String userDetailsDeleteQuery="delete from tbl_user_detail where app_user_id=?";
					jdbcTemplateObject.update(userDetailsDeleteQuery,new Object[]{appUserId});
					logger.info(":::userDetailsDeleteQuery:::"+userDetailsDeleteQuery);
					String userDetailsInsertQuery="insert into tbl_user_detail(app_user_id,property_value,property_name,created_time) values(?,?,?,now())";
					for(HashMap<Object, Object> object:request.getUserProfile()){
						userParameter.add(new Object[]{appUserId,object.get("propertyValue"),object.get("propertyName")});
					}
					logger.info(":::userDetailsInsertQuery:::"+userDetailsInsertQuery);
					i=jdbcTemplateObject.batchUpdate(userDetailsInsertQuery,userParameter);
					if(i.length>0){
						userDetailResponse.setUserProfile(request.getUserProfile());
					}
					return userDetailResponse;
				}else{
					logger.info(":::user object missing:::");
					errorCode="1008";
					return ErrorMessage.getInstance().getErrorResponse(errorCode);
				}
			}else{
				logger.info(":::app user not exists:::");
				errorCode="1009";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} catch (Exception e) {
			userDetailResponse=null;
			logger.info(":::Exception in updating user details:::" +e.getLocalizedMessage());
			e.printStackTrace();
			return userDetailResponse;
		}
	}

	/**
	 * Method to get user response
	 */
	public Object getAppUser(String appUserId) {
		logger.info(":::in getAppUser:::"+appUserId);
			Object UserDetail=getAppUserDetails(appUserId);
			if(UserDetail!=null){
				return UserDetail;
			}else{
				errorCode="500";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
	}
	
	/**
	 * Method to get App user  response
	 */
	private Object getAppUserDetails(String appUserId) {
		logger.info(":::in getAppUserDetails:::"+appUserId);
		SqlRowSet rst=null,rst1=null;
		AppUserModel userDetailResponse=new AppUserModel();
		HashMap<Object, Object> userInfo=null;
		ArrayList<HashMap<Object, Object>> userObject=null;
		try {
			String userCheckQuery="select * from tbl_app_user_detail where app_user_id=?";
			rst=jdbcTemplateObject.queryForRowSet(userCheckQuery,new Object[]{appUserId});
			if(rst.next()){
				logger.info(":::App user exists:::");
				userDetailResponse.setAppUserId(rst.getString("app_user_id"));
				userDetailResponse.setDeviceKey(rst.getString("device_key"));
				HashMap<Object, Object> userIds=new HashMap<Object, Object>();
				userIds.put("skidataUserId", rst.getString("skidata_user_id"));
				userIds.put("emkitUserId", rst.getString("emkit_user_id"));
				userDetailResponse.setExternalUserIds(userIds);
					String userDetailsQuery="select * from tbl_user_detail where app_user_id=?";
					rst1=jdbcTemplateObject.queryForRowSet(userDetailsQuery,new Object[]{rst.getString("app_user_id")});
					userObject=new ArrayList<HashMap<Object,Object>>();
					while(rst1.next()){
						userInfo=new HashMap<Object, Object>();
						userInfo.put("propertyName", rst1.getString("property_name"));
						userInfo.put("propertyValue", rst1.getString("property_value"));
						userObject.add(userInfo);
					}
					userDetailResponse.setUserProfile(userObject);
					logger.info("User Detail Response:::"+userDetailResponse.toString());
					return userDetailResponse;
			}else{
				logger.info(":::App user not exists:::");
				errorCode="1009";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} catch (Exception e) {
			userDetailResponse=null;
			logger.info(":::Exception in getAppUserDetails:::"+e.getLocalizedMessage());
			e.printStackTrace();
			return userDetailResponse;
		}
	}

	/**
	 * Method to get External User Id's response
	 */
	public Object getExternalUserIds(String appUserId) {
		logger.info(":::in getExternalUserIds:::"+appUserId);
		Object ExternalUserIds=getExternalUserIdsResponse(appUserId);
		if(ExternalUserIds!=null){
			return ExternalUserIds;
		}else{
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	/**
	 * Method to check and get External User Id's response
	 */
	private Object getExternalUserIdsResponse(String appUserId) {
		logger.info(":::in getExternalUserIdsResponse:::"+appUserId);
		SqlRowSet rst=null;
		HashMap<Object, Object> userIds=null;
		try {
			String userCheckQuery="select * from tbl_app_user_detail where app_user_id=?";
			rst=jdbcTemplateObject.queryForRowSet(userCheckQuery,new Object[]{appUserId});
			if(rst.next()){
				logger.info(":::App user exists:::");
				userIds=new HashMap<Object, Object>();
				userIds.put("skidataUserId", rst.getString("skidata_user_id"));
				userIds.put("emkitUserId", rst.getString("emkit_user_id"));
					logger.info("External User Id Response:::"+userIds.toString());
					return userIds;
			}else{
				logger.info(":::App user not exists:::");
				errorCode="1009";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} catch (Exception e) {
			userIds=null;
			logger.info(":::Exception in getExternalUserIdsResponse:::"+e.getLocalizedMessage());
			e.printStackTrace();
			return userIds;
		}
	}

	/**
	 * Method to get User detail response
	 */
	public Object getUserProfileDetail(String appUserId) {
		logger.info(":::in getUserProfileDetail:::"+appUserId);
			Object UserDetail=getUserProfileDetailResponse(appUserId);
			if(UserDetail!=null){
				return UserDetail;
			}else{
				errorCode="500";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
	}
	
	/**
	 * Method to check and get User detail response
	 */
	private Object getUserProfileDetailResponse(String appUserId) {
		logger.info(":::in getUserProfileDetailResponse:::"+appUserId);
		SqlRowSet rst=null,rst1=null;
		HashMap<Object, Object> userInfo=null;
		ArrayList<HashMap<Object, Object>> userObject=null;
		try {
			String userCheckQuery="select * from tbl_app_user_detail where app_user_id=?";
			rst=jdbcTemplateObject.queryForRowSet(userCheckQuery,new Object[]{appUserId});
			if(rst.next()){
				logger.info(":::App user exists:::");
					String userDetailsQuery="select * from tbl_user_detail where app_user_id=?";
					rst1=jdbcTemplateObject.queryForRowSet(userDetailsQuery,new Object[]{rst.getString("app_user_id")});
					userObject=new ArrayList<HashMap<Object,Object>>();
					while(rst1.next()){
						userInfo=new HashMap<Object, Object>();
						userInfo.put("propertyName", rst1.getString("property_name"));
						userInfo.put("propertyValue", rst1.getString("property_value"));
						userObject.add(userInfo);
					}
					logger.info("User Profile Detail Response:::"+userObject.toString());
					return userObject;
			}else{
				logger.info(":::App user not exists:::");
				errorCode="1009";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		} catch (Exception e) {
			userObject=null;
			logger.info(":::Exception in getUserProfileDetailResponse:::"+e.getLocalizedMessage());
			e.printStackTrace();
			return userObject;
		}
	}
	
	public Object getFavoritefeedPosts(String appUserId) {
		logger.info(":::in getFavoritefeedPosts:::"+appUserId);
		Object UserFavoritefeed=getUserFavoriteFeedResponse(appUserId);
		if(UserFavoritefeed!=null){
			return UserFavoritefeed;
		}else{
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}

	private Object getUserFavoriteFeedResponse(String appUserId) {
		logger.info(":::in getUserFavoriteFeedResponse:::"+appUserId);
		SqlRowSet sqlrowset=null,rst=null;
		SocialStreamResponseV2 post=null;
		ArrayList<SocialStreamResponseV2> favoriteFeed=new ArrayList<SocialStreamResponseV2>();
		Hashtable<String,Object> data=new Hashtable<String, Object>();
		try {
			if(appUserId!=null&&!appUserId.equalsIgnoreCase("")){
				rst=jdbcTemplateObject.queryForRowSet("select * from tbl_user_post_favorited_mapping where app_user_id='"+appUserId+"' and user_favorite_status=1");
				while(rst.next()){
					if(rst.getString("type").equalsIgnoreCase("news")){
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,html_description as post_html_description," +
								"mark_for_delete as delete_status from tbl_newsfeed)) as maintable " +
								"where delete_status !=1 and postid = '"+rst.getString("post_id")+"' group by post_time order by post_time desc");
					}else if(rst.getString("type").equalsIgnoreCase("photo")){
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
								"from((select item_id as postid,title as post_title,description as post_description," +
								"pubdate as post_time,link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_photo_gallaries_master) " +
								") as maintable " +
								"where delete_status !=1 and  postid = '"+rst.getString("post_id")+"' group by post_time order by post_time desc");
					}else if(rst.getString("type").equalsIgnoreCase("video")){
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
								"from((select item_id as postid,title as post_title,description as post_description,pubdate as post_time," +
								"link as link,photo_url as post_picture_url,video_url as video_url,type as type,NULL as audio_url,NULL as post_html_description,mark_for_delete as delete_status from tbl_videos_master "+
								")) as maintable " +
								"where delete_status !=1 and  postid = '"+rst.getString("post_id")+"' group by post_time order by post_time desc");
					}else if(rst.getString("type").equalsIgnoreCase("audio")){
						sqlrowset = jdbcTemplateObject.queryForRowSet("select distinct postid,post_title," +
								"post_picture_url,post_description,post_time,video_url,type,link,audio_url,post_html_description,delete_status " +
								"from((select item_id as postid" +
								",title as post_title,description as post_description,pubdate as post_time, link as link,photo_url as post_picture_url,NULL as video_url,type as type,audio_url as audio_url,NULL as post_html_description" +
								",mark_for_delete as delete_status from tbl_audio_master)) as maintable " +
								"where delete_status !=1 and postid = '"+rst.getString("post_id")+"' group by post_time order by post_time desc");
					}
					if(sqlrowset.next()){
						post = new SocialStreamResponseV2();
						post.setPost_id(sqlrowset.getString("postid"));
						//String  title1 = new String(sqlrowset.getString("post_title").getBytes("ISO-8859-1"));

						String  title1="",title2="";
						title1 = sqlrowset.getString("post_title");
						logger.info("Title before ::::"+title1);
						if(title1!=null && title1.length()>0)
						{
							title1 = StringEscapeUtils.unescapeHtml(title1);
							title2 = new String(title1.getBytes("UTF-8"),"UTF-8");
						}
						logger.info("Title after ::::"+title2);

						post.setPost_title(title2);

						String desc1="",desc2="",htmldesc1="",htmldesc2="";

						desc1 = sqlrowset.getString("post_description");

						if(desc1!=null && desc1.length()>0)
						{
							desc1 = StringEscapeUtils.unescapeHtml(desc1);
							desc2 = new String(desc1.getBytes("UTF-8"),"UTF-8");
						}
						post.setPost_description(desc2);
						htmldesc1 = sqlrowset.getString("post_html_description");

						if(htmldesc1!=null && htmldesc1.length()>0)
						{
							htmldesc1 = new String(htmldesc1.getBytes("US-ASCII"));

							htmldesc1=htmldesc1.replaceAll("</?a href=[^>]+>", "");
							htmldesc1=htmldesc1.replaceAll("(</a>)+", "");
							htmldesc1=htmldesc1.replaceAll("</?img src=[^>]+>", "");
							htmldesc1=htmldesc1.replaceAll("(</img>)+", "");
							//htmldesc1 = StringEscapeUtils.unescapeHtml(htmldesc1);
							htmldesc2 = new String(htmldesc1.getBytes("UTF-8"),"UTF-8");

						}
						post.setPost_htmldescription(htmldesc2);
						post.setPost_time(sqlrowset.getString("post_time"));

						post.setPost_picture_url(sqlrowset.getString("post_picture_url"));

						post.setVideo_url(sqlrowset.getString("video_url"));
						post.setAudio_url(sqlrowset.getString("audio_url"));

						post.setType(sqlrowset.getString("type"));
						post.setLink(sqlrowset.getString("link"));

						logger.info("Link from db  is :::::::::::::"+sqlrowset.getString("link"));
						logger.info("post time from db  is :::::::::::::"+sqlrowset.getString("post_time"));

						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("video"))
						{
							post.setVideo_url(aggregateFeedV2Dao.getVideoUrl(sqlrowset.getString("postid")));
							post.setPost_picture_url(aggregateFeedV2Dao.getVideoThumb(sqlrowset.getString("postid")));
						}
						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("audio"))
						{
							post.setAudio_url(aggregateFeedV2Dao.getAudioUrl(sqlrowset.getString("postid")));
							post.setPost_picture_url(aggregateFeedV2Dao.getAudioThumb(sqlrowset.getString("postid")));
						}

						if(sqlrowset.getString("type")!=null && sqlrowset.getString("type").equalsIgnoreCase("photo"))
						{
							post.setPhotogallery(aggregateFeedV2Dao.getGalleryArray(sqlrowset.getString("postid")));
							post.setPost_picture_url(aggregateFeedV2Dao.getGalleryThumb(sqlrowset.getString("postid")));
						}
						if(sqlrowset.getString("type")!=null &&!sqlrowset.getString("type").equalsIgnoreCase(""))
							post.setUserFavoritedStatus(aggregateFeedV2Dao.getFavoritedStatus(appUserId,sqlrowset.getString("postid"),sqlrowset.getString("type")));

						favoriteFeed.add(post);
					}
				}
				data.put("data", favoriteFeed);
			}else {
				data=null;
			}
		} catch (Exception e) {
			data=null;
			logger.info(":::Exception in getting user favourited feed:::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return data;
	}
}
