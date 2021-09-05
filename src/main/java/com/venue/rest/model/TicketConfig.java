package com.venue.rest.model;

import java.util.ArrayList;

public class TicketConfig {
	
	private ArrayList<PriceCodeAssets> price_code_assets = null;
	private TicketsAccountConfig buyTicketsAccountManagerConfig = null;
	
	public ArrayList<PriceCodeAssets> getPrice_code_assets() {
		return price_code_assets;
	}
	public void setPrice_code_assets(ArrayList<PriceCodeAssets> price_code_assets) {
		this.price_code_assets = price_code_assets;
	}
	public TicketsAccountConfig getBuyTicketsAccountManagerConfig() {
		return buyTicketsAccountManagerConfig;
	}
	public void setBuyTicketsAccountManagerConfig(
			TicketsAccountConfig buyTicketsAccountManagerConfig) {
		this.buyTicketsAccountManagerConfig = buyTicketsAccountManagerConfig;
	}
}
