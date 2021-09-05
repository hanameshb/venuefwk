package com.venue.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.HubMenuDAOV3;
import com.venue.rest.model.HubMenuModelV3;

@Controller
@RequestMapping("/v3")
public class HubMenuControllerV3 {

	@Autowired
	HubMenuDAOV3 hubMenuDaoV3;
	/**
	 * Controller to get hub menu list
	 */
	@RequestMapping(value="/menulist/{appId}/{venueId}", method=RequestMethod.GET)
	public @ResponseBody Object getMenuList(@RequestParam(value="appUserId",required=false) String appUserId,@PathVariable String appId,@PathVariable String venueId){
		Object responce=null;
		responce=hubMenuDaoV3.getMenuListV3(appUserId,appId,venueId);
		return responce;
	}

	/**
	 * Controller to get hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.GET)
	public @ResponseBody Object getMenu(@PathVariable String menuItemkey){
		Object responce=null;
		responce=hubMenuDaoV3.getMenuV3(menuItemkey);
		return responce;
	}

	/**
	 * Controller to update hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.PUT)
	public @ResponseBody Object updateMenu(@PathVariable String menuItemkey,@RequestBody HubMenuModelV3 menu){
		Object responce=null;
		responce=hubMenuDaoV3.updateMenuV3(menuItemkey,menu);
		return responce;
	}

	/**
	 * Controller to delete hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.DELETE)
	public @ResponseBody Object deleteMenu(@PathVariable String menuItemkey){
		Object responce=null;
		responce=hubMenuDaoV3.deleteMenuV3(menuItemkey);
		return responce;
	}

	/**
	 * Controller to get hub sub menu item
	 */
	/*@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.GET)
	public @ResponseBody Object getSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey){
		Object responce=null;
		HubMenuDAOV3 dao=HubMenuDAOV3.getInstance();
		responce=dao.getSubMenuV3(menuItemkey,subMenuItemkey);
		return responce;
	}*/

	/**
	 * Controller to delete hub sub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.DELETE)
	public @ResponseBody Object deleteSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey){
		Object responce=null;
		responce=hubMenuDaoV3.deleteSubMenu(menuItemkey,subMenuItemkey);
		return responce;
	}

	/**
	 * Controller to update hub sub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.PUT)
	public @ResponseBody Object updateSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey,@RequestBody HubMenuModelV3 subMenu){
		Object responce=null;
		responce=hubMenuDaoV3.updateSubMenu(menuItemkey,subMenuItemkey,subMenu);
		return responce;
	}

}
