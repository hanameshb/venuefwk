package com.venue.rest.dao;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.venue.rest.model.HubMenuModelV3;
import com.venue.rest.model.HubSubMenuModelV3;
import com.venue.rest.util.ErrorMessage;
@Repository
public class HubMenuDAOV3 {


	private static Logger logger = Logger.getLogger(HubMenuDAOV3.class);
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
	public Object getMenuListV3(String appUserId,String appId,String venueId) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuListV3 appUserId"+appUserId);
		logger.info(":::inside getMenuListV3 appId"+appId);
		logger.info(":::inside getMenuListV3 venueId"+venueId);
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
		ArrayList<HubMenuModelV3> mainMenuList=new ArrayList<HubMenuModelV3>();
		ArrayList<HubSubMenuModelV3> subMenuList=null;
		SqlRowSet rst=null,rst1=null;
		try{
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list where venue_id=? and app_id=? and menu_status='publish' order by sort_order_id",
					new Object[]{venueId,appId});
			while(rst.next()){
				HubMenuModelV3 menuModel=new HubMenuModelV3();
				menuModel.setKey(rst.getString("menu_key"));
				menuModel.setValue(rst.getString("value"));
				menuModel.setIcon(rst.getString("icon"));
				menuModel.setSelected_icon(rst.getString("selected_icon"));
				menuModel.setDeeplink_url(rst.getString("deeplink_url"));
				menuModel.setSort_order_id(rst.getString("sort_order_id"));

				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list where menu_id="+rst.getInt("menu_id")+" and sub_menu_status='publish' order by sort_order_id");
				if(rst1!=null){
					subMenuList=new ArrayList<HubSubMenuModelV3>();
				}
				while(rst1.next()){
					HubSubMenuModelV3 menuModel1=new HubSubMenuModelV3();
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

		}catch(Exception e){
			logger.info(":::Exception in getting menu list:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return mainMenuList;
	}



	public Object getMenuV3(String menuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuV3:::");
		ArrayList<HubSubMenuModelV3> subMenuList=null;
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV3 menuModel=new HubMenuModelV3();
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
					subMenuList=new ArrayList<HubSubMenuModelV3>();
				}
				while(rst1.next()){
					HubSubMenuModelV3 menuModel1=new HubSubMenuModelV3();
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



	public Object deleteMenuV3(String menuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV3:"+menuItemkey);

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



	public Object getSubMenuV3(String menuItemkey,String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getSubMenuV3:::");
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV3 menuModel1=new HubMenuModelV3();
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



	public Boolean deleteSubMenuV3(String menuItemkey, String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside deleteSubMenuV3:::");
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
			logger.info(":::Exception in deleteSubMenuV3:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}



	public Object deleteSubMenu(String menuItemkey, String subMenuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV3:"+menuItemkey);

        	Boolean flag = deleteSubMenuV3(menuItemkey,subMenuItemkey);
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



	public Object updateMenuV3(String menuItemkey, HubMenuModelV3 menu) {
        try
        {
    		logger.info("::in updateMenuV3:"+menuItemkey);

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



	private Boolean updateMenu(String menuItemkey, HubMenuModelV3 menu) {
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
						for(HubSubMenuModelV3 subMenu:menu.getSubMenuList()){
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



	public Object updateSubMenu(String menuItemkey, String subMenuItemkey, HubMenuModelV3 subMenu) {
        try
        {
    		logger.info("::in deleteMenuV3:"+menuItemkey);

        	Boolean flag = updateSubMenuV3(menuItemkey,subMenuItemkey,subMenu);
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



	private Boolean updateSubMenuV3(String menuItemkey, String subMenuItemkey,
			HubMenuModelV3 subMenu) {
		// TODO Auto-generated method stub
		logger.info(":::inside updateSubMenuV3:::");
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
			logger.info(":::Exception in updateSubMenuV3:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}




}
