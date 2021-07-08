create table tv_organization(
                            id bigint auto_increment primary key,
                            organization_uuid varchar(100) unique not null,
                            name varchar(255) not null,
                            display_name varchar(255) null,
                            description varchar(1000) null,
                            email varchar(150) not null,
                            phone varchar(150) not null,
                            created_date datetime default  CURRENT_TIMESTAMP,
                            last_modified_date  datetime default CURRENT_TIMESTAMP,
                            created_by varchar(255),
                            last_modified_by varchar(255)
);

create index tv_organization_uuid_index  on tv_organization(organization_uuid);
