package com.venue.rest.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_tenant_account_config_master")
public class TenantAccountConfigMaster implements Serializable {

    @Id
    @Column(name = "tenant_account_id")
    private long tenantAccountID;

    @Column(name = "emkit_api_key")
    private String emkitAPIKey;

    @Column(name = "config_file_url")
    private String configFileURL;

    @Column(name = "theme_file_url")
    private String themeFileURL;

    @Column(name = "express_pickup_start_page")
    private String expressPickupStartPage;

    @Column(name = "in_seat_order_start_page")
    private String inSeatOrderStartPage;

    @Column(name = "sports_team_id")
    private int sportsTeamId;

    @Column(name = "ticketmaster_tenant_account_id")
    private int ticketmasterTenantAccountId;
    
    @Column(name = "ticketing_platform")
    private String ticketingPlatform;
    
    @Column(name = "auto_store_ticket_account_email")
    private boolean autoStoreTicketAccountEmail;
    
    @Column(name = "ticket_account_email_name_pattern")
    private String ticketAccountEmailNamePattern;
    
    @Column(name = "access_page_copy_text")
    private String accessPageCopyText;
    
    @Column(name = "access_page_branding_image_url")
    private String accessPageBrandingImageUrl;

    @Column(name = "tenant_account_name")
    private String tenantAccountName;

    @Column(name = "access_page_details")
    private String accessPageDetails;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "venue_id")
    private String venueId;
    
    @Column(name = "primary_branding_color")
    private String primaryBrandingColor;
    
    @Column(name = "x_api_key")
    private String xApiKey;
    
    @Column(name = "loyalty_is_enabled")
    private boolean loyaltyIsEnabled;
    
    @Column(name = "loyalty_identifier")
    private String loyaltyIdentifier;

    private MyWalletConfigProperties myWalletConfigProperties;

    @OneToOne(targetEntity=MobileOrderingConfig.class,cascade=CascadeType.ALL)  
    private MobileOrderingConfig mobileOrderingConfigProperties;  

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public TenantAccountConfigMaster() {
    }

    public TenantAccountConfigMaster(final long tenantAccountID, final String emkitApiKey) {
        this.tenantAccountID = tenantAccountID;
        this.emkitAPIKey = emkitApiKey;
    }

    public long getTenantAccountID() {
        return tenantAccountID;
    }

    public void setTenantAccountID(long tenantAccountID) {
        this.tenantAccountID = tenantAccountID;
    }

    public String getEmkitAPIKey() {
        return emkitAPIKey;
    }

    public void setEmkitAPIKey(String emkitAPIKey) {
        this.emkitAPIKey = emkitAPIKey;
    }

    public String getConfigFileURL() {
        return configFileURL;
    }

    public void setConfigFileURL(String configFileURL) {
        this.configFileURL = configFileURL;
    }

    public String getThemeFileURL() {
        return themeFileURL;
    }

    public void setThemeFileURL(String themeFileURL) {
        this.themeFileURL = themeFileURL;
    }

    public String getExpressPickupStartPage() {
        return expressPickupStartPage;
    }

    public void setExpressPickupStartPage(String expressPickupStartPage) {
        this.expressPickupStartPage = expressPickupStartPage;
    }

    public String getInSeatOrderStartPage() {
        return inSeatOrderStartPage;
    }

    public void setInSeatOrderStartPage(String inSeatOrderStartPage) {
        this.inSeatOrderStartPage = inSeatOrderStartPage;
    }

    public int getSportsTeamId() {
        return sportsTeamId;
    }

    public void setSportsTeamId(int sportsTeamId) {
        this.sportsTeamId = sportsTeamId;
    }

    public int getTicketmasterTenantAccountId() {
        return ticketmasterTenantAccountId;
    }

    public void setTicketmasterTenantAccountId(int ticketmasterTenantAccountId) {
        this.ticketmasterTenantAccountId = ticketmasterTenantAccountId;
    }
    
    public String getAccessPageCopyText() {
		return accessPageCopyText;
	}

	public void setAccessPageCopyText(String accessPageCopyText) {
		this.accessPageCopyText = accessPageCopyText;
	}

	public String getAccessPageBrandingImageUrl() {
		return accessPageBrandingImageUrl;
	}

	public void setAccessPageBrandingImageUrl(String accessPageBrandingImageUrl) {
		this.accessPageBrandingImageUrl = accessPageBrandingImageUrl;
	}

	public String getTenantAccountName() {
		return tenantAccountName;
	}

	public void setTenantAccountName(String tenantAccountName) {
		this.tenantAccountName = tenantAccountName;
	}

	public String getAccessPageDetails() {
		return accessPageDetails;
	}

	public void setAccessPageDetails(String accessPageDetails) {
		this.accessPageDetails = accessPageDetails;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getPrimaryBrandingColor() {
		return primaryBrandingColor;
	}

	public void setPrimaryBrandingColor(String primaryBrandingColor) {
		this.primaryBrandingColor = primaryBrandingColor;
	}

	public String getxApiKey() {
		return xApiKey;
	}

	public void setxApiKey(String xApiKey) {
		this.xApiKey = xApiKey;
	}
	
	public boolean isLoyaltyIsEnabled() {
		return loyaltyIsEnabled;
	}

	public void setLoyaltyIsEnabled(boolean loyaltyIsEnabled) {
		this.loyaltyIsEnabled = loyaltyIsEnabled;
	}

	public String getLoyaltyIdentifier() {
		return loyaltyIdentifier;
	}

	public void setLoyaltyIdentifier(String loyaltyIdentifier) {
		this.loyaltyIdentifier = loyaltyIdentifier;
	}

	public MyWalletConfigProperties getMyWalletConfigProperties() {
		return myWalletConfigProperties;
	}

	public void setMyWalletConfigProperties(MyWalletConfigProperties myWalletConfigProperties) {
		this.myWalletConfigProperties = myWalletConfigProperties;
	}

	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getTicketingPlatform() {
		return ticketingPlatform;
	}

	public void setTicketingPlatform(String ticketingPlatform) {
		this.ticketingPlatform = ticketingPlatform;
	}

	public boolean isAutoStoreTicketAccountEmail() {
		return autoStoreTicketAccountEmail;
	}

	public void setAutoStoreTicketAccountEmail(boolean autoStoreTicketAccountEmail) {
		this.autoStoreTicketAccountEmail = autoStoreTicketAccountEmail;
	}

	public String getTicketAccountEmailNamePattern() {
		return ticketAccountEmailNamePattern;
	}

	public void setTicketAccountEmailNamePattern(String ticketAccountEmailNamePattern) {
		this.ticketAccountEmailNamePattern = ticketAccountEmailNamePattern;
	}
	
	public MobileOrderingConfig getMobileOrderingConfigProperties() {
		return mobileOrderingConfigProperties;
	}

	public void setMobileOrderingConfigProperties(MobileOrderingConfig mobileOrderingConfigProperties) {
		this.mobileOrderingConfigProperties = mobileOrderingConfigProperties;
	}

	@PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date();
        if (this.updatedAt == null) updatedAt = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = new Date();
    }
}
