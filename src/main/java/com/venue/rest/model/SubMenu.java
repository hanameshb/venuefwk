package com.venue.rest.model;

import java.util.Objects;

public class SubMenu {
	
	private String sub_menu_id = "";
	private String key = "";
	private String value = "";
	private String icon = "";
	private String selected_icon = "";
	private String deeplink_url = "";
	private String sort_order_id = "";
	private String web_url = "";
	private String signup_required = "";
	private String icon_2x = "";
	private String selected_icon_2x = "";
	private String width = "";
	private String height = "";
	private int placeSegmentId;
	private String placeSegmentName;
	private String startTime = "";
	private String endTime = "";
	
	public String getSub_menu_id() {
		return sub_menu_id;
	}
	public void setSub_menu_id(String sub_menu_id) {
		this.sub_menu_id = sub_menu_id;
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
	public String getWeb_url() {
		return web_url;
	}
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}
	public String getSignup_required() {
		return signup_required;
	}
	public void setSignup_required(String signup_required) {
		this.signup_required = signup_required;
	}
	public String getIcon_2x() {
		return icon_2x;
	}
	public void setIcon_2x(String icon_2x) {
		this.icon_2x = icon_2x;
	}
	public String getSelected_icon_2x() {
		return selected_icon_2x;
	}
	public void setSelected_icon_2x(String selected_icon_2x) {
		this.selected_icon_2x = selected_icon_2x;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}

	public int getPlaceSegmentId() {
		return placeSegmentId;
	}

	public void setPlaceSegmentId(final int placeSegmentId) {
		this.placeSegmentId = placeSegmentId;
	}

	public String getPlaceSegmentName() {
		return placeSegmentName;
	}

	public void setPlaceSegmentName(final String placeSegmentName) {
		this.placeSegmentName = placeSegmentName;
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
	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final SubMenu subMenu = (SubMenu) o;
		return sub_menu_id.equals(subMenu.sub_menu_id) &&
				key.equals(subMenu.key) &&
				value.equals(subMenu.value) &&
				Objects.equals(icon, subMenu.icon) &&
				Objects.equals(selected_icon, subMenu.selected_icon) &&
				Objects.equals(deeplink_url, subMenu.deeplink_url) &&
				Objects.equals(sort_order_id, subMenu.sort_order_id) &&
				Objects.equals(web_url, subMenu.web_url) &&
				Objects.equals(signup_required, subMenu.signup_required) &&
				Objects.equals(icon_2x, subMenu.icon_2x) &&
				Objects.equals(selected_icon_2x, subMenu.selected_icon_2x) &&
				Objects.equals(width, subMenu.width) &&
				Objects.equals(height, subMenu.height) &&
				Objects.equals(placeSegmentId, subMenu.placeSegmentId) &&
				Objects.equals(placeSegmentName, subMenu.placeSegmentName) &&
				Objects.equals(startTime, subMenu.startTime) &&
				Objects.equals(endTime, subMenu.endTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sub_menu_id, key, value, icon, selected_icon, deeplink_url, sort_order_id, web_url,
				signup_required, icon_2x, selected_icon_2x, width, height, placeSegmentId, placeSegmentName);
	}
}
