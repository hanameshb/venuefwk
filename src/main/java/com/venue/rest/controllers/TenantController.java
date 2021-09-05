package com.venue.rest.controllers;

import com.venue.rest.exceptions.TenantAccountConfigNotFoundException;
import com.venue.rest.model.MobileOrderingConfig;
import com.venue.rest.model.TenantAccountConfigMaster;
import com.venue.rest.model.TenantAccountConfigMasterDTO;
import com.venue.rest.services.TenantService;
import com.venue.rest.util.Utility;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{accountApiKey}/platform")
public class TenantController {

    private static Logger logger = Logger.getLogger(TenantController.class);

    private final TenantService tenantService;

    public TenantController(final TenantService tenantService) {
        this.tenantService = tenantService;
    }

	@GetMapping("/tenants/{tenantAccountId}/v1")
	public ResponseEntity<TenantAccountConfigMasterDTO> getTenantById(@PathVariable final String accountApiKey,
	                                                                  @PathVariable final long tenantAccountId) {
	
	    logger.info(":: getting tenant account config by id and account api key ::");
	
	    TenantAccountConfigMasterDTO tenantAccountConfigMasterDTO;
	    try {
	        tenantAccountConfigMasterDTO = tenantService.getTenantAccountConfigById(accountApiKey, tenantAccountId);
	    } catch (TenantAccountConfigNotFoundException e) {
	        logger.error(":: Tenant Account Config not found. ::", e);
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(tenantAccountConfigMasterDTO);
	
	}    

	/*
	 * VB-632
	 * Getting tenant account list
	 * GET /{accountApiKey}/platform/tenants/
	 */
	@GetMapping("/tenants")
	public Object getTenantsList(@PathVariable final String accountApiKey) {
		try {
			//@RequestHeader(value="x-api-key") String xApiKey == to get the value of header
			logger.info(":: TenantController :: getTenantsList() ::");
			return tenantService.getTenantsList(accountApiKey);
		} catch(Exception e) {
			logger.error(":: TenantController :: getTenantsList() :: ", e);
			return Utility.errorMessage("500", "There is a problem getting data from Tenant Service");
		}
	}
	  
	/*
	 * VB-625
	 * Tenant Account Management
	 * PUT /{accountApiKey}/platform/tenants/{tenantAccountId}/v1
	 */
	@PutMapping("/tenants/{tenantAccountId}/v1")
	public Object updateTenantAccountDetails(@RequestBody TenantAccountConfigMaster tenantAccountConfigMasterRequest) {
		try {
			logger.info(":: TenantController :: updateTenantAccountDetails() ::");
			return tenantService.updateTenantAccountDetails(tenantAccountConfigMasterRequest);
		} catch(Exception e) {
			logger.error(":: TenantController :: updateTenantAccountDetails() :: ", e);
			return Utility.errorMessage("500", "There is a problem getting data from Tenant Service");
		}
	}
	  
	/*
	 * VB-626
	 * Mobile Ordering Management
	 * PUT /{accountApiKey}/platform/tenants/{tenantAccountId}/mobileorderingconfig/v1
	 */
	@PutMapping("/tenants/{tenantAccountId}/mobileorderingconfig/v1")
	public Object updateTenantMobileOrdering(@RequestBody String mobileOrderAndWalletConfigRequest,@PathVariable final long tenantAccountId) {
		try {
			logger.info(":: TenantController :: updateTenantMobileOrdering() ::");
			return tenantService.updateTenantMobileOrdering(mobileOrderAndWalletConfigRequest, tenantAccountId);
		} catch(Exception e) {
			logger.error(":: TenantController :: updateTenantMobileOrdering() :: ", e);		  
			return Utility.errorMessage("500", "There is a problem getting data from Tenant Service");
		}
	}
	
	@DeleteMapping("/tenants/{tenantAccountId}/cache/v1")
	public Object clearTenantConfigCache(@PathVariable final String accountApiKey,
            @PathVariable final long tenantAccountId) {
		logger.info(":: TenantController :: clearTenantConfigCache() ::");
		return tenantService.clearTenantConfigCache(accountApiKey, tenantAccountId);
	}
}
