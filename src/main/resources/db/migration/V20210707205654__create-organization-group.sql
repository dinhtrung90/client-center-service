create table tv_organization_group(
                                       id bigint auto_increment primary key,
                                       organization_id varchar(100) not null,
                                       name varchar(255) not null,
                                       description varchar(255) not null,
                                       created_date datetime default  CURRENT_TIMESTAMP,
                                       last_modified_date  datetime default CURRENT_TIMESTAMP,
                                       created_by varchar(255),
                                       last_modified_by varchar(255),
                                       foreign key (organization_id) references tv_organization(id)
);
