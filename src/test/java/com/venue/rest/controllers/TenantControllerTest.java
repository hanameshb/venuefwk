package com.venue.rest.controllers;

import com.venue.rest.exceptions.TenantAccountConfigNotFoundException;
import com.venue.rest.model.MobileOrderingConfigDTO;
import com.venue.rest.model.MyWalletConfigProperties;
import com.venue.rest.model.TenantAccountConfigMasterDTO;
import com.venue.rest.services.TenantService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class TenantControllerTest {

    @InjectMocks
    private TenantController tenantController;

    private MockMvc mvc;

    @Mock
    private TenantService tenantService;

    private static final String baseUrl = "/accountApiKey1/platform";

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        tenantController = new TenantController(tenantService);
        mvc = MockMvcBuilders.standaloneSetup(tenantController).build();
    }

    @Test
    public void getTenantAccountByIdAndAccountApiKeyTest() throws Exception {

        final long tenantAccountID = 1L;
        final String emkitAPIKey = "accountApiKey";
        final String tenantId = "1";
        final String configFileURL = "https://xyz.com/app.config";
        final String themeFileURL = "https://xyz.com/app.css";
        final String expressPickupStartPage = "expressorder.html";
        final String inSeatOrderStartPage = "inseatorder.html";
        final int sportsTeamID = 7;
        final int ticketmasterTenantAccountID = 15;
        final String ticketingPlatform="Tickets.com";
        final String mobileOrderingSponsorshipMenuId="12345";
        final String mobileOrderingSponsorshipAppId="7";
        final String mobileOrderingSponsorshipVenueId="9";
        
        final String mobileOrderingFnBSectionRowSeatLabel="Section | Row | Seat";
    	final String mobileOrderingMerchSectionRowSeatLabel="Section | Spot";
    	final String mobileOrderingInSeatSelectionFnBCopyText="mobileOrderingInSeatSelectionFnBCopyText";
    	final String mobileOrderingInSeatSelectionMerchCopyText="mobileOrderingInSeatSelectionMerchCopyText";
    	final String mobileOrderingPrimaryBrandingColorFont = "0000ff";
    	final String mobileOrderingPrimaryBrandingColorBtn = "000000";
    	final boolean mobileOrderingNotesSupported = true;
    	final boolean mobileOrderingSearchSupported = false;
    	final boolean mobileOrderingSmsPhoneNumberSupported = true;
    	final String mobileOrderingAccountMenuId ="321";
    	final boolean mobileOrderingGuestCheckoutSupported = true;
    	final boolean mobileOrderingTransferBenefitSupported = false;
    	final boolean mobileOrderingSmsPhoneNumberRequired = true;
    	final boolean mobileOrderingMerchAppEnabled = true;
    	final boolean mobileOrderingFnBAppEnabled = true;
    	final String mobileOrderingSmsPhoneNumberCopyText = "mobile copy text" ;
    	final String mobileOrderingPageDetails = "{\r\n" + 
    			"{ \\\"enumId\\\":1001, \\\"id\\\":\\\"QUESTIONNAIRE\\\", \\\"title\\\":\\\"QUESTIONNAIRE\\\" }}";
        
        final boolean autoStoreTicketAccountEmail=true;
        final MobileOrderingConfigDTO mobileOrderingConfig = new MobileOrderingConfigDTO(mobileOrderingSponsorshipMenuId, mobileOrderingSponsorshipAppId, mobileOrderingSponsorshipVenueId,
        		mobileOrderingFnBSectionRowSeatLabel, mobileOrderingMerchSectionRowSeatLabel, mobileOrderingInSeatSelectionFnBCopyText, mobileOrderingInSeatSelectionMerchCopyText,
        		mobileOrderingPrimaryBrandingColorFont, mobileOrderingPrimaryBrandingColorBtn, mobileOrderingNotesSupported, mobileOrderingSearchSupported, mobileOrderingSmsPhoneNumberSupported, 
        		mobileOrderingAccountMenuId, mobileOrderingGuestCheckoutSupported, mobileOrderingTransferBenefitSupported, mobileOrderingSmsPhoneNumberCopyText, mobileOrderingSmsPhoneNumberRequired,
        		mobileOrderingMerchAppEnabled, mobileOrderingFnBAppEnabled, mobileOrderingPageDetails);
        final String ticketAccountEmailNamePattern="tdc{{teamID}}TicketAccountEmail";
        final String accessPageCopyText = "accessPageCopyText";
        final String accessPageBrandingImageUrl = "accessPageBrandingImageUrl";
        final String tenantAccountName = "tenantAccountName";
        final String accessPageDetails = "accessPageDetails";
        final String appId = "appId";
        final String venueId = "venueId";
        final String primaryBrandingColor = "#ffffff";
        final String xApiKey = "mock_x_api_key";
        final boolean loyaltyIsEnabled = true;
        final String loyaltyIdentifier = "loyaltyIdentifier";
        final MyWalletConfigProperties myWalletConfigProperties = new MyWalletConfigProperties();
        myWalletConfigProperties.setSponsorshipMenuId("mock_wallet_sponsor");
        myWalletConfigProperties.setAccountMenuId("121");
        final TenantAccountConfigMasterDTO tenantAccountConfigMasterDTO = new TenantAccountConfigMasterDTO(tenantAccountID, emkitAPIKey, configFileURL, themeFileURL, expressPickupStartPage, 
        		inSeatOrderStartPage, sportsTeamID, ticketmasterTenantAccountID, ticketingPlatform, autoStoreTicketAccountEmail, ticketAccountEmailNamePattern, accessPageCopyText, accessPageBrandingImageUrl, 
        		tenantAccountName, accessPageDetails, appId, venueId, primaryBrandingColor, xApiKey, loyaltyIsEnabled, loyaltyIdentifier, myWalletConfigProperties, mobileOrderingConfig);
        when(tenantService.getTenantAccountConfigById("accountApiKey1", 1L))
                .thenReturn(tenantAccountConfigMasterDTO);

        mvc.perform(get(baseUrl + "/tenants/"+ tenantId+ "/v1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantAccountConfig.tenantAccountID",
                        is(1)))
                .andExpect(jsonPath("$.tenantAccountConfig.emkitAPIKey",
                        is(tenantAccountConfigMasterDTO.getEmkitAPIKey())))
                .andExpect(jsonPath("$.tenantAccountConfig.configFileURL",
                        is(tenantAccountConfigMasterDTO.getConfigFileURL())))
                .andExpect(jsonPath("$.tenantAccountConfig.themeFileURL",
                        is(tenantAccountConfigMasterDTO.getThemeFileURL())))
                .andExpect(jsonPath("$.tenantAccountConfig.expressPickupStartPage",
                        is(tenantAccountConfigMasterDTO.getExpressPickupStartPage())))
                .andExpect(jsonPath("$.tenantAccountConfig.inSeatOrderStartPage",
                        is(tenantAccountConfigMasterDTO.getInSeatOrderStartPage())))
                .andExpect(jsonPath("$.tenantAccountConfig.sportsTeamId",
                        is(tenantAccountConfigMasterDTO.getSportsTeamId())))
                .andExpect(jsonPath("$.tenantAccountConfig.ticketmasterTenantAccountId",
                        is(tenantAccountConfigMasterDTO.getTicketmasterTenantAccountId())))
                .andExpect(jsonPath("$.tenantAccountConfig.ticketingPlatform",
                        is(tenantAccountConfigMasterDTO.getTicketingPlatform())))
                .andExpect(jsonPath("$.tenantAccountConfig.autoStoreTicketAccountEmail",
                        is(tenantAccountConfigMasterDTO.isAutoStoreTicketAccountEmail())))
                .andExpect(jsonPath("$.tenantAccountConfig.ticketAccountEmailNamePattern",
                		is(tenantAccountConfigMasterDTO.getTicketAccountEmailNamePattern())))
                .andExpect(jsonPath("$.tenantAccountConfig.accessPageCopyText",
                		is(tenantAccountConfigMasterDTO.getAccessPageCopyText())))
                .andExpect(jsonPath("$.tenantAccountConfig.accessPageBrandingImageUrl",
                		is(tenantAccountConfigMasterDTO.getAccessPageBrandingImageUrl())))
                .andExpect(jsonPath("$.tenantAccountConfig.tenantAccountName",
                		is(tenantAccountConfigMasterDTO.getTenantAccountName())))
                .andExpect(jsonPath("$.tenantAccountConfig.accessPageDetails",
                		is(tenantAccountConfigMasterDTO.getAccessPageDetails())))
                .andExpect(jsonPath("$.tenantAccountConfig.appId",
                		is(tenantAccountConfigMasterDTO.getAppId())))
                .andExpect(jsonPath("$.tenantAccountConfig.venueId",
                		is(tenantAccountConfigMasterDTO.getVenueId())))
                .andExpect(jsonPath("$.tenantAccountConfig.primaryBrandingColor",
                		is(tenantAccountConfigMasterDTO.getPrimaryBrandingColor())))
                .andExpect(jsonPath("$.tenantAccountConfig.xApiKey",
                		is(tenantAccountConfigMasterDTO.getxApiKey())))
                .andExpect(jsonPath("$.tenantAccountConfig.loyaltyIsEnabled",
                		is(tenantAccountConfigMasterDTO.isLoyaltyIsEnabled())))
                .andExpect(jsonPath("$.tenantAccountConfig.loyaltyIdentifier",
                		is(tenantAccountConfigMasterDTO.getLoyaltyIdentifier())))
                .andExpect(jsonPath("$.tenantAccountConfig.myWalletConfigProperties.sponsorshipMenuId",
                		is(tenantAccountConfigMasterDTO.getMyWalletConfigProperties().getSponsorshipMenuId())))
                .andExpect(jsonPath("$.tenantAccountConfig.myWalletConfigProperties.accountMenuId",
                		is(tenantAccountConfigMasterDTO.getMyWalletConfigProperties().getAccountMenuId())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSponsorshipMenuId",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipMenuId())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSponsorshipAppId",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipAppId())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSponsorshipVenueId",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingSponsorshipVenueId())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingFnBSectionRowSeatLabel",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingFnBSectionRowSeatLabel())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingMerchSectionRowSeatLabel",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingMerchSectionRowSeatLabel())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingInSeatSelectionFnBCopyText",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionFnBCopyText())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingInSeatSelectionMerchCopyText",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingInSeatSelectionMerchCopyText())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingPrimaryBrandingColorFont",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorFont())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingPrimaryBrandingColorBtn",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingPrimaryBrandingColorBtn())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingNotesSupported",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingNotesSupported())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSearchSupported",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingSearchSupported())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSmsPhoneNumberSupported",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberSupported())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingAccountMenuId",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingAccountMenuId())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingGuestCheckoutSupported",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingGuestCheckoutSupported())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingTransferBenefitSupported",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingTransferBenefitSupported())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSmsPhoneNumberCopyText",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingSmsPhoneNumberCopyText())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSmsPhoneNumberRequired",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberRequired())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingMerchAppEnabled",
						is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingMerchAppEnabled())))
				.andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingFnBAppEnabled",
						is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().isMobileOrderingFnBAppEnabled())))
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingPageDetails",
                		is(tenantAccountConfigMasterDTO.getMobileOrderingConfigProperties().getMobileOrderingPageDetails())));

    }

    @Test
    public void getTenantAccountByIdAndAccountApiKeyWithOnlyRequiredFieldsTest() throws Exception {

        final long tenantAccountID = 1L;
        final String emkitAPIKey = "accountApiKey";
        final TenantAccountConfigMasterDTO tenantAccountConfigMasterDTO = new TenantAccountConfigMasterDTO(
                tenantAccountID, emkitAPIKey);
        when(tenantService.getTenantAccountConfigById("accountApiKey1", 1L))
                .thenReturn(tenantAccountConfigMasterDTO);

        mvc.perform(get(baseUrl + "/tenants/1/v1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantAccountConfig.tenantAccountID",
                        is(1)))
                .andExpect(jsonPath("$.tenantAccountConfig.emkitAPIKey",
                        is(tenantAccountConfigMasterDTO.getEmkitAPIKey())))
                .andExpect(jsonPath("$.tenantAccountConfig.autoStoreTicketAccountEmail",
                        is(tenantAccountConfigMasterDTO.isAutoStoreTicketAccountEmail())))
                .andExpect(jsonPath("$.tenantAccountConfig.configFileURL").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.themeFileURL").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.expressPickupStartPage").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.inSeatOrderStartPage").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.sportsTeamID").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.ticketmasterTenantAccountID").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.ticketingPlatform").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.ticketAccountEmailNamePattern").doesNotExist())
                .andExpect(jsonPath("$.tenantAccountConfig.mobileOrderingConfigProperties.mobileOrderingSponsorshipMenuId").doesNotExist());

    }

    @Test
    public void getTenantAccountByIdAndAccountApiKeyNotFoundTest() throws Exception {

        when(tenantService.getTenantAccountConfigById("accountApiKey1", 1L))
                .thenThrow(new TenantAccountConfigNotFoundException("accountApiKe1", 1L));

        mvc.perform(get(baseUrl + "/tenants/1/v1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

}
