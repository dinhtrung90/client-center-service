create table tv_composite_role (
    composite varchar(150) not null,
    child_role varchar(150)  not null,
    PRIMARY KEY (`composite`,`child_role`),
    foreign key (composite) references jhi_authority(name),
    foreign key (child_role) references jhi_authority(name)
);
