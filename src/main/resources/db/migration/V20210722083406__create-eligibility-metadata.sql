CREATE TABLE `tv_eligibility_metadata` (
                                  `id` varchar(100) NOT NULL,
                                  `eligibility_id` varchar(100) not null,
                                  `thumb_url` varchar(255) DEFAULT NULL,
                                  `file_name` varchar(100) DEFAULT NULL,
                                  `signature` varchar(50) DEFAULT NULL,
                                  `created_by` varchar(50) NOT NULL,
                                  `created_date` timestamp NULL DEFAULT NULL,
                                  `last_modified_by` varchar(50) DEFAULT NULL,
                                  `last_modified_date` timestamp NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  foreign key (eligibility_id) references tv_eligibility(id)


);
