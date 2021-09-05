package com.venue.rest.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mobile_order_config")
public class MobileOrderingConfig {
	
	@Id
    @Column(name = "mobile_order_config_id")
    private long mobileOrderConfigId;
	
	@OneToOne(targetEntity=TenantAccountConfigMaster.class)  
	private TenantAccountConfigMaster tenantAccountConfigMaster;  
	
	@Column(name = "sponsor_menu_id")
	private String mobileOrderingSponsorshipMenuId;
	
	@Column(name = "sponsor_app_id")
	private String mobileOrderingSponsorshipAppId;
	
	@Column(name = "sponsor_venue_id")
	private String mobileOrderingSponsorshipVenueId;
	
	@Column(name = "fnB_section_row_seat_label")
	private String mobileOrderingFnBSectionRowSeatLabel;
	
	@Column(name = "merch_section_row_seat_label")
	private String mobileOrderingMerchSectionRowSeatLabel;
	
	@Column(name = "in_seat_selection_fnB_copy_text")
	private String mobileOrderingInSeatSelectionFnBCopyText;
	
	@Column(name = "in_seat_selection_merch_copy_text")
	private String mobileOrderingInSeatSelectionMerchCopyText;
	
	@Column(name = "primary_branding_color_font")
    private String mobileOrderingPrimaryBrandingColorFont;
    
    @Column(name = "primary_branding_color_btn")
    private String mobileOrderingPrimaryBrandingColorBtn;
    
    @Column(name = "notes_supported")
    private boolean mobileOrderingNotesSupported;
    
    @Column(name = "search_supported")
    private boolean mobileOrderingSearchSupported;
    
    @Column(name = "sms_phone_number_supported")
    private boolean mobileOrderingSmsPhoneNumberSupported;
    
    @Column(name = "account_menu_id")
    private String mobileOrderingAccountMenuId;
	
	@Column(name = "guest_checkout_supported")
    private boolean mobileOrderingGuestCheckoutSupported;
	
	@Column(name = "transfer_benefit_supported")
    private boolean mobileOrderingTransferBenefitSupported;
	
	@Column(name = "sms_phone_number_copy_text")
    private String mobileOrderingSmsPhoneNumberCopyText;
	
	@Column(name = "sms_phone_number_required")
    private boolean mobileOrderingSmsPhoneNumberRequired;
	
	@Column(name = "merch_app_enabled")
	private boolean mobileOrderingMerchAppEnabled;
	
	@Column(name = "fnb_app_enabled")
	private boolean mobileOrderingFnBAppEnabled;
    
    @Column(name = "page_details")
    private String mobileOrderingPageDetails;
    
	@Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
    
    
    public MobileOrderingConfig() {
    	
    }
    
    public long getMobileOrderConfigId() {
		return mobileOrderConfigId;
	}

	public void setMobileOrderConfigId(long mobileOrderConfigId) {
		this.mobileOrderConfigId = mobileOrderConfigId;
	}

	public TenantAccountConfigMaster getTenantAccountConfigMaster() {
		return tenantAccountConfigMaster;
	}

	public void setTenantAccountConfigMaster(TenantAccountConfigMaster tenantAccountConfigMaster) {
		this.tenantAccountConfigMaster = tenantAccountConfigMaster;
	}

	public String getMobileOrderingSponsorshipMenuId() {
		return mobileOrderingSponsorshipMenuId;
	}

	public void setMobileOrderingSponsorshipMenuId(String mobileOrderingSponsorshipMenuId) {
		this.mobileOrderingSponsorshipMenuId = mobileOrderingSponsorshipMenuId;
	}
	
	public String getMobileOrderingSponsorshipAppId() {
		return mobileOrderingSponsorshipAppId;
	}

	public void setMobileOrderingSponsorshipAppId(String mobileOrderingSponsorshipAppId) {
		this.mobileOrderingSponsorshipAppId = mobileOrderingSponsorshipAppId;
	}

	public String getMobileOrderingSponsorshipVenueId() {
		return mobileOrderingSponsorshipVenueId;
	}

	public void setMobileOrderingSponsorshipVenueId(String mobileOrderingSponsorshipVenueId) {
		this.mobileOrderingSponsorshipVenueId = mobileOrderingSponsorshipVenueId;
	}
	
	public String getMobileOrderingFnBSectionRowSeatLabel() {
		return mobileOrderingFnBSectionRowSeatLabel;
	}

	public void setMobileOrderingFnBSectionRowSeatLabel(String mobileOrderingFnBSectionRowSeatLabel) {
		this.mobileOrderingFnBSectionRowSeatLabel = mobileOrderingFnBSectionRowSeatLabel;
	}

	public String getMobileOrderingMerchSectionRowSeatLabel() {
		return mobileOrderingMerchSectionRowSeatLabel;
	}

	public void setMobileOrderingMerchSectionRowSeatLabel(String mobileOrderingMerchSectionRowSeatLabel) {
		this.mobileOrderingMerchSectionRowSeatLabel = mobileOrderingMerchSectionRowSeatLabel;
	}

	public String getMobileOrderingInSeatSelectionFnBCopyText() {
		return mobileOrderingInSeatSelectionFnBCopyText;
	}

