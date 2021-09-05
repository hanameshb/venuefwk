CREATE TABLE `tbl_tenant_account_config_master` (
  `tenant_account_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `emkit_api_key` varchar(20) NOT NULL,
  `config_file_url` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `theme_file_url` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `express_pickup_start_page` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `in_seat_order_start_page` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sports_team_id` bigint(20) DEFAULT NULL,
  `ticketmaster_tenant_account_id` bigint(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tenant_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

alter table tbl_tenant_account_config_master add column ticketing_platform varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after ticketmaster_tenant_account_id;

alter table tbl_tenant_account_config_master add column auto_store_ticket_account_email tinyint(1) DEFAULT 0 NOT NULL after ticketing_platform;

alter table tbl_tenant_account_config_master add column ticket_account_email_name_pattern varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after auto_store_ticket_account_email;

alter table tbl_tenant_account_config_master add column access_page_copy_text TEXT COLLATE utf8_unicode_ci DEFAULT NULL after ticket_account_email_name_pattern;

alter table tbl_tenant_account_config_master add column access_page_branding_image_url varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL after access_page_copy_text;

alter table tbl_tenant_account_config_master add column tenant_account_name varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after access_page_branding_image_url;
alter table tbl_tenant_account_config_master add column access_page_details TEXT COLLATE utf8_unicode_ci DEFAULT NULL after tenant_account_name;
alter table tbl_tenant_account_config_master add column app_id varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after access_page_details;
alter table tbl_tenant_account_config_master add column venue_id varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after app_id;

alter table tbl_tenant_account_config_master add column primary_branding_color varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL after venue_id;

alter table tbl_tenant_account_config_master add column x_api_key varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' after primary_branding_color;

alter table tbl_tenant_account_config_master add column loyalty_is_enabled tinyint(1) DEFAULT 0 NOT NULL after x_api_key;
alter table tbl_tenant_account_config_master add column loyalty_identifier varchar(300) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' after loyalty_is_enabled;