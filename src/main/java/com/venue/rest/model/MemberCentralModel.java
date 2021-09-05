package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberCentralModel {

	private String key="";
	private String value="";
	private String icon="";
	private String selected_icon="";
	private String deeplink_url="";
	private String type="";
	private String sort_order_id="";
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSort_order_id() {
		return sort_order_id;
	}
	public void setSort_order_id(String sort_order_id) {
		this.sort_order_id = sort_order_id;
	}


}
