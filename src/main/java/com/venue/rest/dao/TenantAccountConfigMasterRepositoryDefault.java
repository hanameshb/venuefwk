package com.venue.rest.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.venue.rest.model.MobileOrderingConfig;
import com.venue.rest.model.MyWalletConfigProperties;
import com.venue.rest.model.TenantAccountConfigMaster;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.venue.rest.util.Utility;

@Repository
public class TenantAccountConfigMasterRepositoryDefault implements TenantAccountConfigMasterRepository {

    private static Logger logger = Logger.getLogger(TenantAccountConfigMasterRepositoryDefault.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("dataSourceVenue")
    DataSource dataSourceVenue;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSourceVenue);
    }
    TenantAccountConfigMasterRepositoryDefault() { }

    TenantAccountConfigMasterRepositoryDefault(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<TenantAccountConfigMaster> findTenantAccountConfigMasterById(String accountApiKey, long tenantAccountID) {

        logger.info(":: in findTenantAccountConfigMasterById ::");

        logger.info(String.format(":: Querying for tenant account config by id: %d and accountApiKey: %s ::",
                tenantAccountID, accountApiKey));

        final String queryTenantByIdAndAccountApiKey = "SELECT a.tenant_account_id, emkit_api_key, config_file_url, " +
                " theme_file_url, express_pickup_start_page, in_seat_order_start_page, sports_team_id, " +
                " ticketmaster_tenant_account_id, ticketing_platform, auto_store_ticket_account_email, ticket_account_email_name_pattern, "
                + "access_page_copy_text, access_page_branding_image_url, tenant_account_name, access_page_details, app_id, venue_id, "
                + "a.primary_branding_color, a.x_api_key, loyalty_is_enabled, loyalty_identifier, mobile_order_config_id, b.sponsor_menu_id, sponsor_app_id, "
                + "sponsor_venue_id, fnB_section_row_seat_label, in_seat_selection_fnB_copy_text, merch_section_row_seat_label, "
                + "in_seat_selection_merch_copy_text, primary_branding_color_font, primary_branding_color_btn, notes_supported, "
                + "search_supported, sms_phone_number_supported, b.account_menu_id as mobile_ordering_account_menu_id, guest_checkout_supported, transfer_benefit_supported, "
                + "sms_phone_number_copy_text,sms_phone_number_required, merch_app_enabled, fnb_app_enabled, page_details, w.sponsor_menu_id as wallet_sponsor, w.account_menu_id as account_menu_id" +
                " FROM " +
                "   tbl_tenant_account_config_master a left join tbl_mobile_order_config b on (a.tenant_account_id =b.tenant_account_id) " + 
                "left join tbl_wallet_config w on (a.tenant_account_id = w.tenant_account_id) " +
                " WHERE " +
                "   a.tenant_account_id = ? AND a.emkit_api_key = ?";
        final SqlRowSet tenantRowSet = this.jdbcTemplate.queryForRowSet(queryTenantByIdAndAccountApiKey,
                tenantAccountID, accountApiKey);

        final TenantAccountConfigMaster tenantAccountConfigMaster = new TenantAccountConfigMaster();
        if (tenantRowSet != null) {

            while (tenantRowSet.next()) {

                tenantAccountConfigMaster.setTenantAccountID(tenantRowSet.getLong("tenant_account_id"));
                tenantAccountConfigMaster.setEmkitAPIKey(tenantRowSet.getString("emkit_api_key"));
                tenantAccountConfigMaster.setConfigFileURL(tenantRowSet.getString("config_file_url"));
                tenantAccountConfigMaster.setThemeFileURL(tenantRowSet.getString("theme_file_url"));
                tenantAccountConfigMaster.setExpressPickupStartPage(tenantRowSet.getString("express_pickup_start_page"));
                tenantAccountConfigMaster.setInSeatOrderStartPage(tenantRowSet.getString("in_seat_order_start_page"));
                tenantAccountConfigMaster.setSportsTeamId(tenantRowSet.getInt("sports_team_id"));
                tenantAccountConfigMaster.setTicketmasterTenantAccountId(tenantRowSet.getInt("ticketmaster_tenant_account_id"));
                tenantAccountConfigMaster.setTicketingPlatform(tenantRowSet.getString("ticketing_platform"));
                tenantAccountConfigMaster.setAutoStoreTicketAccountEmail(tenantRowSet.getBoolean("auto_store_ticket_account_email"));
                tenantAccountConfigMaster.setTicketAccountEmailNamePattern(tenantRowSet.getString("ticket_account_email_name_pattern"));
                tenantAccountConfigMaster.setAccessPageCopyText(tenantRowSet.getString("access_page_copy_text"));
                tenantAccountConfigMaster.setAccessPageBrandingImageUrl(tenantRowSet.getString("access_page_branding_image_url"));
                tenantAccountConfigMaster.setTenantAccountName(tenantRowSet.getString("tenant_account_name"));
                tenantAccountConfigMaster.setAccessPageDetails(tenantRowSet.getString("access_page_details"));
                tenantAccountConfigMaster.setAppId(tenantRowSet.getString("app_id"));
                tenantAccountConfigMaster.setVenueId(tenantRowSet.getString("venue_id"));
                tenantAccountConfigMaster.setPrimaryBrandingColor(tenantRowSet.getString("primary_branding_color"));
                tenantAccountConfigMaster.setxApiKey(tenantRowSet.getString("x_api_key"));
                tenantAccountConfigMaster.setLoyaltyIsEnabled(tenantRowSet.getBoolean("loyalty_is_enabled"));;
                tenantAccountConfigMaster.setLoyaltyIdentifier(tenantRowSet.getBoolean("loyalty_is_enabled") ? tenantRowSet.getString("loyalty_identifier") : "");
                MyWalletConfigProperties myWalletConfigProperties = new MyWalletConfigProperties();
                myWalletConfigProperties.setSponsorshipMenuId(tenantRowSet.getString("wallet_sponsor"));
                myWalletConfigProperties.setAccountMenuId(tenantRowSet.getString("account_menu_id"));
                tenantAccountConfigMaster.setMyWalletConfigProperties(myWalletConfigProperties);
                
				MobileOrderingConfig mobileOrderingConfig = new MobileOrderingConfig();
				mobileOrderingConfig.setMobileOrderingSponsorshipMenuId(tenantRowSet.getString("sponsor_menu_id"));
				mobileOrderingConfig.setMobileOrderingSponsorshipAppId(tenantRowSet.getString("sponsor_app_id"));
				mobileOrderingConfig.setMobileOrderingSponsorshipVenueId(tenantRowSet.getString("sponsor_venue_id"));
				mobileOrderingConfig
						.setMobileOrderingFnBSectionRowSeatLabel(tenantRowSet.getString("fnB_section_row_seat_label"));
				mobileOrderingConfig.setMobileOrderingMerchSectionRowSeatLabel(
						tenantRowSet.getString("merch_section_row_seat_label"));
				mobileOrderingConfig.setMobileOrderingInSeatSelectionFnBCopyText(
						tenantRowSet.getString("in_seat_selection_fnB_copy_text"));
				mobileOrderingConfig.setMobileOrderingInSeatSelectionMerchCopyText(
						tenantRowSet.getString("in_seat_selection_merch_copy_text"));
				mobileOrderingConfig.setMobileOrderingPrimaryBrandingColorFont(
						tenantRowSet.getString("primary_branding_color_font"));
				mobileOrderingConfig.setMobileOrderingPrimaryBrandingColorBtn(
						tenantRowSet.getString("primary_branding_color_btn"));
				mobileOrderingConfig.setMobileOrderingNotesSupported(
						tenantRowSet.getBoolean("notes_supported"));
				mobileOrderingConfig.setMobileOrderingSearchSupported(
						tenantRowSet.getBoolean("search_supported"));
				mobileOrderingConfig.setMobileOrderingSmsPhoneNumberSupported(
						tenantRowSet.getBoolean("sms_phone_number_supported"));
				mobileOrderingConfig
						.setMobileOrderingAccountMenuId(tenantRowSet.getString("mobile_ordering_account_menu_id"));
				mobileOrderingConfig
						.setMobileOrderingGuestCheckoutSupported(tenantRowSet.getBoolean("guest_checkout_supported"));
				mobileOrderingConfig.setMobileOrderingTransferBenefitSupported(
						tenantRowSet.getBoolean("transfer_benefit_supported"));
				mobileOrderingConfig.setMobileOrderingSmsPhoneNumberCopyText(tenantRowSet.getString("sms_phone_number_copy_text"));
				mobileOrderingConfig.setMobileOrderingSmsPhoneNumberRequired(tenantRowSet.getBoolean("sms_phone_number_required"));
				mobileOrderingConfig.setMobileOrderingMerchAppEnabled(tenantRowSet.getBoolean("merch_app_enabled"));
				mobileOrderingConfig.setMobileOrderingFnBAppEnabled(tenantRowSet.getBoolean("fnb_app_enabled"));
				mobileOrderingConfig.setMobileOrderingPageDetails(tenantRowSet.getString("page_details"));
				tenantAccountConfigMaster.setMobileOrderingConfigProperties(mobileOrderingConfig);
            }
        }

        if (tenantAccountConfigMaster.getTenantAccountID() == 0) {
            logger.info(String.format(":: Tenant Account Config with id: '%d' and accountApiKey: '%s' not found ::",
                    tenantAccountID, accountApiKey));
            return Optional.empty();
        } else {
            logger.info(String.format(":: Tenant Account Config with id: '%d' and accountApiKey: '%s' found ::",
                    tenantAccountID, accountApiKey));
            return Optional.of(tenantAccountConfigMaster);
        }

    }

	/*
	 * VB-632
	 * Getting tenant account list
	 * GET /{accountApiKey}/platform/tenants/
	 */
    @Override
    public Object getTenantsList(final String accountApiKey) {
		try {		
	    	logger.info(":: TenantAccountConfigMasterRespositoryDefault :: getTenantsList() ::");
			SqlRowSet tenantListRst=null;
			ArrayList<HashMap<String, String>> tenantList = new ArrayList<>();
			
			tenantListRst = jdbcTemplate.queryForRowSet("select * from tbl_tenant_account_config_master where emkit_api_key=?",new Object[]{accountApiKey});
			
			while(tenantListRst.next()){
				HashMap<String, String> item = new HashMap<>();
				item.put("emkitApiKey",tenantListRst.getString("emkit_api_key"));
				item.put("tenantAccountId",tenantListRst.getString("tenant_account_id"));
				item.put("tenantAccountName",tenantListRst.getString("tenant_account_name"));
				tenantList.add(item);
			}
			
			if(tenantList.size() > 0) {
				return tenantList;
			} else {
				return Utility.errorMessage("404", "No records found");
			}
		} catch (Exception e) {
	    	logger.error(":: TenantAccountConfigMasterRespositoryDefault :: getTenantsList() :: ", e);
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
    		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantAccountDetails() ::");
    		if(isEmpty(tenantAccountConfigMasterRequest.getTenantAccountName())) {
    			logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantAccountDetails() :: Tenant Account name is required");
    			return Utility.errorMessage("400", "Bad request, Tenant Account Name is required");
    		}
    		
        	String updateQuery = "update tbl_tenant_account_config_master set " +
					"config_file_url=?," +
					"theme_file_url=?," +
					"express_pickup_start_page=?," +
					"in_seat_order_start_page=?," +
					"sports_team_id=?," +
					"ticketmaster_tenant_account_id=?," +
					"ticketing_platform=?," +
					"auto_store_ticket_account_email=?," +
					"ticket_account_email_name_pattern=?," +
					"access_page_copy_text=?," +
					"access_page_branding_image_url=?," +
					"tenant_account_name=?," +
					"access_page_details=?," +
					"app_id=?," +
					"venue_id=?," +
					"primary_branding_color=?," +
					"x_api_key=? where tenant_account_id=?";
        	int updateValueStatus = 0;
        	updateValueStatus = jdbcTemplate.update(updateQuery, tenantAccountConfigMasterRequest.getConfigFileURL(),
								        				tenantAccountConfigMasterRequest.getThemeFileURL(),
								        				tenantAccountConfigMasterRequest.getExpressPickupStartPage(),
								        				tenantAccountConfigMasterRequest.getInSeatOrderStartPage(),
								        				tenantAccountConfigMasterRequest.getSportsTeamId(),
								        				tenantAccountConfigMasterRequest.getTicketmasterTenantAccountId(),
								        				tenantAccountConfigMasterRequest.getTicketingPlatform(),
								        				tenantAccountConfigMasterRequest.isAutoStoreTicketAccountEmail(),
								        				tenantAccountConfigMasterRequest.getTicketAccountEmailNamePattern(),
								        				tenantAccountConfigMasterRequest.getAccessPageCopyText(),
								        				tenantAccountConfigMasterRequest.getAccessPageBrandingImageUrl(),
														tenantAccountConfigMasterRequest.getTenantAccountName(),
														tenantAccountConfigMasterRequest.getAccessPageDetails(),
														tenantAccountConfigMasterRequest.getAppId(),
														tenantAccountConfigMasterRequest.getVenueId(),
														tenantAccountConfigMasterRequest.getPrimaryBrandingColor(),
														tenantAccountConfigMasterRequest.getxApiKey(),
														tenantAccountConfigMasterRequest.getTenantAccountID());
        	
        	return Utility.successMessage("200", "Updated Tenant Account Details Successfully");
    	} catch(Exception e) {
    		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantAccountDetails() :: ", e);
    		return Utility.errorMessage("500", "Updating Tenant Account Details Failed");
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
    		
			JsonNode mobileOrderAndWalletConfigRequestJsonNode = new ObjectMapper().readValue(mobileOrderAndWalletConfigRequest, JsonNode.class);
			
			MobileOrderingConfig mobileOrderConfigRequest = new ObjectMapper().readValue(mobileOrderAndWalletConfigRequestJsonNode.get("mobileOrderingDetails").toString(), MobileOrderingConfig.class);
			MyWalletConfigProperties walletConfigRequest = new ObjectMapper().readValue(mobileOrderAndWalletConfigRequestJsonNode.get("walletConfigDetails").toString(), MyWalletConfigProperties.class);
    		
    		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantMobileOrdering() ::");
    		
        	String updateMobileOrderConfigQuery = "update tbl_mobile_order_config set " +
					"sponsor_menu_id=?," +
					"sponsor_app_id=?," +
					"sponsor_venue_id=?," +
					"fnB_section_row_seat_label=?," +
					"in_seat_selection_fnB_copy_text=?," +
					"merch_section_row_seat_label=?," +
					"in_seat_selection_merch_copy_text=?," +
					"primary_branding_color_font=?," +
					"primary_branding_color_btn=?," +
					"notes_supported=?," +
					"search_supported=?," +
					"sms_phone_number_supported=?," +
					"page_details=?," +
					"account_menu_id=?," +
					"guest_checkout_supported=?," +
					"transfer_benefit_supported=?," +
					"sms_phone_number_copy_text=?," +
					"sms_phone_number_required=?," +
					"merch_app_enabled=?," +
					"fnb_app_enabled=? where tenant_account_id=?";
        	int updateMobileOrderValueStatus = 0;
        	updateMobileOrderValueStatus = jdbcTemplate.update(updateMobileOrderConfigQuery,mobileOrderConfigRequest.getMobileOrderingSponsorshipMenuId(),
									        			mobileOrderConfigRequest.getMobileOrderingSponsorshipAppId(),
									        			mobileOrderConfigRequest.getMobileOrderingSponsorshipVenueId(),
									        			mobileOrderConfigRequest.getMobileOrderingFnBSectionRowSeatLabel(),
									        			mobileOrderConfigRequest.getMobileOrderingInSeatSelectionFnBCopyText(),
									        			mobileOrderConfigRequest.getMobileOrderingMerchSectionRowSeatLabel(),
									        			mobileOrderConfigRequest.getMobileOrderingInSeatSelectionMerchCopyText(),
									        			mobileOrderConfigRequest.getMobileOrderingPrimaryBrandingColorFont(),
        												mobileOrderConfigRequest.getMobileOrderingPrimaryBrandingColorBtn(),
        												mobileOrderConfigRequest.isMobileOrderingNotesSupported(),
        												mobileOrderConfigRequest.isMobileOrderingSearchSupported(),
        												mobileOrderConfigRequest.isMobileOrderingSmsPhoneNumberSupported(),
        												mobileOrderConfigRequest.getMobileOrderingPageDetails(),
        												mobileOrderConfigRequest.getMobileOrderingAccountMenuId(),
        												mobileOrderConfigRequest.isMobileOrderingGuestCheckoutSupported(),
        												mobileOrderConfigRequest.isMobileOrderingTransferBenefitSupported(),
        												mobileOrderConfigRequest.getMobileOrderingSmsPhoneNumberCopyText(),
        												mobileOrderConfigRequest.isMobileOrderingSmsPhoneNumberRequired(),
        												mobileOrderConfigRequest.isMobileOrderingMerchAppEnabled(),
        												mobileOrderConfigRequest.isMobileOrderingFnBAppEnabled(),
        												tenantAccountId);
        	
        	// Save/Update wallet config
        	String queryWalletConfig = "select count(*) as rowcount from tbl_wallet_config where tenant_account_id=?";
        	SqlRowSet walletConfigRst = jdbcTemplate.queryForRowSet(queryWalletConfig,new Object[]{tenantAccountId});
        	walletConfigRst.next();
        	int rowCount = walletConfigRst.getInt("rowcount");
        	
        	String walletConfigSponsorshipMenuId = walletConfigRequest.getSponsorshipMenuId();
        	String walletConfigAccountMenuId = walletConfigRequest.getAccountMenuId();
        	
        	if(rowCount > 0) {
        		
        		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantMobileOrdering() :: Update tbl_wallet_config");
        		
        		String updateWalletConfigQuery = "update tbl_wallet_config set " +
							    							"sponsor_menu_id=?," +
							    							"account_menu_id=? where tenant_account_id=?";
        		int updateWalletConfigStatus = 0;
        		updateWalletConfigStatus = jdbcTemplate.update(updateWalletConfigQuery, new Object[]{walletConfigSponsorshipMenuId,
	        																							walletConfigAccountMenuId,
	        																							tenantAccountId});
        	} else {
        		
        		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantMobileOrdering() :: Insert tbl_wallet_config");
        		
        		String insertWalletConfigQuery = "insert into tbl_wallet_config (tenant_account_id, sponsor_menu_id, account_menu_id, created_at) values (?, ?, ?, now())";
        		int insertWalletConfigStatus = 0;
        		insertWalletConfigStatus = jdbcTemplate.update(insertWalletConfigQuery, new Object[]{tenantAccountId,
																					        			walletConfigSponsorshipMenuId,
																					        			walletConfigAccountMenuId});
        	}
        	
        	return Utility.successMessage("200", "Updated Mobile Ordering Config Successfully");
    	} catch(Exception e) {
    		logger.info(":: TenantAccountConfigMasterRespositoryDefault :: updateTenantMobileOrdering() :: ", e);
    		return Utility.errorMessage("500", "Updating Mobile Ordering Config Failed");
    	}
    }
}
