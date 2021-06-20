create table tv_user_profile(
    user_id varchar(100) unique not null primary key,
    gender varchar(50) null,
    avatar_url varchar(255) null,
    phone varchar(50) not null,
    home_phone varchar(50) null,
    birth_date datetime null,
    longitude varchar(100),
    latitude varchar(100),
    created_date datetime default  CURRENT_TIMESTAMP,
    last_modified_date  datetime default CURRENT_TIMESTAMP,
    created_by varchar(255),
    last_modified_by varchar(255),
    constraint `fk_user_profile_id` foreign key (`user_id`) references jhi_user (`id`)
);

create index tv_user_profile_phone_index  on tv_user_profile(phone);

