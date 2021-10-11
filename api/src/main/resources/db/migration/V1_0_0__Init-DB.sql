create sequence hibernate_sequence start 1 increment 1;

create table pharmacies(
 id bigserial not null,
 name varchar(256) not null,
 medicine_link_template varchar(256),
 primary key (id)
);

create table medicines(
 id bigserial not null,
 title varchar(256) not null,
 primary key (id)
);

create table prices(
 pharmacy_id bigint,
 medicine_id bigint,
 price decimal(10,2) not null,
 external_id varchar(255) not null,
 updated_at timestamp not null default now(),
 primary key(pharmacy_id,medicine_id)
);