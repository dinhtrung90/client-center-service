alter table jhi_authority add column created_date datetime default  CURRENT_TIMESTAMP;
alter table jhi_authority add column last_modified_date  datetime default CURRENT_TIMESTAMP;
alter table jhi_authority add column  created_by varchar(255);
alter table jhi_authority add column  last_modified_by varchar(255);
