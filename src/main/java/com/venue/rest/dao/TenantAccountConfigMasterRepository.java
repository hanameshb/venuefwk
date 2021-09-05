package com.venue.rest.dao;

import com.venue.rest.model.MobileOrderingConfig;
import com.venue.rest.model.TenantAccountConfigMaster;

import java.util.Optional;

public interface TenantAccountConfigMasterRepository {

    Optional<TenantAccountConfigMaster> findTenantAccountConfigMasterById(final String accountApiKey, final long tenantAccountID);

    Object getTenantsList(final String accountApiKey);
    
    Object updateTenantAccountDetails(TenantAccountConfigMaster req);
    
    Object updateTenantMobileOrdering(String mobileOrderAndWalletConfigRequest, final long tenantAccountId);
}
