create table tv_organization_user(
                                      id bigint auto_increment primary key,
                                      organization_id varchar(100) not null,
                                      user_id varchar(100) not null,
                                      foreign key (organization_id) references tv_organization(id),
                                      foreign key (user_id) references jhi_user(id)
);
