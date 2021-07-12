create table tv_organization(
                            id varchar(100) primary key,
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

create index tv_organization_name_index  on tv_organization(name);
