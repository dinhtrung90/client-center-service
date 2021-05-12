create table tv_role_permission(
   id bigint auto_increment primary key,
   role_name varchar(255) not null,
   permission_id bigint not null ,
   enable_create BOOLEAN not null default false,
   enable_read BOOLEAN not null default false,
   enable_update BOOLEAN not null default false,
   enable_delete BOOLEAN not null default false,
   created_date datetime default  CURRENT_TIMESTAMP,
   last_modified_date  datetime default CURRENT_TIMESTAMP,
   created_by varchar(255),
   last_modified_by varchar(255),
   foreign key (permission_id) references tv_permission(id),
   foreign key (role_name) references jhi_authority(name)
);
