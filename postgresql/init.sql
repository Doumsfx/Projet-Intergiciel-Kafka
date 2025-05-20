drop table if exists Client;

create table if not exists Client (
	nom VARCHAR(50) primary key unique,
	heure TIMESTAMP not null 
);

create table if not exists Logs (
	id_log SERIAL primary key,
	contenu TEXT not null,
	heure TIMESTAMP not null 
);

