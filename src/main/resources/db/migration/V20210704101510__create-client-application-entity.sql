create table tv_client_app(
                                id varchar(50) primary key,
                                app_name varchar(50) unique not null,
                                app_desc varchar(255) null,
                                created_date datetime default  CURRENT_TIMESTAMP,
                                last_modified_date  datetime default CURRENT_TIMESTAMP,
                                created_by varchar(255),
                                last_modified_by varchar(255)
);

create index tv_client_app_index  on tv_client_app(app_name);
