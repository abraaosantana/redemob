INSERT INTO rocket.seg_grupo(id, active, date_created, last_updated, name, version) VALUES (nextval('rocket.seg_grupo_seq'), TRUE, now(), now(), 'ADMIN', 0);
INSERT INTO rocket.seg_grupo(id, active, date_created, last_updated, name, version) VALUES (nextval('rocket.seg_grupo_seq'), TRUE, now(), now(), 'USER', 0);
INSERT INTO rocket.seg_usuario(id, active, version, date_created, last_updated, cpf, email, password, data_nascimento, nome_completo, nome_completo_mae) VALUES (nextval('rocket.seg_usuario_seq'), TRUE, 0, now(), now(),'87455814364', 'abraao.cs@gmail.com', '$2a$10$eqFU1oxf14wBDWMvHI9SDuopqwxUFPOLt5YfOyNzrxj4qG9fkOaGa','29/09/1980', 'Administrador do Sistema', 'Mãe Administrador do Sistema');
INSERT INTO rocket.seg_usuario(id, active, version, date_created, last_updated, cpf, email, password, data_nascimento, nome_completo, nome_completo_mae) VALUES (nextval('rocket.seg_usuario_seq'), TRUE, 0, now(), now(), '61713047861', 'contato@devweb.tec.br', '$2a$10$eqFU1oxf14wBDWMvHI9SDuopqwxUFPOLt5YfOyNzrxj4qG9fkOaGa', '29/09/1980', 'Usuário do Sistema', 'Mãe Usuário do Sistema');
INSERT INTO rocket.seg_usuario_grupo(usuario_id, grupo_id) VALUES (1, 1);
INSERT INTO rocket.seg_usuario_grupo(usuario_id, grupo_id) VALUES (1, 2);
INSERT INTO rocket.seg_usuario_grupo(usuario_id, grupo_id) VALUES (2, 2);
