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

import com.venue.rest.model.HubMenuModelV21;
import com.venue.rest.util.ErrorMessage;
@Repository
public class HubMenuDAOV21 {


	private static Logger logger = Logger.getLogger(HubMenuDAOV21.class);
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
	public Object getMenuList(String appUserId) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuListV2"+appUserId);
		if(appUserId!=null && !appUserId.equalsIgnoreCase("")){
			Object menuList=getMenuListResponce(appUserId);
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
	private Object getMenuListResponce(String appUserId) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMentListResponce:::"+appUserId);
		ArrayList<HubMenuModelV21> mainMenuList=new ArrayList<HubMenuModelV21>();
		ArrayList<HubMenuModelV21> subMenuList=null;
		SqlRowSet rst=null,rst1=null,rst2=null;
		
		try{
			//rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list_v21 order by sort_order_id");
			rst2=jdbcTemplateObject.queryForRowSet("select property_value from tbl_user_detail where app_user_id=? and property_name=?", new Object[]{appUserId, "Membership Location Tier"});
			if(rst2.next()){
				logger.info("::User have Membership Location Tier values::");
				if((rst2.getString("property_value").equalsIgnoreCase("BLACK CARD")) || (rst2.getString("property_value").equalsIgnoreCase("BLACK CARD NINE")) || (rst2.getString("property_value").equalsIgnoreCase("CLUB")) || (rst2.getString("property_value").equalsIgnoreCase("SUITE")) || (rst2.getString("property_value").equalsIgnoreCase("BLACK CARD EMC")))
				{
					logger.info("::inside if property value::" +rst2.getString("property_value"));
					rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list_v21 order by sort_order_id");
				}
			} else{
				logger.info("::User doesn't have Membership Location Tier values::");
				rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list_v21 where menu_key not like '%miride' order by sort_order_id");
			}
			while(rst.next()){
				HubMenuModelV21 menuModel=new HubMenuModelV21();
				menuModel.setKey(rst.getString("menu_key"));
				menuModel.setValue(rst.getString("value"));
				menuModel.setIcon(rst.getString("icon"));
				menuModel.setSelected_icon(rst.getString("selected_icon"));
				menuModel.setDeeplink_url(rst.getString("deeplink_url"));
				menuModel.setSort_order_id(rst.getString("sort_order_id"));
				menuModel.setType(rst.getString("type"));
				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list_v21 where menu_id="+rst.getInt("menu_id")+" order by sort_order_id");
				if(rst1!=null){
					subMenuList=new ArrayList<HubMenuModelV21>();
				}
				while(rst1.next()){
					HubMenuModelV21 menuModel1=new HubMenuModelV21();
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
					menuModel1.setType(rst1.getString("type"));
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
	
	public Object getMenu(String menuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getMenuV2:::");
		ArrayList<HubMenuModelV21> subMenuList=null;
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV21 menuModel=new HubMenuModelV21();
		try{
			rst=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				menuModel.setKey(rst.getString("menu_key"));
				menuModel.setValue(rst.getString("value"));
				menuModel.setIcon(rst.getString("icon"));
				menuModel.setSelected_icon(rst.getString("selected_icon"));
				menuModel.setDeeplink_url(rst.getString("deeplink_url"));
				menuModel.setSort_order_id(rst.getString("sort_order_id"));
				menuModel.setType(rst.getString("type"));
				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list_v21 where menu_id="+rst.getInt("menu_id"));
				if(rst1!=null){
					subMenuList=new ArrayList<HubMenuModelV21>();
				}
				while(rst1.next()){
					HubMenuModelV21 menuModel1=new HubMenuModelV21();
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
					menuModel1.setType(rst1.getString("type"));
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



	public Object deleteMenuV21(String menuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV2:"+menuItemkey);

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
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
			if(rst.next())
				menuId=rst.getInt("menu_id");
			i=jdbcTemplateObject.update("delete from tbl_hub_menu_list_v21 where menu_id="+menuId);
			if(i>0){
				i=jdbcTemplateObject.update("delete from tbl_hub_sub_menu_list_v21 where menu_id="+menuId);
				flag=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Exception in delete menu:::"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return flag;
	}



	public Object getSubMenu(String menuItemkey,String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside getSubMenuV2:::");
		SqlRowSet rst=null,rst1=null;
		HubMenuModelV21 menuModel1=new HubMenuModelV21();
		try{
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				rst1=jdbcTemplateObject.queryForRowSet("select * from tbl_hub_sub_menu_list_v21 where menu_id="+rst.getInt("menu_id")+" and menu_key='"+subMenuItemkey+"'");
				if(rst1.next()){
					menuModel1.setKey(rst1.getString("menu_key"));
					menuModel1.setValue(rst1.getString("value"));
					menuModel1.setIcon(rst1.getString("icon"));
					menuModel1.setSelected_icon(rst1.getString("selected_icon"));
					menuModel1.setDeeplink_url(rst1.getString("deeplink_url"));
					menuModel1.setSort_order_id(rst1.getString("sort_order_id"));
					menuModel1.setType(rst1.getString("type"));
				}
			}

		}catch(Exception e){
			logger.info(":::Exception in getting SubMenu:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return menuModel1;
	}



	public Boolean deleteSubMenuV21(String menuItemkey, String subMenuItemkey) {
		// TODO Auto-generated method stub
		logger.info(":::inside deleteSubMenuV2:::");
		SqlRowSet rst=null;
		Boolean flag=false;
		int i=0;
		try{
			rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
			if(rst.next()){
				i=jdbcTemplateObject.update("delete from tbl_hub_sub_menu_list_v21 where menu_id="+rst.getInt("menu_id")+" and menu_key='"+subMenuItemkey+"'");
				if(i>0)
					flag=true;
			}

		}catch(Exception e){
			logger.info(":::Exception in deleteSubMenuV2:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}



	public Object deleteSubMenu(String menuItemkey, String subMenuItemkey) {
        try
        {
    		logger.info("::in deleteMenuV2:"+menuItemkey);

        	Boolean flag = deleteSubMenuV21(menuItemkey,subMenuItemkey);
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



	public Object updateMenuV2(String menuItemkey, HubMenuModelV21 menu) {
        try
        {
    		logger.info("::in updateMenuV2:"+menuItemkey);

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



	private Boolean updateMenu(String menuItemkey, HubMenuModelV21 menu) {
		// TODO Auto-generated method stub
		logger.info(":::inside updateMenu:::");
		SqlRowSet rst=null;
		int i=0;
		Boolean flag=false;
		try{
				if(menu!=null){
				i=jdbcTemplateObject.update("update tbl_hub_menu_list_v21 set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
						+ "where menu_key=?",new Object[]{menu.getValue(),menu.getIcon(),menu.getSelected_icon(),menu.getDeeplink_url()
								,menu.getSort_order_id(),menuItemkey});
				if(menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0){
					rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
					if(rst.next()){
						for(HubMenuModelV21 subMenu:menu.getSubMenuList()){
							i=jdbcTemplateObject.update("update tbl_hub_sub_menu_list_v21 set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
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



	public Object updateSubMenu(String menuItemkey, String subMenuItemkey, HubMenuModelV21 subMenu) {
        try
        {
    		logger.info("::in deleteMenuV2:"+menuItemkey);

        	Boolean flag = updateSubMenuV21(menuItemkey,subMenuItemkey,subMenu);
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



	private Boolean updateSubMenuV21(String menuItemkey, String subMenuItemkey,
			HubMenuModelV21 subMenu) {
		// TODO Auto-generated method stub
		logger.info(":::inside updateSubMenuV2:::");
		SqlRowSet rst=null;
		int i=0;
		Boolean flag=false;
		try{
				if(subMenu!=null){
					rst=jdbcTemplateObject.queryForRowSet("select menu_id from tbl_hub_menu_list_v21 where menu_key='"+menuItemkey+"'");
					if(rst.next()){
							i=jdbcTemplateObject.update("update tbl_hub_sub_menu_list_v21 set value=?,icon=?,selected_icon=?,deeplink_url=?,sort_order_id=? "
									+ "where menu_id=? and menu_key=?",new Object[]{subMenu.getValue(),subMenu.getIcon(),subMenu.getSelected_icon(),subMenu.getDeeplink_url()
											,subMenu.getSort_order_id(),rst.getInt("menu_id"),subMenuItemkey});
						}

				if(i>0)
					flag=true;
				}
		}catch(Exception e){
			logger.info(":::Exception in updateSubMenuV2:::"+e.getLocalizedMessage());
			e.printStackTrace();

		}
		return flag;
	}




}
