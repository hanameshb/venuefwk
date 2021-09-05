package com.venue.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(value = "tenantAccountConfig")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantAccountConfigMasterDTO {

    private final long tenantAccountID;

    private final String emkitAPIKey;

    private String configFileURL;

    private String themeFileURL;

    private String expressPickupStartPage;

    private String inSeatOrderStartPage;

    private int sportsTeamId;

    private int ticketmasterTenantAccountId;
    
    private String ticketingPlatform;
    
    private boolean autoStoreTicketAccountEmail;
    
    private String ticketAccountEmailNamePattern;
    
    private String accessPageCopyText;
    
    private String accessPageBrandingImageUrl;

    private String tenantAccountName;

    private String accessPageDetails;

    private String appId;

    private String venueId;

    private String primaryBrandingColor;
    
    private String xApiKey;
    
    private boolean loyaltyIsEnabled;
    
    private String loyaltyIdentifier;

    private MyWalletConfigProperties myWalletConfigProperties;
    
    private MobileOrderingConfigDTO mobileOrderingConfigProperties;

    public TenantAccountConfigMasterDTO(final long tenantAccountID, final String emkitAPIKey, final String configFileURL,
                                        final String themeFileURL, final String expressPickupStartPage,
                                        final String inSeatOrderStartPage, final int sportsTeamId,
                                        final int ticketmasterTenantAccountId, final String ticketingPlatform,
                                        final boolean autoStoreTicketAccountEmail, final String ticketAccountEmailNamePattern,
                                        final String accessPageCopyText, final String accessPageBrandingImageUrl,
                                        final String tenantAccountName, final String accessPageDetails, final String appId, final String venueId,
                                        final String primaryBrandingColor, final String xApiKey, final boolean loyaltyIsEnabled, final String loyaltyIdentifier, final MyWalletConfigProperties myWalletConfigProperties, 
                                        final MobileOrderingConfigDTO mobileOrderingConfigProperties) {
        this.tenantAccountID = tenantAccountID;
        this.emkitAPIKey = emkitAPIKey;
        this.configFileURL = configFileURL;
        this.themeFileURL = themeFileURL;
        this.expressPickupStartPage = expressPickupStartPage;
        this.inSeatOrderStartPage = inSeatOrderStartPage;
        this.sportsTeamId = sportsTeamId;
        this.ticketmasterTenantAccountId = ticketmasterTenantAccountId;
        this.ticketingPlatform = ticketingPlatform;
        this.autoStoreTicketAccountEmail = autoStoreTicketAccountEmail;
        this.ticketAccountEmailNamePattern = ticketAccountEmailNamePattern;
        this.accessPageCopyText = accessPageCopyText;
        this.accessPageBrandingImageUrl = accessPageBrandingImageUrl;
        this.tenantAccountName = tenantAccountName;
        this.accessPageDetails = accessPageDetails;
        this.appId = appId;
        this.venueId = venueId;
        this.primaryBrandingColor = primaryBrandingColor;
        this.xApiKey = xApiKey;
        this.loyaltyIsEnabled = loyaltyIsEnabled;
        this.loyaltyIdentifier = loyaltyIdentifier;
        this.myWalletConfigProperties = myWalletConfigProperties;
        this.mobileOrderingConfigProperties = mobileOrderingConfigProperties;
    }

    public TenantAccountConfigMasterDTO(long tenantAccountID, String emkitAPIKey) {
        this.tenantAccountID = tenantAccountID;
        this.emkitAPIKey = emkitAPIKey;
    }

    public long getTenantAccountID() {
        return tenantAccountID;
    }

    public String getEmkitAPIKey() {
        return emkitAPIKey;
    }

    public String getConfigFileURL() {
        return configFileURL;
    }

    public String getThemeFileURL() {
        return themeFileURL;
    }

    public String getExpressPickupStartPage() {
        return expressPickupStartPage;
    }

    public String getInSeatOrderStartPage() {
        return inSeatOrderStartPage;
    }

    public int getSportsTeamId() {
        return sportsTeamId;
    }

    public int getTicketmasterTenantAccountId() {
        return ticketmasterTenantAccountId;
    }

	public String getTicketingPlatform() {
		return ticketingPlatform;
	}

	public boolean isAutoStoreTicketAccountEmail() {
		return autoStoreTicketAccountEmail;
	}

	public String getTicketAccountEmailNamePattern() {
		return ticketAccountEmailNamePattern;
	}

	public String getAccessPageCopyText() {
		return accessPageCopyText;
	}

	public String getAccessPageBrandingImageUrl() {
		return accessPageBrandingImageUrl;
	}

	public MobileOrderingConfigDTO getMobileOrderingConfigProperties() {
		return mobileOrderingConfigProperties;
	}

	public String getTenantAccountName() {
		return tenantAccountName;
	}

	public String getAccessPageDetails() {
		return accessPageDetails;
	}

	public String getAppId() {
		return appId;
	}

	public String getVenueId() {
		return venueId;
	}

	public String getPrimaryBrandingColor() {
		return primaryBrandingColor;
	}

	public String getxApiKey() {
		return xApiKey;
	}
	
	public boolean isLoyaltyIsEnabled() {
		return loyaltyIsEnabled;
	}

	public String getLoyaltyIdentifier() {
		return loyaltyIdentifier;
	}

	public MyWalletConfigProperties getMyWalletConfigProperties() {
		return myWalletConfigProperties;
	}
	
}