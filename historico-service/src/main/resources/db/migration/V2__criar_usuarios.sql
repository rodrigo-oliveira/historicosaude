-- V2__criar_usuarios.sql
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL,
    data_ultima_alteracao TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_login ON usuarios(login);
CREATE INDEX idx_cpf ON usuarios(cpf);
