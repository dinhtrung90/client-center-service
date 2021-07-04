create table tv_client_authority(
                              client_id varchar(50) not null,
                              authority_name varchar(50) not null,
                              primary key (client_id, authority_name)
);

ALTER TABLE tv_client_authority
    ADD CONSTRAINT fk_client_authority
        FOREIGN KEY (client_id) REFERENCES tv_client_app(id);
