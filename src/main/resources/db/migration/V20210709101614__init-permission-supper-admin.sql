-- WARMING: truncate all permissions
drop table tv_operations;
create table tv_operations(
    name varchar(20) not null primary key
);

delete from tv_role_permission where 1=1;

insert into tv_operations (name)
values ('Read'), ('Update'), ('Delete'), ('Create');

delete from tv_permission where 1=1;


insert into tv_permission(name, created_date, description, last_modified_date, created_by, last_modified_by)
select concat('ROLE_PERMISSION_', UPPER(m.name), '_', UPPER(o.name)),
       NOW() as created_date,
       CONCAT(o.name, ' ', m.name, ' ', 'Permission') as description,
       now() as last_modified_date,
       'System' as created_by,
       'System' as last_modified_by
from tv_modules m cross join tv_operations o order by m.name;

drop table tv_operations;

-- force super admin all permissions
insert into tv_role_permission(role_name, permission_id, created_by, last_modified_by)
select 'ROLE_SUPER_ADMIN' as role_name, a.id as permission_id, 'System', 'system' from tv_permission a
