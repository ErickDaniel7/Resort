CREATE TABLE hospede (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  telefone VARCHAR(255) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE, -- Added UNIQUE constraint
  rg VARCHAR(255) NOT NULL UNIQUE, -- Added UNIQUE constraint
  data_nascimento DATE NOT NULL
);


CREATE TYPE status_quarto AS ENUM ('DISPONIVEL', 'OCUPADO', 'IMPEDIDO');

CREATE TABLE quarto (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL UNIQUE,  -- Unique name for the room
  vista_mar BOOLEAN NOT NULL,
  valor_dia NUMERIC(10,2) NOT NULL,  -- Daily rate with precision
  qtd_max_ocupantes INTEGER NOT NULL,
  status status_quarto  -- Reference to status enum (created separately)
  descricao VARCHAR(100)
);

-- Create enum for status
CREATE TYPE status_reserva AS ENUM ('PENDENTE', 'ABERTO', 'FECHADO');

CREATE TABLE reserva (
  id SERIAL PRIMARY KEY,
  data_entrada DATE NOT NULL,
  data_saida DATE NOT NULL,
  status status_reserva NOT NULL,  -- Status como enum
  valor_total NUMERIC(10,2) NOT NULL,
  fk_hospede_id INTEGER NOT NULL REFERENCES hospede(id),
  fk_quarto_id INTEGER NOT NULL REFERENCES quarto(id)
);


