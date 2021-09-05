package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {

	private String menu_id = "";
	private String key = "";
	private String value = "";
	private String menu_view_type = "";
	private String icon = "";
	private String selected_icon = "";
	private String deeplink_url = "";
	private String sort_order_id = "";
	private String updatedTime = "";
	private String startTime = "";
	private String endTime = "";
	@JsonInclude(Include.NON_EMPTY)
	private List<SubMenu> subMenuList = null;
	
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMenu_view_type() {
		return menu_view_type;
	}
	public void setMenu_view_type(String menu_view_type) {
		this.menu_view_type = menu_view_type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSelected_icon() {
		return selected_icon;
	}
	public void setSelected_icon(String selected_icon) {
		this.selected_icon = selected_icon;
	}
	public String getDeeplink_url() {
		return deeplink_url;
	}
	public void setDeeplink_url(String deeplink_url) {
		this.deeplink_url = deeplink_url;
	}
	public String getSort_order_id() {
		return sort_order_id;
	}
	public void setSort_order_id(String sort_order_id) {
		this.sort_order_id = sort_order_id;
	}
	public List<SubMenu> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<SubMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
