CREATE TABLE `tv_eligibility_present_status` (
    id bigint auto_increment primary key,
    code varchar(255) not null,
    qr_code_url varchar(255) null,
    has_present boolean default false,
    eligibility_id varchar(50) not null,
    expired_at datetime null,
    `created_by` varchar(50) NOT NULL,
    `created_date` timestamp NULL DEFAULT NULL,
    `last_modified_by` varchar(50) DEFAULT NULL,
    `last_modified_date` timestamp NULL DEFAULT NULL,
    foreign key (eligibility_id) references tv_eligibility(id)
);

create index create_code_index  on tv_eligibility_present_status(code);
create index create_has_present_index  on tv_eligibility_present_status(has_present);
