package com.venue.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.MemberCentralModel;
import com.venue.rest.util.ErrorMessage;
@Repository
public class MemberCentralDao {

	private static Logger logger = Logger.getLogger(MemberCentralDao.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;

	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }

	
	public Object getMemberCentralList(String appUserId) {
		logger.info("::in getMemberCentralList::");
		SqlRowSet rst=null;
		SqlRowSet rst1=null;
		String memberTypeCategory = "";
		String memberKey = "";
		
		try {
			logger.info("::appUserId in getMemberCentralList::" +appUserId);
			ArrayList<MemberCentralModel> membercentralList=new ArrayList<MemberCentralModel>();
			rst = jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and property_name='memberTypeCategory'");
			while(rst != null && rst.next()){
				memberTypeCategory = rst.getString("property_value");
			}
			logger.info("::memberTypeCategory in getMemberCentralList::" +memberTypeCategory);
			
			rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_map_membercentral where staus=0 order by sort_id");
			while(rst1.next()){
				MemberCentralModel model=new MemberCentralModel();
				memberKey = rst1.getString("member_key");
				model.setKey(memberKey);
				model.setDeeplink_url(rst1.getString("deeplink_url"));
				model.setIcon(rst1.getString("icon"));
				model.setSelected_icon(rst1.getString("selected_icon"));
				model.setSort_order_id(rst1.getString("sort_id"));
				model.setType(rst1.getString("type"));
				model.setValue(rst1.getString("member_value"));
				logger.info("::black_card_visibility::" +rst1.getBoolean("black_card_visibility"));
				if(memberTypeCategory != null && memberTypeCategory.equalsIgnoreCase("Black Card")) {
					if(memberKey != null && memberKey.equalsIgnoreCase("coastalcar") && rst1.getBoolean("black_card_visibility"))
						membercentralList.add(model);
					else if(memberKey != null && !memberKey.equalsIgnoreCase("coastalcar"))
						membercentralList.add(model);
				} else {
					if(memberKey != null && !memberKey.equalsIgnoreCase("coastalcar"))
						membercentralList.add(model);
				}
			}
			HashMap<String, Object> otherAssets=new HashMap<String, Object>();
			otherAssets.put("backgroundImage", "");
			otherAssets.put("backgroundColor", "white");
			otherAssets.put("bannerColor", "aqua");
			otherAssets.put("bannerImage", "");
			HashMap<String, Object> response=new HashMap<String, Object>();
			response.put("membercentralList", membercentralList);
			response.put("otherAssets", otherAssets);
			return response;
		} catch (Exception e) {
			logger.error("::Exception in getMemberCentralList::" +e);
			e.printStackTrace();
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}

	}
	
