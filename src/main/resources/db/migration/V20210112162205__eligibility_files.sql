DROP TABLE IF EXISTS `vts_eligibility`;
CREATE TABLE `vts_eligibility` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `created_by` varchar(100) NULL,
                             `created_date` datetime not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
                             `last_modified_by` varchar(1) NULL,
                             `last_modified_date` datetime not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
                             `file_name` varchar(255) NULL,
                             `ref_id` varchar(200) NOT NULL,
                             `file_url` text,
                             PRIMARY KEY (`id`)
);
