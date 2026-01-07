-- V1__criar_consultas.sql
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

CREATE INDEX idx_cpf_paciente ON consultas(cpf_paciente);
CREATE INDEX idx_medico ON consultas(medico);
CREATE INDEX idx_status ON consultas(status);
