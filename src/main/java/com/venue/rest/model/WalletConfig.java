package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletConfig {

	private String discount_card_type = "";
	private String discount_percentage = "0";
	private String copy_text = "";
	private String merchant_id = "";
	public String getDiscount_card_type() {
		return discount_card_type;
	}
	public void setDiscount_card_type(String discount_card_type) {
		this.discount_card_type = discount_card_type;
	}
	public String getDiscount_percentage() {
		return discount_percentage;
	}
	public void setDiscount_percentage(String discount_percentage) {
		this.discount_percentage = discount_percentage;
	}
	public String getCopy_text() {
		return copy_text;
	}
	public void setCopy_text(String copy_text) {
		this.copy_text = copy_text;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	
}

