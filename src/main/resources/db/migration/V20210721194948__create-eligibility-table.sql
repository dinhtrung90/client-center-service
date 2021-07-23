CREATE TABLE `tv_eligibility` (
                            `id` varchar(100) NOT NULL,
                            `full_name` varchar(50) DEFAULT NULL,
                            `email` varchar(50) DEFAULT NULL,
                            `phone` varchar(30) DEFAULT NULL,
                            `employee_id` varchar(100) NOT NULL,
                            `company` varchar(100) NOT NULL,
                            `other_company` varchar(200) NULL ,
                            `ssn`  varchar(20) DEFAULT NULL,
                            `birth_day` datetime null,
                            `gender` varchar(20)DEFAULT NULL,
                            `full_address` varchar(200) DEFAULT NULL,
                            `created_by` varchar(50) NOT NULL,
                            `created_date` timestamp NULL DEFAULT NULL,
                            `last_modified_by` varchar(50) DEFAULT NULL,
                            `last_modified_date` timestamp NULL DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_user_email` (`email`),
                            UNIQUE KEY `ux_user_phone` (`phone`),
                            UNIQUE KEY `ux_employee_id` (`employee_id`)
);

create index create_employee_id_index  on tv_eligibility(employee_id);
create index create_phone_index  on tv_eligibility(phone);
