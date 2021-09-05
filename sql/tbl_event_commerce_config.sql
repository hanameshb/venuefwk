CREATE TABLE tbl_event_commerce_config (
 id int(11) NOT NULL AUTO_INCREMENT,
 event_id int(11) NOT NULL,
 start_date_offset varchar(30) DEFAULT NULL,
 end_date_offset varchar(30) DEFAULT NULL,
 created_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
 updated_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;