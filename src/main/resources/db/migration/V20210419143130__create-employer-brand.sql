create table tv_employer_brand(
    id bigint auto_increment primary key,
    employer_id bigint not null,
    logo_url varchar(1000) default null,
    primary_color varchar(100) default null,
    display_name varchar(100),
    created_date datetime default  CURRENT_TIMESTAMP,
    last_modified_date  datetime default CURRENT_TIMESTAMP,
    created_by varchar(255),
    last_modified_by varchar(255),
    foreign key (employer_id) references tv_employer(id)
);
