create table tv_permission_operation(
  id bigint auto_increment primary key,
  operation_id bigint not null,
  role_permission_id bigint not null
);
