package com.venue.rest.services;

import com.venue.rest.converters.TenantAccountConfigMasterToTenantAccountConfigMasterDTOConverter;
import com.venue.rest.dao.TenantAccountConfigMasterRepository;
import com.venue.rest.exceptions.TenantAccountConfigNotFoundException;
import com.venue.rest.model.MobileOrderingConfig;
import com.venue.rest.model.TenantAccountConfigMaster;
import com.venue.rest.model.TenantAccountConfigMasterDTO;

import com.venue.rest.util.Utility;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceDefault implements TenantService {

    private static Logger logger = Logger.getLogger(TenantServiceDefault.class);

    private final TenantAccountConfigMasterRepository tenantAccountConfigMasterRepository;
    private final TenantAccountConfigMasterToTenantAccountConfigMasterDTOConverter tenantAccountConfigMasterConverter;

    public TenantServiceDefault(final TenantAccountConfigMasterRepository tenantAccountConfigMasterRepository,
                                final TenantAccountConfigMasterToTenantAccountConfigMasterDTOConverter tenantAccountConfigMasterConverter) {
        this.tenantAccountConfigMasterRepository = tenantAccountConfigMasterRepository;
        this.tenantAccountConfigMasterConverter = tenantAccountConfigMasterConverter;
    }

    @Override
    @Cacheable(value = "TenantConfigCache",key = "#tenantAccountId")
    public TenantAccountConfigMasterDTO getTenantAccountConfigById(final String accountApiKey,
                                                                   final long tenantAccountId)
            throws TenantAccountConfigNotFoundException {

        logger.info(":: in getTenantAccountConfigById ::");

        final TenantAccountConfigMaster tenantAccountConfigMaster =
                tenantAccountConfigMasterRepository.findTenantAccountConfigMasterById(accountApiKey, tenantAccountId)
                .orElseThrow(() -> new TenantAccountConfigNotFoundException(accountApiKey, tenantAccountId));
        return tenantAccountConfigMasterConverter.convert(tenantAccountConfigMaster);
    }
    
	/*
	 * VB-632
	 * Getting tenant account list
	 * GET /{accountApiKey}/platform/tenants/
	 */
    @Override
    public Object getTenantsList(final String accountApiKey) {    
    	try {
    		logger.info(":: TenantService :: getTenantsList()::");
        	final Object response = tenantAccountConfigMasterRepository.getTenantsList(accountApiKey);
        	return response;
    	} catch(Exception e) {
    		logger.error(":: TenantService :: getTenantsList():: ", e);
			return Utility.errorMessage("500", "There is a problem getting data from database");
    	}
    }
    
	/*
	 * VB-625
	 * Tenant Account Management
	 * PUT /{accountApiKey}/platform/tenants/{tenantAccountId}/v1
	 */
    @Override
    public Object updateTenantAccountDetails(TenantAccountConfigMaster tenantAccountConfigMasterRequest) {    	
    	try {
    		logger.info(":: TenantService :: updateTenantAccountDetails()::");
        	final Object response = tenantAccountConfigMasterRepository.updateTenantAccountDetails(tenantAccountConfigMasterRequest);
        	return response;	
    	} catch(Exception e) {
    		logger.error(":: TenantService :: updateTenantAccountDetails():: ", e);
    		return Utility.errorMessage("500", "There is a problem getting data from database");
    	}
    }
    
	/*
	 * VB-626
	 * Mobile Ordering Management
	 * PUT /{accountApiKey}/platform/tenants/{tenantAccountId}/mobileorderingconfig/v1
	 */
    @Override
    public Object updateTenantMobileOrdering(String mobileOrderAndWalletConfigRequest, final long tenantAccountId) { 
    	try {
    		logger.info(":: TenantService :: updateTenantMobileOrdering()::");
        	final Object response = tenantAccountConfigMasterRepository.updateTenantMobileOrdering(mobileOrderAndWalletConfigRequest, tenantAccountId);
        	return response;
    	} catch(Exception e) {
    		logger.error(":: TenantService :: updateTenantMobileOrdering():: ", e);
    		return Utility.errorMessage("500", "There is a problem getting data from database");
    	}
    }

	@Override
	@CacheEvict(value = "TenantConfigCache",key = "#tenantAccountId")
	public Object clearTenantConfigCache(final String accountApiKey,final long tenantAccountId) {
		logger.info("::In clearTenantConfigCache for accountApiKey::" +accountApiKey + ",tenantAccountId "+tenantAccountId);
		return "{\"responseCode\":\"200\",\"status\":\"Tenant Config Cache for accountApiKey "+ accountApiKey +" and tenantAccountId "+tenantAccountId+" cleared Successfully\"}";
	}
}
