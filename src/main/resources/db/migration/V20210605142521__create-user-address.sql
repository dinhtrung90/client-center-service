create table tv_user_address(
                                id bigint auto_increment primary key,
                                user_id varchar(100) not null,
                                address_line_1 varchar(255) null,
                                address_line_2 varchar(255) null,
                                city varchar(50) null,
                                country varchar(50) null,
                                zip_code varchar(50) null,
                                created_date datetime default  CURRENT_TIMESTAMP,
                                last_modified_date  datetime default CURRENT_TIMESTAMP,
                                created_by varchar(255),
                                last_modified_by varchar(255),
                                latitude varchar(100),
                                longitude varchar(100),
                                foreign key (user_id) references jhi_user(id)
);

create index tv_user_address_country_index  on tv_user_address(country);
create index tv_user_address_city_index  on tv_user_address(city);
create index tv_user_address_zip_code_index  on tv_user_address(zip_code);
