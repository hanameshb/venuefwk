CREATE TABLE tbl_mobile_order_config (
	mobile_order_config_id bigint(20) NOT NULL AUTO_INCREMENT,
	tenant_account_id bigint(20) NOT NULL,
	sponsor_menu_id varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
	created_at timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    updated_atd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (mobile_order_config_id),
	FOREIGN KEY (tenant_account_id) REFERENCES tbl_tenant_account_config_master(tenant_account_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

alter table tbl_mobile_order_config add column sponsor_app_id varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after sponsor_menu_id;
alter table tbl_mobile_order_config add column sponsor_venue_id varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after sponsor_app_id;

alter table tbl_mobile_order_config add column fnB_section_row_seat_label varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after sponsor_venue_id;
alter table tbl_mobile_order_config add column in_seat_selection_fnB_copy_text varchar(800) COLLATE utf8_unicode_ci DEFAULT NULL after fnB_section_row_seat_label;
alter table tbl_mobile_order_config add column merch_section_row_seat_label varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL after in_seat_selection_fnB_copy_text;
alter table tbl_mobile_order_config add column in_seat_selection_merch_copy_text varchar(800) COLLATE utf8_unicode_ci DEFAULT NULL after merch_section_row_seat_label;

alter table tbl_mobile_order_config add column primary_branding_color_font varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL after in_seat_selection_merch_copy_text;
alter table tbl_mobile_order_config add column primary_branding_color_btn varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL after primary_branding_color_font;
alter table tbl_mobile_order_config add column notes_supported tinyint(1) DEFAULT 0 NOT NULL after primary_branding_color_btn;
alter table tbl_mobile_order_config add column search_supported tinyint(1) DEFAULT 0 NOT NULL after notes_supported;

alter table tbl_mobile_order_config add column page_details TEXT COLLATE utf8_unicode_ci DEFAULT NULL after search_supported;

alter table tbl_mobile_order_config add column sms_phone_number_supported tinyint(1) DEFAULT 0 NOT NULL after search_supported;

alter table tbl_mobile_order_config add column account_menu_id varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' after page_details;
alter table tbl_mobile_order_config add column guest_checkout_supported tinyint(1) DEFAULT 0 NOT NULL after account_menu_id;
alter table tbl_mobile_order_config add column transfer_benefit_supported tinyint(1) DEFAULT 0 NOT NULL after guest_checkout_supported;
alter table tbl_mobile_order_config add column sms_phone_number_copy_text varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' after transfer_benefit_supported;

alter table tbl_mobile_order_config add column sms_phone_number_required tinyint(1) DEFAULT 0 NOT NULL after sms_phone_number_copy_text;

ALTER TABLE tbl_mobile_order_config ADD COLUMN merch_app_enabled TINYINT(1) NOT NULL DEFAULT 1 AFTER sms_phone_number_required, ADD COLUMN fnb_app_enabled TINYINT(1) NOT NULL DEFAULT 1 AFTER merch_app_enabled;