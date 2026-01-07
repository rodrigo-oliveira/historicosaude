-- V1__criar_usuarios_e_consultas.sql
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

CREATE TABLE IF NOT EXISTS consultas (
    id BIGSERIAL PRIMARY KEY,
    cpf_paciente VARCHAR(11) NOT NULL,
    nome_paciente VARCHAR(255) NOT NULL,
    email_paciente VARCHAR(255) NOT NULL,
    data_consulta TIMESTAMP NOT NULL,
    medico VARCHAR(255) NOT NULL,
    especialidade VARCHAR(255),
    motivo VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'AGENDADA',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
