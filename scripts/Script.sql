CREATE ROLE rocket WITH
	LOGIN
	SUPERUSER
	CREATEDB
	CREATEROLE
	INHERIT
	REPLICATION
	BYPASSRLS
	CONNECTION LIMIT -1
	PASSWORD 'E89x7.Aisx';
COMMENT ON ROLE rocket IS 'Usuário Db - rocket';

CREATE DATABASE rocket
    WITH
    OWNER = "rocket"
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

GRANT ALL ON DATABASE rocket TO "rocket" WITH GRANT OPTION;

-- Rodar logado no DB rocket
CREATE SCHEMA rocket AUTHORIZATION rocket;
GRANT ALL ON SCHEMA rocket TO rocket WITH GRANT OPTION;

create table rocket.aux_endereco 
	(id int8 not null,
	active boolean not null,
	bairro varchar(255),
	cep varchar(255),
	complemento varchar(255),
	date_created timestamp,
	gia varchar(255),
	ibge varchar(255),
	last_updated timestamp,
	localidade varchar(255),
	logradouro varchar(255),
	numero varchar(255),
	uf varchar(255),
	unidade varchar(255),
	version int8 not null,
	primary key (id)
	);

create table rocket.seg_grupo 
	(id int8 not null,
	active boolean not null,
	date_created timestamp,
	last_updated timestamp,
	name varchar(255),
	version int8 not null,
	primary key (id)
	);

create table rocket.seg_usuario 
	(id int8 not null,
	active boolean not null,
	cpf varchar(255),
	data_nascimento date,
	date_created timestamp,
	email varchar(255),
	last_updated timestamp,
	nome_completo varchar(255),
	nome_completo_mae varchar(255),
	password varchar(255),
	token varchar(255),
	validade_token date,
	version int8 not null,
	primary key (id)
	);

create table rocket.seg_usuario_grupo 
	(grupo_id int8 not null,
	usuario_id int8 not null,
	primary key (grupo_id, usuario_id)
	);

create table rocket.seg_usuario_profile 
	(id int8 not null,
	active boolean not null,
	celular varchar(255),
	date_created timestamp,
	last_updated timestamp,
	profissao varchar(255),
	telefone_fixo varchar(255),
	version int8 not null,
	usuario_endereco_id int8,
	usuario_id int8,
	primary key (id)
);

create table rocket.solicitacao 
	(id int8 not null, 
	active boolean not null, 
	date_created timestamp, 
	doc_biometria bytea not null, 
	doc_comprovante_residencia bytea not null, 
	doc_identidade bytea not null, 
	last_updated timestamp, 
	situacao int4, 
	version int8 not null, 
	usuario_id int8, 
	primary key (id)
);

alter table if exists rocket.seg_grupo add constraint UK_SEG_GRUPO_NAME unique (name);

alter table if exists rocket.seg_usuario add constraint UK_SEG_USUARIO_EMAIL unique (email);

create sequence rocket.aux_endereco_seq start 1 increment 1;

create sequence rocket.seg_grupo_seq start 1 increment 1;

create sequence rocket.seg_usuario_profile_seq start 1 increment 1;

create sequence rocket.seg_usuario_seq start 1 increment 1;

create sequence rocket.solicitacao_seq start 1 increment 1;

alter table if exists rocket.seg_usuario_grupo add constraint FK_SEG_USUARIO_GRUPO_ID foreign key (grupo_id) references rocket.seg_grupo;

alter table if exists rocket.seg_usuario_grupo add constraint FK_SEG_USUARIO_USUARIO_ID foreign key (usuario_id) references rocket.seg_usuario;

alter table if exists rocket.seg_usuario_profile add constraint FK_SEG_USUARIO_PROFILE_ENDERECO_ID foreign key (usuario_endereco_id) references rocket.aux_endereco;

alter table if exists rocket.seg_usuario_profile add constraint FK_SEG_USUARIO_PROFILE_USUARIO_ID foreign key (usuario_id) references rocket.seg_usuario;

alter table if exists rocket.solicitacao add constraint FK_SOLICITACAO_USUARIO_ID foreign key (usuario_id) references rocket.seg_usuario;

-- INSERTS
INSERT INTO rocket.seg_grupo(id, active, date_created, last_updated, name, version) 
VALUES (1, TRUE, now(), now(), 'ADMIN', 0);

INSERT INTO rocket.seg_grupo(id, active, date_created, last_updated, name, version) 
VALUES (2, TRUE, now(), now(), 'USER', 0);

INSERT INTO rocket.seg_usuario(id, active, version, date_created, last_updated, cpf, email, password, data_nascimento, nome_completo, nome_completo_mae) 
VALUES (1, TRUE, 0, now(), now(),'87455814364', 'abraao.cs@gmail.com', '$2a$10$eqFU1oxf14wBDWMvHI9SDuopqwxUFPOLt5YfOyNzrxj4qG9fkOaGa','1986-09-29', 'Administrador do Sistema', 'Mãe Administrador do Sistema');
INSERT INTO rocket.seg_usuario(id, active, version, date_created, last_updated, cpf, email, password, data_nascimento, nome_completo, nome_completo_mae) 
VALUES (2, TRUE, 0, now(), now(), '61713047861', '2401acs@gmail.com', '$2a$10$eqFU1oxf14wBDWMvHI9SDuopqwxUFPOLt5YfOyNzrxj4qG9fkOaGa', '1986-09-29', 'Usuário do Sistema', 'Mãe Usuário do Sistema');

INSERT INTO rocket.seg_usuario_grupo(usuario_id, grupo_id) VALUES (1, 1);
INSERT INTO rocket.seg_usuario_grupo(usuario_id, grupo_id) VALUES (2, 2);