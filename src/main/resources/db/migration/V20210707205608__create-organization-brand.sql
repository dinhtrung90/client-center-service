create table tv_organization_brand(
                                  id bigint auto_increment primary key,
                                  organization_id bigint not null ,
                                  logo_url varchar(1000) default null,
                                  primary_color varchar(100) default null,
                                  background_color varchar(100) default null,
                                  display_name varchar(100),
                                  created_date datetime default  CURRENT_TIMESTAMP,
                                  last_modified_date  datetime default CURRENT_TIMESTAMP,
                                  created_by varchar(255),
                                  last_modified_by varchar(255),
                                  foreign key (organization_id) references tv_organization(id)
);