	public Object getMemberCentralListNonSTM() {
		logger.info("::in getMemberCentralListNonSTM::");
		SqlRowSet rst=null;
		
		try {
			ArrayList<MemberCentralModel> membercentralList=new ArrayList<MemberCentralModel>();
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_map_membercentral where staus=1 order by sort_id");
			while(rst.next()){
				MemberCentralModel model=new MemberCentralModel();
				model.setKey(rst.getString("member_key"));
				model.setDeeplink_url(rst.getString("deeplink_url"));
				model.setIcon(rst.getString("icon"));
				model.setSelected_icon(rst.getString("selected_icon"));
				model.setSort_order_id(rst.getString("sort_id"));
				model.setType(rst.getString("type"));
				model.setValue(rst.getString("member_value"));
				membercentralList.add(model);
			}
			HashMap<String, Object> otherAssets=new HashMap<String, Object>();
			otherAssets.put("backgroundImage", "");
			otherAssets.put("backgroundColor", "white");
			otherAssets.put("bannerColor", "aqua");
			otherAssets.put("bannerImage", "");
			HashMap<String, Object> response=new HashMap<String, Object>();
			response.put("membercentralList", membercentralList);
			response.put("otherAssets", otherAssets);
			return response;
		} catch (Exception e) {
			logger.error("::Exception in getMemberCentralListNonSTM::" +e);
			e.printStackTrace();
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}
	
	
	
	
	public Object getMemberCentralListV21(String appUserId) {
		logger.info("::in getMemberCentralList::");
		SqlRowSet rst=null;
		SqlRowSet rst1=null;
		String memberTypeCategory = "";
		String memberKey = "";
		
		try {
			logger.info("::appUserId in getMemberCentralList::" +appUserId);
			ArrayList<MemberCentralModel> membercentralList=new ArrayList<MemberCentralModel>();
			rst = jdbcTemplateObject.queryForRowSet("select * from tbl_user_detail where app_user_id='"+appUserId+"' and property_name='memberTypeCategory'");
			while(rst != null && rst.next()){
				memberTypeCategory = rst.getString("property_value");
			}
			logger.info("::memberTypeCategory in getMemberCentralList::" +memberTypeCategory);
			
			rst1 = jdbcTemplateObject.queryForRowSet("select * from tbl_map_membercentral_V21 where staus=0 order by sort_id");
			while(rst1.next()){
				MemberCentralModel model=new MemberCentralModel();
				memberKey = rst1.getString("member_key");
				model.setKey(memberKey);
				model.setDeeplink_url(rst1.getString("deeplink_url"));
				model.setIcon(rst1.getString("icon"));
				model.setSelected_icon(rst1.getString("selected_icon"));
				model.setSort_order_id(rst1.getString("sort_id"));
				model.setType(rst1.getString("type"));
				model.setValue(rst1.getString("member_value"));
				logger.info("::black_card_visibility::" +rst1.getBoolean("black_card_visibility"));
				if(memberTypeCategory != null && memberTypeCategory.equalsIgnoreCase("Black Card")) {
					if(memberKey != null && memberKey.equalsIgnoreCase("coastalcar") && rst1.getBoolean("black_card_visibility"))
						membercentralList.add(model);
					else if(memberKey != null && !memberKey.equalsIgnoreCase("coastalcar"))
						membercentralList.add(model);
				} else {
					if(memberKey != null && !memberKey.equalsIgnoreCase("coastalcar"))
						membercentralList.add(model);
				}
			}
			HashMap<String, Object> otherAssets=new HashMap<String, Object>();
			otherAssets.put("backgroundImage", "");
			otherAssets.put("backgroundColor", "white");
			otherAssets.put("bannerColor", "aqua");
			otherAssets.put("bannerImage", "");
			HashMap<String, Object> response=new HashMap<String, Object>();
			response.put("membercentralList", membercentralList);
			response.put("otherAssets", otherAssets);
			return response;
		} catch (Exception e) {
			logger.error("::Exception in getMemberCentralList::" +e);
			e.printStackTrace();
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}

	}
	
	public Object getMemberCentralListNonSTMV21() {
		logger.info("::in getMemberCentralListNonSTM::");
		SqlRowSet rst=null;
		
		try {
			ArrayList<MemberCentralModel> membercentralList=new ArrayList<MemberCentralModel>();
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_map_membercentral_V21 where staus=1 order by sort_id");
			while(rst.next()){
				MemberCentralModel model=new MemberCentralModel();
				model.setKey(rst.getString("member_key"));
				model.setDeeplink_url(rst.getString("deeplink_url"));
				model.setIcon(rst.getString("icon"));
				model.setSelected_icon(rst.getString("selected_icon"));
				model.setSort_order_id(rst.getString("sort_id"));
				model.setType(rst.getString("type"));
				model.setValue(rst.getString("member_value"));
				membercentralList.add(model);
			}
			HashMap<String, Object> otherAssets=new HashMap<String, Object>();
			otherAssets.put("backgroundImage", "");
			otherAssets.put("backgroundColor", "white");
			otherAssets.put("bannerColor", "aqua");
			otherAssets.put("bannerImage", "");
			HashMap<String, Object> response=new HashMap<String, Object>();
			response.put("membercentralList", membercentralList);
			response.put("otherAssets", otherAssets);
			return response;
		} catch (Exception e) {
			logger.error("::Exception in getMemberCentralListNonSTM::" +e);
			e.printStackTrace();
			errorCode="500";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);
		}
	}

}
