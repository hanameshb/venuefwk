package com.venue.rest.exceptions;

public class TenantAccountConfigNotFoundException extends Exception {
    public TenantAccountConfigNotFoundException(final String accountApiKey, final long tenantAccountId) {
        super(String.format("TenantAccountConfig with id: '%d' and accountApiKey: '%s' not found.", tenantAccountId,
                accountApiKey));
    }
}
