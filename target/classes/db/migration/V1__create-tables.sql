-- Create enum for status
CREATE TYPE status_reserva AS ENUM ('PENDENTE', 'ABERTO', 'FECHADO');

CREATE TYPE status_quarto AS ENUM ('DISPONIVEL', 'OCUPADO');

CREATE TABLE hospede (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  telefone VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL UNIQUE,
  rg VARCHAR(11) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL
);


CREATE TABLE quarto (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL UNIQUE,
  vista_mar BOOLEAN NOT NULL,
  valor_dia NUMERIC(10,2) NOT NULL,
  qtd_max_ocupantes INTEGER NOT NULL,
  status status_quarto NOT NULL DEFAULT 'DISPONIVEL' ,
  descricao VARCHAR(100)
);


CREATE TABLE reserva (
  id SERIAL PRIMARY KEY,
  data_entrada DATE NOT NULL,
  data_saida DATE NOT NULL,
  status status_reserva NOT NULL,  -- Status como enum
  valor_total NUMERIC(10,2) NOT NULL,
  fk_hospede_id INTEGER NOT NULL REFERENCES hospede(id),
  fk_quarto_id INTEGER NOT NULL REFERENCES quarto(id)
);
