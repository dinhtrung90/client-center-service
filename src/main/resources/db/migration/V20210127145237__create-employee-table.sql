create table if not exists vts_employees
(
    id bigint auto_increment
        primary key,
        employee_id varchar(100) not null,
    source_id varchar(100) not null,
    first_name varchar(100) not null,
    middle_initial varchar(1) not null,
    last_name varchar(100) not null,
    email_address varchar(200) not null,
    phone_number varchar(50) not null,
    street varchar(255) not null,
    city varchar(255) not null,
    state varchar(100) not null,
    zip_code varchar(20) not null,
    birth_date date not null,
    social_security_number varchar(20) not null
);

