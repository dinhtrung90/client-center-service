create table tv_organization(
                            id bigint auto_increment primary key,
                            organization_key varchar(100) unique not null,
                            name varchar(255) not null,
                            address varchar(255) null,
                            street varchar(255),
                            city varchar(255) null,
                            county varchar(255) null,
                            email varchar(150) not null,
                            phone varchar(150) not null,
                            longitude varchar(100),
                            latitude varchar(100),
                            created_date datetime default  CURRENT_TIMESTAMP,
                            last_modified_date  datetime default CURRENT_TIMESTAMP,
                            created_by varchar(255),
                            last_modified_by varchar(255)
);

create index tv_organization_id_index  on tv_organization(organization_key);
