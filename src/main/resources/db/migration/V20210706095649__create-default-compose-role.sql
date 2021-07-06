insert into tv_composite_role (composite, child_role) VALUES ('ROLE_ADMIN', 'ROLE_USER');
insert into tv_composite_role (composite, child_role) VALUES ('ROLE_ADMIN', 'ROLE_SUPERVISOR');

insert into tv_composite_role (composite, child_role) VALUES ('ROLE_SUPER_ADMIN', 'ROLE_USER');
insert into tv_composite_role (composite, child_role) VALUES ('ROLE_SUPER_ADMIN', 'ROLE_ADMIN');
insert into tv_composite_role (composite, child_role) VALUES ('ROLE_SUPER_ADMIN', 'ROLE_SUPERVISOR');

insert into tv_composite_role (composite, child_role) VALUES ('ROLE_SUPERVISOR', 'ROLE_USER');
