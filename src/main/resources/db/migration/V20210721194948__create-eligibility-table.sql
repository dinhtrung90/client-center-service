CREATE TABLE `tv_eligibility` (
                            `id` varchar(100) NOT NULL,
                            `full_name` varchar(50) DEFAULT NULL,
                            `email` varchar(50) DEFAULT NULL,
                            `phone` varchar(30) DEFAULT NULL,
                            `ssn`  varchar(20) DEFAULT NULL,
                            `birth_day` datetime null,
                            `created_by` varchar(50) NOT NULL,
                            `created_date` timestamp NULL DEFAULT NULL,
                            `last_modified_by` varchar(50) DEFAULT NULL,
                            `last_modified_date` timestamp NULL DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_user_email` (`email`),
                            UNIQUE KEY `ux_user_phone` (`phone`)
);
