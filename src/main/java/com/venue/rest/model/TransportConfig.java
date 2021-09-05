package com.venue.rest.model;

import java.util.ArrayList;

public class TransportConfig {
	
	private DestinationAddress destinationAddress = null;
	private ArrayList<TransportServices> transportServices = null;
	
	public DestinationAddress getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(DestinationAddress destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public ArrayList<TransportServices> getTransportServices() {
		return transportServices;
	}
	public void setTransportServices(ArrayList<TransportServices> transportServices) {
		this.transportServices = transportServices;
	}
}
