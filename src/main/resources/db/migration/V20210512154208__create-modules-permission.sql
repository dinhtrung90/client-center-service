create table tv_permission(
                              id bigint auto_increment primary key,
                              name varchar(255) not null,
                              created_date datetime default  CURRENT_TIMESTAMP,
                              description varchar(255),
                              last_modified_date  datetime default CURRENT_TIMESTAMP,
                              created_by varchar(255),
                              last_modified_by varchar(255)
);
