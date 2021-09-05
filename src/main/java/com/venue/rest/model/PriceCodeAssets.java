package com.venue.rest.model;

public class PriceCodeAssets {
	
	private String parent_price_code = "";
	private String price_code_verbose_description = "";
	private String price_code_image_url = "";
	
	public String getParent_price_code() {
		return parent_price_code;
	}
	public void setParent_price_code(String parent_price_code) {
		this.parent_price_code = parent_price_code;
	}
	public String getPrice_code_verbose_description() {
		return price_code_verbose_description;
	}
	public void setPrice_code_verbose_description(
			String price_code_verbose_description) {
		this.price_code_verbose_description = price_code_verbose_description;
	}
	public String getPrice_code_image_url() {
		return price_code_image_url;
	}
	public void setPrice_code_image_url(String price_code_image_url) {
		this.price_code_image_url = price_code_image_url;
	}
}
