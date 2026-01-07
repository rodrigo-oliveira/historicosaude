-- V2__inserir_admin_teste.sql
-- Hash BCrypt da senha "123456" (gerado com BCryptPasswordEncoder)
INSERT INTO usuarios (cpf, nome, email, login, senha, perfil)
VALUES ('12345678901', 'Administrador Teste', 'admin@historicosaude.com', 'admin', '$2a$12$rhIaPLgkcSP4EWCW6cDRj.AEWlNUHfa.TqYsBvf7fzEgx81lP8W1W', 'ADMIN')
ON CONFLICT (login) DO UPDATE SET senha = '$2a$12$rhIaPLgkcSP4EWCW6cDRj.AEWlNUHfa.TqYsBvf7fzEgx81lP8W1W';

