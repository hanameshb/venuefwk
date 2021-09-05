package com.venue.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.venue.rest.dao.HubMenuDAOV21;
import com.venue.rest.model.HubMenuModelV21;

@Controller
@RequestMapping("/v21")
public class HubMenuControllerV21 {

	@Autowired
	HubMenuDAOV21 hubMenuDaoV21;
	/**
	 * Controller to get hub menu list
	 */
	@RequestMapping(value="/menulist", method=RequestMethod.GET)
	public @ResponseBody Object getMenuList(@RequestParam(value="appUserId",required=false) String appUserId){
		Object responce=null;
		responce=hubMenuDaoV21.getMenuList(appUserId);
		return responce;
	}

	/**
	 * Controller to get hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.GET)
	public @ResponseBody Object getMenu(@PathVariable String menuItemkey){
		Object responce=null;
		responce=hubMenuDaoV21.getMenu(menuItemkey);
		return responce;
	}

	/**
	 * Controller to update hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.PUT)
	public @ResponseBody Object updateMenu(@PathVariable String menuItemkey,@RequestBody HubMenuModelV21 menu){
		Object responce=null;
		responce=hubMenuDaoV21.updateMenuV2(menuItemkey,menu);
		return responce;
	}

	/**
	 * Controller to delete hub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}", method=RequestMethod.DELETE)
	public @ResponseBody Object deleteMenu(@PathVariable String menuItemkey){
		Object responce=null;
		responce=hubMenuDaoV21.deleteMenuV21(menuItemkey);
		return responce;
	}

	/**
	 * Controller to get hub sub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.GET)
	public @ResponseBody Object getSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey){
		Object responce=null;
		responce=hubMenuDaoV21.getSubMenu(menuItemkey,subMenuItemkey);
		return responce;
	}

	/**
	 * Controller to delete hub sub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.DELETE)
	public @ResponseBody Object deleteSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey){
		Object responce=null;
		responce=hubMenuDaoV21.deleteSubMenu(menuItemkey,subMenuItemkey);
		return responce;
	}

	/**
	 * Controller to update hub sub menu item
	 */
	@RequestMapping(value="/menulist/{menuItemkey}/{subMenuItemkey}", method=RequestMethod.PUT)
	public @ResponseBody Object updateSubMenu(@PathVariable String menuItemkey,@PathVariable String subMenuItemkey,@RequestBody HubMenuModelV21 subMenu){
		Object responce=null;
		responce=hubMenuDaoV21.updateSubMenu(menuItemkey,subMenuItemkey,subMenu);
		return responce;
	}

}
