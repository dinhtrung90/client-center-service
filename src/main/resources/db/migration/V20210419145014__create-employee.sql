create table if not exists tv_employee
(
    id bigint auto_increment
        primary key,
    employee_id varchar(100) not null,
    employer_id bigint not null,
    department_id bigint not null ,
    source_id varchar(100) not null,
    first_name varchar(100) not null,
    middle_initial varchar(1) not null,
    last_name varchar(100) not null,
    email_address varchar(200) not null,
    number_phone varchar(50) not null,
    street varchar(255) not null,
    city varchar(255) not null,
    state_code varchar(100) not null,
    zip varchar(20) not null,
    department varchar(100) null,
    birth_date date not null,
    social_security_number varchar(20) not null,
    created_date datetime default  CURRENT_TIMESTAMP,
    last_modified_date  datetime default CURRENT_TIMESTAMP,
    created_by varchar(255),
    last_modified_by varchar(255),
    foreign key (employer_id) references tv_employer(id),
    foreign key (department_id) references tv_employer_department(id)
);

create index tv_employer_employee_id_index  on tv_employee(employee_id);
create index tv_employer_source_id_index  on tv_employee(source_id);
