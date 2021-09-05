package com.venue.rest.converters;

import com.venue.rest.model.MobileOrderingConfigDTO;
import com.venue.rest.model.MyWalletConfigProperties;
import com.venue.rest.model.TenantAccountConfigMaster;
import com.venue.rest.model.TenantAccountConfigMasterDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TenantAccountConfigMasterToTenantAccountConfigMasterDTOConverter
        implements Converter<TenantAccountConfigMaster, TenantAccountConfigMasterDTO> {

    @Override
    public TenantAccountConfigMasterDTO convert(TenantAccountConfigMaster tenantAccountConfigMaster) {
    	MobileOrderingConfigDTO mobileOrderingConfigDTO = null;
    	if (tenantAccountConfigMaster.getMobileOrderingConfigProperties() != null) {
    		mobileOrderingConfigDTO = new MobileOrderingConfigDTO(
    				tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipMenuId() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipMenuId() :"",
    				tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipAppId() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipAppId() :"",
    				tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipVenueId() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipVenueId():"",
    				tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingFnBSectionRowSeatLabel() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingFnBSectionRowSeatLabel():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingMerchSectionRowSeatLabel() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingMerchSectionRowSeatLabel():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionFnBCopyText() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionFnBCopyText():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionMerchCopyText() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionMerchCopyText():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorFont() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorFont():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorBtn() != null ? tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorBtn():"",
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingNotesSupported(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingSearchSupported(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberSupported(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingAccountMenuId(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingGuestCheckoutSupported(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingTransferBenefitSupported(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingSmsPhoneNumberCopyText(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberRequired(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingMerchAppEnabled(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().isMobileOrderingFnBAppEnabled(),
					tenantAccountConfigMaster.getMobileOrderingConfigProperties().getMobileOrderingPageDetails()
    				);
    	}
    	MyWalletConfigProperties myWalletConfigProperties = tenantAccountConfigMaster.getMyWalletConfigProperties();
    	if(myWalletConfigProperties.getSponsorshipMenuId() == null) {
    		myWalletConfigProperties.setSponsorshipMenuId("");
    	}
    	if (myWalletConfigProperties.getAccountMenuId() == null) {
    		myWalletConfigProperties.setAccountMenuId("");
    	}
        return new TenantAccountConfigMasterDTO(
                tenantAccountConfigMaster.getTenantAccountID(), tenantAccountConfigMaster.getEmkitAPIKey(),
                tenantAccountConfigMaster.getConfigFileURL(), tenantAccountConfigMaster.getThemeFileURL(),
                tenantAccountConfigMaster.getExpressPickupStartPage(),
                tenantAccountConfigMaster.getInSeatOrderStartPage(), tenantAccountConfigMaster.getSportsTeamId(),
                tenantAccountConfigMaster.getTicketmasterTenantAccountId(), tenantAccountConfigMaster.getTicketingPlatform(),
                tenantAccountConfigMaster.isAutoStoreTicketAccountEmail(), tenantAccountConfigMaster.getTicketAccountEmailNamePattern(),
                tenantAccountConfigMaster.getAccessPageCopyText() != null ? tenantAccountConfigMaster.getAccessPageCopyText() : "",
                tenantAccountConfigMaster.getAccessPageBrandingImageUrl() != null ? tenantAccountConfigMaster.getAccessPageBrandingImageUrl() : "",
                tenantAccountConfigMaster.getTenantAccountName() != null ? tenantAccountConfigMaster.getTenantAccountName() : "",
                tenantAccountConfigMaster.getAccessPageDetails() != null ? tenantAccountConfigMaster.getAccessPageDetails() : "",
                tenantAccountConfigMaster.getAppId() != null ? tenantAccountConfigMaster.getAppId() : "0",
                tenantAccountConfigMaster.getVenueId() != null ? tenantAccountConfigMaster.getVenueId() : "0",
                tenantAccountConfigMaster.getPrimaryBrandingColor() != null ? tenantAccountConfigMaster.getPrimaryBrandingColor() : "",
                tenantAccountConfigMaster.getxApiKey(),
                tenantAccountConfigMaster.isLoyaltyIsEnabled(),
                tenantAccountConfigMaster.getLoyaltyIdentifier(),
                myWalletConfigProperties,
                mobileOrderingConfigDTO);
    }
}
