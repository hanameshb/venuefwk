package com.venue.rest.dao;

import com.venue.rest.model.TenantAccountConfigMaster;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TenantAccountConfigMasterRepositoryDefaultTest {

    private TenantAccountConfigMasterRepositoryDefault tenantAccountConfigMasterRepositoryDefault;
    private JdbcTemplate jdbcTemplate;
    private SqlRowSet sqlRowSetMock;
    private Map<SqlRowSet, MockRow> mockRows;

    @Before
    public void setUp() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        mockRows = new HashMap<SqlRowSet, MockRow>();

        tenantAccountConfigMasterRepositoryDefault = new TenantAccountConfigMasterRepositoryDefault(jdbcTemplate);
    }

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

    @Test
    public void findTenantAccountConfigMasterByIdTest() {

        // Fake results for menu place sql query
        String primaryBrandingColor = "mock_primary_branding_color";
        String walletSponserId = "mock_wallet_sponsor";
        String accountMenuId = "121";
        String xApiKey = "mock_x_api_key";
        String loyaltyIdentifier = "loyaltyIdentifier";
        List<Map<String, String>> placeResult = new ArrayList<>();
        placeResult.add(new HashMap<>());
        placeResult.get(0).put("tenant_account_id", "1");
        placeResult.get(0).put("emkit_api_key", "accountApiKey1");
        placeResult.get(0).put("config_file_url", "https://xyz.com/app.config");
        placeResult.get(0).put("theme_file_url", "https://xyz.com/app.css");
        placeResult.get(0).put("express_pickup_start_page", "expressorder.html");
        placeResult.get(0).put("in_seat_order_start_page", "inseatorder.html");
        placeResult.get(0).put("sports_team_id", "7");
        placeResult.get(0).put("ticketmaster_tenant_account_id", "15");
        placeResult.get(0).put("ticketing_platform", "Tickets.com");
        placeResult.get(0).put("auto_store_ticket_account_email", "1");
        placeResult.get(0).put("ticket_account_email_name_pattern", "tdc{{teamID}}TicketAccountEmail");
        placeResult.get(0).put("notes_supported", "1");
        placeResult.get(0).put("search_supported", "0");
        placeResult.get(0).put("sms_phone_number_supported", "0");
		placeResult.get(0).put("primary_branding_color", primaryBrandingColor);
		placeResult.get(0).put("wallet_sponsor", walletSponserId);
		placeResult.get(0).put("account_menu_id", accountMenuId);
		placeResult.get(0).put("mobile_ordering_account_menu_id", "321");
		placeResult.get(0).put("sms_phone_number_copy_text", "phone copy");
		placeResult.get(0).put("guest_checkout_supported", "1");
		placeResult.get(0).put("sms_phone_number_required", "1");
		placeResult.get(0).put("merch_app_enabled", "1");
		placeResult.get(0).put("fnb_app_enabled", "1");
		placeResult.get(0).put("transfer_benefit_supported", "phone copy");
		placeResult.get(0).put("x_api_key", xApiKey);
		placeResult.get(0).put("loyalty_is_enabled", "1");
		placeResult.get(0).put("loyalty_identifier", loyaltyIdentifier);

        doReturn(generateFakeRowSet(placeResult))
                .when(jdbcTemplate)
                .queryForRowSet(queryTenantByIdAndAccountApiKey, new Object[]{1L, "accountApiKey1"});

        Optional<TenantAccountConfigMaster> tenantAccountConfigMasterOptional =
                tenantAccountConfigMasterRepositoryDefault.findTenantAccountConfigMasterById("accountApiKey1", 1L);

        TenantAccountConfigMaster response = tenantAccountConfigMasterOptional.get();
		assertEquals(response.getTenantAccountID(), 1);
        assertEquals(response.getEmkitAPIKey(), "accountApiKey1");
        assertEquals(response.getConfigFileURL(), "https://xyz.com/app.config");
        assertEquals(response.getThemeFileURL(), "https://xyz.com/app.css");
        assertEquals(response.getExpressPickupStartPage(), "expressorder.html");
        assertEquals(response.getInSeatOrderStartPage(), "inseatorder.html");
        assertEquals(response.getSportsTeamId(), 7);
        assertEquals(response.getTicketmasterTenantAccountId(), 15);
        assertEquals(response.getTicketingPlatform(), "Tickets.com");
        assertEquals(response.isAutoStoreTicketAccountEmail(), true);
        assertEquals(response.isLoyaltyIsEnabled(), true);
        assertEquals(response.getLoyaltyIdentifier(), loyaltyIdentifier);
        assertEquals(response.getTicketAccountEmailNamePattern(), "tdc{{teamID}}TicketAccountEmail");
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingNotesSupported(), true);
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingSearchSupported(), false);
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberSupported(), false);
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingSmsPhoneNumberRequired(), true);
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingMerchAppEnabled(), true);
        assertEquals(response.getMobileOrderingConfigProperties().isMobileOrderingFnBAppEnabled(), true);
        assertEquals(response.getPrimaryBrandingColor(), primaryBrandingColor);
        assertEquals(response.getMyWalletConfigProperties().getSponsorshipMenuId(), walletSponserId);
        assertEquals(response.getMyWalletConfigProperties().getAccountMenuId(), accountMenuId);
        assertEquals(response.getxApiKey(), xApiKey);
    }

    @Test
    public void findTenantAccountConfigMasterByIdOnlyRequiredFieldsTest() {

        // Fake results for menu place sql query
        List<Map<String, String>> placeResult = new ArrayList<>();
        placeResult.add(new HashMap<>());
        placeResult.get(0).put("tenant_account_id", "1");
        placeResult.get(0).put("emkit_api_key", "accountApiKey1");
        placeResult.get(0).put("auto_store_ticket_account_email", "0");
        placeResult.get(0).put("notes_supported", "1");
        placeResult.get(0).put("search_supported", "0");
        placeResult.get(0).put("sms_phone_number_supported", "0");
        placeResult.get(0).put("x_api_key", "");
        placeResult.get(0).put("loyalty_is_enabled", "1");
        
        placeResult.get(0).put("mobile_ordering_account_menu_id", "321");
		placeResult.get(0).put("sms_phone_number_copy_text", "phone copy");
		placeResult.get(0).put("guest_checkout_supported", "1");
		placeResult.get(0).put("sms_phone_number_required", "1");
		placeResult.get(0).put("merch_app_enabled", "1");
		placeResult.get(0).put("fnb_app_enabled", "1");
		placeResult.get(0).put("transfer_benefit_supported", "phone copy");

        doReturn(generateFakeRowSet(placeResult))
                .when(jdbcTemplate)
                .queryForRowSet(queryTenantByIdAndAccountApiKey, new Object[]{1L, "accountApiKey1"});

        Optional<TenantAccountConfigMaster> tenantAccountConfigMasterOptional =
                tenantAccountConfigMasterRepositoryDefault.findTenantAccountConfigMasterById("accountApiKey1", 1L);

        assertEquals(tenantAccountConfigMasterOptional.get().getTenantAccountID(), 1);
        assertEquals(tenantAccountConfigMasterOptional.get().getEmkitAPIKey(), "accountApiKey1");
        assertNull(tenantAccountConfigMasterOptional.get().getConfigFileURL());
        assertNull(tenantAccountConfigMasterOptional.get().getThemeFileURL());
        assertNull(tenantAccountConfigMasterOptional.get().getExpressPickupStartPage());
        assertNull(tenantAccountConfigMasterOptional.get().getInSeatOrderStartPage());
        assertNull(tenantAccountConfigMasterOptional.get().getTicketingPlatform());
        assertNull(tenantAccountConfigMasterOptional.get().getTicketAccountEmailNamePattern());
        assertEquals(tenantAccountConfigMasterOptional.get().getSportsTeamId(), 0);
        assertEquals(tenantAccountConfigMasterOptional.get().getTicketmasterTenantAccountId(), 0);
        assertEquals(tenantAccountConfigMasterOptional.get().isAutoStoreTicketAccountEmail(), false);
        assertEquals(tenantAccountConfigMasterOptional.get().getxApiKey(), "");
        assertEquals(tenantAccountConfigMasterOptional.get().isLoyaltyIsEnabled(), true);
    }

    @Test
    public void findTenantAccountConfigMasterById_returnsEmptyResponseIfQueryReturnsEmpty() {

        doReturn(null)
                .when(jdbcTemplate)
                .queryForRowSet(queryTenantByIdAndAccountApiKey, new Object[]{1L, "accountApiKey1"});

        Optional<TenantAccountConfigMaster> tenantAccountConfigMasterOptional =
                tenantAccountConfigMasterRepositoryDefault.findTenantAccountConfigMasterById("accountApiKey1", 1L);

        assertFalse(tenantAccountConfigMasterOptional.isPresent());
    }

    private SqlRowSet generateFakeRowSet(List<Map<String, String>> data) {
        sqlRowSetMock = Mockito.mock(SqlRowSet.class);
        MockRow mockRowData = new MockRow(data);
        mockRows.put(sqlRowSetMock, mockRowData);

        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                //mockRows.get(invocation.getMock()).nextRow();
                return mockRows.get(invocation.getMock()).nextRow();
            }
        }).when(sqlRowSetMock).next();

        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String name = ((String) args[0]);
                return mockRows.get(invocation.getMock()).getColumn(name);
            }

            ;
        }).when(sqlRowSetMock).getString(anyString());

        doAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String id = ((String) args[0]);
                return Long.parseLong(mockRows.get(invocation.getMock()).getColumn(id));
            }
        }).when(sqlRowSetMock).getLong(anyString());

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String id = ((String) args[0]);
                String value = mockRows.get(invocation.getMock()).getColumn(id);
                if (StringUtils.isEmpty(value)) {
                    return 0;
                } else {
                    return Integer.parseInt(value);
                }
            }
        }).when(sqlRowSetMock).getInt(anyString());

        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String id = ((String) args[0]);
                return mockRows.get(invocation.getMock()).getColumn(id).equals("1");
            }

            ;
        }).when(sqlRowSetMock).getBoolean(anyString());

        return sqlRowSetMock;
    }

    static class MockRow {
        List<Map<String, String>> rowData;
        int rowIndex = -1;

        public MockRow(List<Map<String, String>> rowData) {
            this.rowData = rowData;
        }

        public boolean nextRow() {
            rowIndex++;
            if (rowData.size() == rowIndex)
                return false;
            else
                return true;
        }

        public String getColumn(String name) {
            return rowData.get(rowIndex).get(name);
        }
    }

}
