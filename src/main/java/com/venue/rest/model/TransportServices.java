package com.venue.rest.model;

public class TransportServices {
	
	private int service_type_id = 0;
	private String service_type = "";
	private String service_type_image_url = "";
	private String deeplink = "";
	private String weburl_ios = "";
	private String weburl_android = "";
	
	public int getService_type_id() {
		return service_type_id;
	}
	public void setService_type_id(int service_type_id) {
		this.service_type_id = service_type_id;
	}
	public String getService_type() {
		return service_type;
	}
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
	public String getService_type_image_url() {
		return service_type_image_url;
	}
	public void setService_type_image_url(String service_type_image_url) {
		this.service_type_image_url = service_type_image_url;
	}
	public String getDeeplink() {
		return deeplink;
	}
	public void setDeeplink(String deeplink) {
		this.deeplink = deeplink;
	}
	public String getWeburl_ios() {
		return weburl_ios;
	}
	public void setWeburl_ios(String weburl_ios) {
		this.weburl_ios = weburl_ios;
	}
	public String getWeburl_android() {
		return weburl_android;
	}
	public void setWeburl_android(String weburl_android) {
		this.weburl_android = weburl_android;
	}
}
