create table tv_modules(
   id bigint auto_increment primary key,
   name varchar(50) not null
);

INSERT INTO tv_modules (name) VALUES ('Account');
INSERT INTO tv_modules (name) VALUES ('Role');
