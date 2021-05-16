create table tv_operation(
   id bigint auto_increment primary key,
   operation varchar(255)
);

insert into tv_operation(operation) values ('CREATE');
insert into tv_operation(operation) values ('READ');
insert into tv_operation(operation) values ('UPDATE');
insert into tv_operation(operation) values ('DELETE');
