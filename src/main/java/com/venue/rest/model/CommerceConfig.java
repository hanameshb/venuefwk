package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class CommerceConfig {

	private LoadedValueOffsetConfig loadedValueOffsetConfig;

	public LoadedValueOffsetConfig getLoadedValueOffsetConfig() {
		return loadedValueOffsetConfig;
	}

	public void setLoadedValueOffsetConfig(LoadedValueOffsetConfig loadedValueOffsetConfig) {
		this.loadedValueOffsetConfig = loadedValueOffsetConfig;
	}
	
	
}
