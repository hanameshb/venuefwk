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

import com.venue.rest.model.HubMenuModelV4;
import com.venue.rest.model.HubSubMenuModelV4;
import com.venue.rest.util.ErrorMessage;
@Repository
public class HubMenuDAOV4 {


	private static Logger logger = Logger.getLogger(HubMenuDAOV4.class);
	String errorCode = "";
	private JdbcTemplate jdbcTemplateObject = null;

	@Autowired
	@Qualifier("dataSourceVenue")
	DataSource dataSourceVenue;
	
	
	@PostConstruct
    public void init() {
		jdbcTemplateObject = new JdbcTemplate(dataSourceVenue);	
    	
    }

	/**
	 * Method to get Hub Menu List
	 */
	public Object getMenuListV4(String appUserId,String appId,String venueId) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuListV4 appUserId"+appUserId);
		logger.info(":::inside getMenuListV4 appId"+appId);
		logger.info(":::inside getMenuListV4 venueId"+venueId);
		if(appUserId!=null && !appUserId.equalsIgnoreCase("")){
			Object menuList=getMenuListResponce(appUserId,appId,venueId);
			if(menuList!=null){
				return menuList;
			}else{
				errorCode="500";
				return ErrorMessage.getInstance().getErrorResponse(errorCode);
			}
		}else{
			errorCode="1002";
			return ErrorMessage.getInstance().getErrorResponse(errorCode);

		}
	}


	/**
	 * Method to get Hub Menu List DB
	 */
	private Object getMenuListResponce(String appUserId,String appId,String venueId) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMentListResponce:::"+appUserId);
		logger.info(":::inside getMentListResponce appId"+appId);
		logger.info(":::inside getMentListResponce venueId"+venueId);
		ArrayList<HubMenuModelV4> mainMenuList=new ArrayList<HubMenuModelV4>();
		ArrayList<HubSubMenuModelV4> subMenuList=null;
		HashMap<String, Object> menuList=new HashMap<String, Object>();
		String lastUpdateTime="";
 		SqlRowSet rst=null,rst1=null; 
		try{
			rst=jdbcTemplateObject.queryForRowSet("select updated_time from tbl_hub_menu_last_updated where venue_id=? and app_id=?",
					new Object[]{venueId,appId});
			while(rst.next()){
				lastUpdateTime=rst.getString("updated_time");
				logger.info(":::lastUpdateTime"+lastUpdateTime);
			}
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list where venue_id=? and app_id=? and menu_status='publish' order by sort_order_id",
					new Object[]{venueId,appId});
			while(rst.next()){
				HubMenuModelV4 menuModel=new HubMenuModelV4();
				menuModel.setKey(rst.getString("menu_key"));
				menuModel.setValue(rst.getString("value"));
				menuModel.setIcon(rst.getString("icon"));
				menuModel.setSelected_icon(rst.getString("selected_icon"));
				menuModel.setDeeplink_url(rst.getString("deeplink_url"));
				menuModel.setSort_order_id(rst.getString("sort_order_id"));

				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id")+" and sub_menu_status='publish' order by sort_order_id");
				if(rst1!=null){
					subMenuList=new ArrayList<HubSubMenuModelV4>();
				}
				while(rst1.next()){
					HubSubMenuModelV4 menuModel1=new HubSubMenuModelV4();
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
					menuModel1.setWeb_url(rst1.getString("web_url"));
					menuModel1.setSignup_required(rst1.getString("signup_required"));
					menuModel1.setIcon_2x(rst1.getString("icon_2x"));
					menuModel1.setSelected_icon_2x(rst1.getString("selected_icon_2x"));
					subMenuList.add(menuModel1);
				}
				if(subMenuList!=null){
					menuModel.setSubMenuList(subMenuList);
				}
				mainMenuList.add(menuModel);
			}
			menuList.put("lastupdatedtime", lastUpdateTime);
			menuList.put("menulist", mainMenuList);
		}catch(Exception e){
			logger.info(":::Exception in getting menu list:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return menuList;
	}



	public Object getMenuV4(String menuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuV4:::");
		ArrayList<HubSubMenuModelV4> subMenuList=null;
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV4 menuModel=new HubMenuModelV4();
		try{
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				menuModel.setKey(rst.getString("menu_key"));
				menuModel.setValue(rst.getString("value"));
				menuModel.setIcon(rst.getString("icon"));
				menuModel.setSelected_icon(rst.getString("selected_icon"));
				menuModel.setDeeplink_url(rst.getString("deeplink_url"));
				menuModel.setSort_order_id(rst.getString("sort_order_id"));
				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id"));
				if(rst1!=null){
					subMenuList=new ArrayList<HubSubMenuModelV4>();
				}
				while(rst1.next()){
					HubSubMenuModelV4 menuModel1=new HubSubMenuModelV4();
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
					subMenuList.add(menuModel1);
				}
				if(subMenuList!=null){
					menuModel.setSubMenuList(subMenuList);
				}
			}

		}catch(Exception e){
			logger.info(":::Exception in getting menu:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return menuModel;
	}



	public Object deleteMenuV4(String menuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV4:"+menuItemkey);

        	Boolean flag = deleteMenu(menuItemkey);
        	if(flag)
            {
            	return  "{\"responseCode\":\"200\",\"status\":\"Success\"}";
            }else
            {
            	errorCode="500";
            	return  ErrorMessage.getInstance().getErrorResponse(errorCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }



	private Boolean deleteMenu(String menuItemkey) {
		// TODO Auto-generated method stub

		Boolean flag=false;
		int i=0;
		SqlRowSet rst=null;
		int menuId=0;
		try {
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
			if(rst.next())
				menuId=rst.getInt("menu_id");
			i=jdbcTemplateObject.update("delete from tbl_hub_menu_list where menu_id="+menuId);
			if(i>0){
				i=jdbcTemplateObject.update("delete from tbl_hub_sub_menu_list where menu_id="+menuId);
				flag=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Exception in delete menu:::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return flag;
	}



	public Object getSubMenuV4(String menuItemkey,String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getSubMenuV4:::");
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV4 menuModel1=new HubMenuModelV4();
		try{
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id")+" and menu_key='"+subMenuItemkey+"'");
				if(rst1.next()){
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
				}
			}

		}catch(Exception e){
			logger.info(":::Exception in getting SubMenu:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return menuModel1;
	}



	public Boolean deleteSubMenuV4(String menuItemkey, String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside deleteSubMenuV4:::");
		SqlRowSet rst=null;
		Boolean flag=false;
		int i=0;
		try{
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				i=jdbcTemplateObject.update("delete from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id")+" and menu_key='"+subMenuItemkey+"'");
				if(i>0)
					flag=true;
			}

		}catch(Exception e){
			logger.info(":::Exception in deleteSubMenuV4:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}



	public Object deleteSubMenu(String menuItemkey, String subMenuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV4:"+menuItemkey);

        	Boolean flag = deleteSubMenuV4(menuItemkey,subMenuItemkey);
        	if(flag)
            {
            	return  "{\"responseCode\":\"200\",\"status\":\"Success\"}";
            }else
            {
            	errorCode="500";
            	return  ErrorMessage.getInstance().getErrorResponse(errorCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }



	public Object updateMenuV4(String menuItemkey, HubMenuModelV4 menu) {
        try
        {
    		logger.info("::in updateMenuV4:"+menuItemkey);

        	Boolean flag = updateMenu(menuItemkey,menu);
        	if(flag)
            {
            	return  "{\"responseCode\":\"200\",\"status\":\"Success\"}";
            }else
            {
            	errorCode="500";
            	return  ErrorMessage.getInstance().getErrorResponse(errorCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }



	private Boolean updateMenu(String menuItemkey, HubMenuModelV4 menu) {
		// TODO Auto-generated method stub
		logger.info(":::inside updateMenu:::");
		SqlRowSet rst=null;
		int i=0;
		Boolean flag=false;
		try{
				if(menu!=null){
				i=jdbcTemplateObject.update("update tbl_hub_menu_list set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
						+ "where menu_key=?",new Object[]{menu.getValue(),menu.getIcon(),menu.getSelected_icon(),menu.getDeeplink_url()
								,menu.getSort_order_id(),menuItemkey});
				if(menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0){
					rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
					if(rst.next()){
						for(HubSubMenuModelV4 subMenu:menu.getSubMenuList()){
							i=jdbcTemplateObject.update("update tbl_hub_sub_menu_list set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
									+ "where menu_id=? and menu_key=?",new Object[]{subMenu.getValue(),subMenu.getIcon(),subMenu.getSelected_icon(),subMenu.getDeeplink_url()
											,subMenu.getSort_order_id(),rst.getInt("menu_id"),subMenu.getKey()});
						}
					}
				}
				if(i>0)
					flag=true;
				}
		}catch(Exception e){
			logger.info(":::Exception in updateMenu:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}



	public Object updateSubMenu(String menuItemkey, String subMenuItemkey, HubMenuModelV4 subMenu) {
        try
        {
    		logger.info("::in deleteMenuV4:"+menuItemkey);

        	Boolean flag = updateSubMenuV4(menuItemkey,subMenuItemkey,subMenu);
        	if(flag)
            {
            	return  "{\"responseCode\":\"200\",\"status\":\"Success\"}";
            }else
            {
            	errorCode="500";
            	return  ErrorMessage.getInstance().getErrorResponse(errorCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
    }



	private Boolean updateSubMenuV4(String menuItemkey, String subMenuItemkey,
			HubMenuModelV4 subMenu) {
		// TODO Auto-generated method stub
		logger.info(":::inside updateSubMenuV4:::");
		SqlRowSet rst=null;
		int i=0;
		Boolean flag=false;
		try{
				if(subMenu!=null){
					rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list where menu_key='"+menuItemkey+"'");
					if(rst.next()){
							i=jdbcTemplateObject.update("update tbl_hub_sub_menu_list set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
									+ "where menu_id=? and menu_key=?",new Object[]{subMenu.getValue(),subMenu.getIcon(),subMenu.getSelected_icon(),subMenu.getDeeplink_url()
											,subMenu.getSort_order_id(),rst.getInt("menu_id"),subMenuItemkey});
						}

				if(i>0)
					flag=true;
				}
		}catch(Exception e){
			logger.info(":::Exception in updateSubMenuV4:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}




}