	public void setMobileOrderingInSeatSelectionFnBCopyText(String mobileOrderingInSeatSelectionFnBCopyText) {
		this.mobileOrderingInSeatSelectionFnBCopyText = mobileOrderingInSeatSelectionFnBCopyText;
	}

	public String getMobileOrderingInSeatSelectionMerchCopyText() {
		return mobileOrderingInSeatSelectionMerchCopyText;
	}

	public void setMobileOrderingInSeatSelectionMerchCopyText(String mobileOrderingInSeatSelectionMerchCopyText) {
		this.mobileOrderingInSeatSelectionMerchCopyText = mobileOrderingInSeatSelectionMerchCopyText;
	}

	public String getMobileOrderingPrimaryBrandingColorFont() {
		return mobileOrderingPrimaryBrandingColorFont;
	}

	public void setMobileOrderingPrimaryBrandingColorFont(String mobileOrderingPrimaryBrandingColorFont) {
		this.mobileOrderingPrimaryBrandingColorFont = mobileOrderingPrimaryBrandingColorFont;
	}

	public String getMobileOrderingPrimaryBrandingColorBtn() {
		return mobileOrderingPrimaryBrandingColorBtn;
	}

	public void setMobileOrderingPrimaryBrandingColorBtn(String mobileOrderingPrimaryBrandingColorBtn) {
		this.mobileOrderingPrimaryBrandingColorBtn = mobileOrderingPrimaryBrandingColorBtn;
	}

	public boolean isMobileOrderingNotesSupported() {
		return mobileOrderingNotesSupported;
	}

	public void setMobileOrderingNotesSupported(boolean mobileOrderingNotesSupported) {
		this.mobileOrderingNotesSupported = mobileOrderingNotesSupported;
	}

	public boolean isMobileOrderingSearchSupported() {
		return mobileOrderingSearchSupported;
	}

	public void setMobileOrderingSearchSupported(boolean mobileOrderingSearchSupported) {
		this.mobileOrderingSearchSupported = mobileOrderingSearchSupported;
	}
	
	public boolean isMobileOrderingSmsPhoneNumberSupported() {
		return mobileOrderingSmsPhoneNumberSupported;
	}

	public void setMobileOrderingSmsPhoneNumberSupported(boolean mobileOrderingSmsPhoneNumberSupported) {
		this.mobileOrderingSmsPhoneNumberSupported = mobileOrderingSmsPhoneNumberSupported;
	}
	

	public String getMobileOrderingAccountMenuId() {
		return mobileOrderingAccountMenuId;
	}

	public void setMobileOrderingAccountMenuId(String mobileOrderingAccountMenuId) {
		this.mobileOrderingAccountMenuId = mobileOrderingAccountMenuId;
	}

	public boolean isMobileOrderingGuestCheckoutSupported() {
		return mobileOrderingGuestCheckoutSupported;
	}

	public void setMobileOrderingGuestCheckoutSupported(boolean mobileOrderingGuestCheckoutSupported) {
		this.mobileOrderingGuestCheckoutSupported = mobileOrderingGuestCheckoutSupported;
	}

	public boolean isMobileOrderingTransferBenefitSupported() {
		return mobileOrderingTransferBenefitSupported;
	}

	public void setMobileOrderingTransferBenefitSupported(boolean mobileOrderingTransferBenefitSupported) {
		this.mobileOrderingTransferBenefitSupported = mobileOrderingTransferBenefitSupported;
	}

	public String getMobileOrderingSmsPhoneNumberCopyText() {
		return mobileOrderingSmsPhoneNumberCopyText;
	}

	public void setMobileOrderingSmsPhoneNumberCopyText(String mobileOrderingSmsPhoneNumberCopyText) {
		this.mobileOrderingSmsPhoneNumberCopyText = mobileOrderingSmsPhoneNumberCopyText;
	}
	
	public boolean isMobileOrderingSmsPhoneNumberRequired() {
		return mobileOrderingSmsPhoneNumberRequired;
	}

	public void setMobileOrderingSmsPhoneNumberRequired(boolean mobileOrderingSmsPhoneNumberRequired) {
		this.mobileOrderingSmsPhoneNumberRequired = mobileOrderingSmsPhoneNumberRequired;
	}
	
	public boolean isMobileOrderingMerchAppEnabled() {
		return mobileOrderingMerchAppEnabled;
	}

	public void setMobileOrderingMerchAppEnabled(boolean mobileOrderingMerchAppEnabled) {
		this.mobileOrderingMerchAppEnabled = mobileOrderingMerchAppEnabled;
	}
	
	public boolean isMobileOrderingFnBAppEnabled() {
		return mobileOrderingFnBAppEnabled;
	}

	public void setMobileOrderingFnBAppEnabled(boolean mobileOrderingFnBAppEnabled) {
		this.mobileOrderingFnBAppEnabled = mobileOrderingFnBAppEnabled;
	}

	public String getMobileOrderingPageDetails() {
		return mobileOrderingPageDetails;
	}

	public void setMobileOrderingPageDetails(String mobileOrderingPageDetails) {
		this.mobileOrderingPageDetails = mobileOrderingPageDetails;
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
