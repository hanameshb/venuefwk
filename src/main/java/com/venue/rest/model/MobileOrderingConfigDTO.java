package com.venue.rest.model;

public class MobileOrderingConfigDTO {

	private String mobileOrderingSponsorshipMenuId;
	private String mobileOrderingSponsorshipAppId;
	private String mobileOrderingSponsorshipVenueId;
	private String mobileOrderingFnBSectionRowSeatLabel;
	private String mobileOrderingMerchSectionRowSeatLabel;
	private String mobileOrderingInSeatSelectionFnBCopyText;
	private String mobileOrderingInSeatSelectionMerchCopyText;
	private String mobileOrderingPrimaryBrandingColorFont;
    private String mobileOrderingPrimaryBrandingColorBtn;
    private boolean mobileOrderingNotesSupported;
    private boolean mobileOrderingSearchSupported;
    private boolean mobileOrderingSmsPhoneNumberSupported;
    private String mobileOrderingAccountMenuId;
    private boolean mobileOrderingGuestCheckoutSupported;
    private boolean mobileOrderingTransferBenefitSupported;
    private String mobileOrderingSmsPhoneNumberCopyText;
    private boolean mobileOrderingSmsPhoneNumberRequired;
    private boolean mobileOrderingMerchAppEnabled;
    private boolean mobileOrderingFnBAppEnabled;
    private String mobileOrderingPageDetails;
    

	public MobileOrderingConfigDTO(String mobileOrderingSponsorshipMenuId, String mobileOrderingSponsorshipAppId, 
			String mobileOrderingSponsorshipVenueId, String mobileOrderingFnBSectionRowSeatLabel, 
			String mobileOrderingMerchSectionRowSeatLabel, String mobileOrderingInSeatSelectionFnBCopyText, 
			String mobileOrderingInSeatSelectionMerchCopyText, String mobileOrderingPrimaryBrandingColorFont, String mobileOrderingPrimaryBrandingColorBtn,
			boolean mobileOrderingNotesSupported, boolean mobileOrderingSearchSupported, boolean mobileOrderingSmsPhoneNumberSupported, 
			String mobileOrderingAccountMenuId, boolean mobileOrderingGuestCheckoutSupported, boolean mobileOrderingTransferBenefitSupported,
			String mobileOrderingSmsPhoneNumberCopyText,boolean mobileOrderingSmsPhoneNumberRequired, boolean mobileOrderingMerchAppEnabled,
			boolean mobileOrderingFnBAppEnabled, String mobileOrderingPageDetails) {
		this.mobileOrderingSponsorshipMenuId = mobileOrderingSponsorshipMenuId;
		this.mobileOrderingSponsorshipAppId = mobileOrderingSponsorshipAppId;
		this.mobileOrderingSponsorshipVenueId = mobileOrderingSponsorshipVenueId;
		this.mobileOrderingFnBSectionRowSeatLabel=mobileOrderingFnBSectionRowSeatLabel;
		this.mobileOrderingMerchSectionRowSeatLabel=mobileOrderingMerchSectionRowSeatLabel;
		this.mobileOrderingInSeatSelectionFnBCopyText=mobileOrderingInSeatSelectionFnBCopyText;
		this.mobileOrderingInSeatSelectionMerchCopyText=mobileOrderingInSeatSelectionMerchCopyText;
		this.mobileOrderingPrimaryBrandingColorFont = mobileOrderingPrimaryBrandingColorFont;
		this.mobileOrderingPrimaryBrandingColorBtn = mobileOrderingPrimaryBrandingColorBtn;
		this.mobileOrderingNotesSupported = mobileOrderingNotesSupported;
		this.mobileOrderingSearchSupported = mobileOrderingSearchSupported;
		this.mobileOrderingSmsPhoneNumberSupported = mobileOrderingSmsPhoneNumberSupported;
		this.mobileOrderingAccountMenuId = mobileOrderingAccountMenuId;
		this.mobileOrderingGuestCheckoutSupported = mobileOrderingGuestCheckoutSupported;
		this.mobileOrderingTransferBenefitSupported = mobileOrderingTransferBenefitSupported;
		this.mobileOrderingSmsPhoneNumberCopyText = mobileOrderingSmsPhoneNumberCopyText;
		this.mobileOrderingSmsPhoneNumberRequired = mobileOrderingSmsPhoneNumberRequired;
		this.mobileOrderingMerchAppEnabled = mobileOrderingMerchAppEnabled;
		this.mobileOrderingFnBAppEnabled = mobileOrderingFnBAppEnabled;
		this.mobileOrderingPageDetails = mobileOrderingPageDetails;
	}

	public String getMobileOrderingSponsorshipMenuId() {
		return mobileOrderingSponsorshipMenuId;
	}

	public String getMobileOrderingSponsorshipAppId() {
		return mobileOrderingSponsorshipAppId;
	}

	public String getMobileOrderingSponsorshipVenueId() {
		return mobileOrderingSponsorshipVenueId;
	}

	public String getMobileOrderingFnBSectionRowSeatLabel() {
		return mobileOrderingFnBSectionRowSeatLabel;
	}

	public String getMobileOrderingMerchSectionRowSeatLabel() {
		return mobileOrderingMerchSectionRowSeatLabel;
	}

	public String getMobileOrderingInSeatSelectionFnBCopyText() {
		return mobileOrderingInSeatSelectionFnBCopyText;
	}

	public String getMobileOrderingInSeatSelectionMerchCopyText() {
		return mobileOrderingInSeatSelectionMerchCopyText;
	}

	public String getMobileOrderingPrimaryBrandingColorFont() {
		return mobileOrderingPrimaryBrandingColorFont;
	}

	public String getMobileOrderingPrimaryBrandingColorBtn() {
		return mobileOrderingPrimaryBrandingColorBtn;
	}

	public boolean isMobileOrderingNotesSupported() {
		return mobileOrderingNotesSupported;
	}

	public boolean isMobileOrderingSearchSupported() {
		return mobileOrderingSearchSupported;
	}

	public boolean isMobileOrderingSmsPhoneNumberSupported() {
		return mobileOrderingSmsPhoneNumberSupported;
	}
	
	public String getMobileOrderingAccountMenuId() {
		return mobileOrderingAccountMenuId;
	}

	public boolean isMobileOrderingGuestCheckoutSupported() {
		return mobileOrderingGuestCheckoutSupported;
	}
	
	public String getMobileOrderingSmsPhoneNumberCopyText() {
		return mobileOrderingSmsPhoneNumberCopyText;
	}

	public boolean isMobileOrderingTransferBenefitSupported() {
		return mobileOrderingTransferBenefitSupported;
	}
	

	public boolean isMobileOrderingSmsPhoneNumberRequired() {
		return mobileOrderingSmsPhoneNumberRequired;
	}
	
	public boolean isMobileOrderingMerchAppEnabled() {
		return mobileOrderingMerchAppEnabled;
	}
	
	public boolean isMobileOrderingFnBAppEnabled() {
		return mobileOrderingFnBAppEnabled;
	}

	public String getMobileOrderingPageDetails() {
		return mobileOrderingPageDetails;
	}

}
