-- ============================================================
-- Script de criação do banco de dados da Locadora de Veículos
-- Banco: PostgreSQL
-- Execute no psql ou pgAdmin antes de rodar o sistema
-- ============================================================

-- Criar banco (execute separado se necessário)
-- CREATE DATABASE locadora;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL UNIQUE,
    telefone  VARCHAR(20),
    cnh       VARCHAR(20)  NOT NULL
);

-- Tabela de funcionários
CREATE TABLE IF NOT EXISTS funcionarios (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL UNIQUE,
    telefone  VARCHAR(20),
    cargo     VARCHAR(50)  NOT NULL
);

-- Tabela de veículos
-- A coluna "tipo" guarda qual subclasse é (Carro, Moto, Caminhao)
CREATE TABLE IF NOT EXISTS veiculos (
    id           SERIAL PRIMARY KEY,
    placa        VARCHAR(10)  NOT NULL UNIQUE,
    modelo       VARCHAR(50)  NOT NULL,
    marca        VARCHAR(50)  NOT NULL,
    ano          INT          NOT NULL,
    valor_diaria NUMERIC(10,2) NOT NULL,
    tipo         VARCHAR(20)  NOT NULL DEFAULT 'Carro',
    disponivel   BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Tabela de aluguéis
CREATE TABLE IF NOT EXISTS alugueis (
    id           SERIAL PRIMARY KEY,
    cliente_id   INT            NOT NULL REFERENCES clientes(id),
    veiculo_id   INT            NOT NULL REFERENCES veiculos(id),
    data_inicio  DATE           NOT NULL,
    data_fim     DATE           NOT NULL,
    dias         INT            NOT NULL,
    valor_total  NUMERIC(10,2)  NOT NULL,
    ativo        BOOLEAN        NOT NULL DEFAULT TRUE
);
