create table tv_role_permission(
   id bigint auto_increment primary key,
   role_name varchar(255) not null,
   permission_id bigint not null ,
   created_date datetime default  CURRENT_TIMESTAMP,
   last_modified_date  datetime default CURRENT_TIMESTAMP,
   created_by varchar(255),
   last_modified_by varchar(255),
   foreign key (permission_id) references tv_permission(id),
   foreign key (role_name) references jhi_authority(name)
);
