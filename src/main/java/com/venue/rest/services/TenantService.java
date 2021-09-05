package com.venue.rest.services;

import com.venue.rest.exceptions.TenantAccountConfigNotFoundException;
import com.venue.rest.model.MobileOrderingConfig;
import com.venue.rest.model.TenantAccountConfigMaster;
import com.venue.rest.model.TenantAccountConfigMasterDTO;

public interface TenantService {

    TenantAccountConfigMasterDTO getTenantAccountConfigById(final String accountApiKey, final long tenantAccountId)
            throws TenantAccountConfigNotFoundException;

    Object getTenantsList(final String accountApiKey);
    
    Object updateTenantAccountDetails(TenantAccountConfigMaster req);

    Object updateTenantMobileOrdering(String mobileOrderAndWalletConfigRequest, final long tenantAccountId);
    
    Object clearTenantConfigCache(final String accountApiKey, final long tenantAccountId);
}
