Sistema de Controle de Vendas em Português
## Controle de reservas de um resort

#### Construindo o Projeto

1. Navegue até o diretório do projeto em seu terminal.
2. Execute `mvn clean install` para construir o projeto.

Isso criará um arquivo JAR no diretório `target`, que contém o aplicativo compilado.

#### Executando a Aplicação

1. Certifique-se de ter um banco de dados em execução (PostgreSQL).
2. Antes de executar é necessário que exista um banco e um schema padrão criados. O nome do banco padrão é `postgres` e o schema padrão é `pdv`
3. Execute `mvn spring-boot:run` para iniciar a aplicação usando o servidor embutido do Spring Boot.

O aplicativo deve estar normalmente acessível em `http://localhost:8080` (porta padrão do Spring Boot).


### URL Inicial
`http://localhost:8080/resort/`


## Requisitos:
- Cadastro de quarto (qtdMaxOcupantes, vistaMar, valor, descrição):
- Regra de negócio (se vista pro mar = true ? valor * 2);
  • Controlar entrada e saída de hóspedes:
- Cadastro do cliente/hóspede (nome, CPF, telefone, RG, idade):
- Regra de negócio: só maior de idade; tempo mínimo 2 dias;
- Controle de quarto:
- Disponíveis só quando não tiver reserva na data;
- Para facilitar nosso check-in e check-out é sempre 14:00;
- Regra de check-in: o hóspede com posse do número da reserva, o atendente muda o status da reserva para "aberto";
- Regra de check-out: o hóspede com posse do número da reserva, o atendente verifica se o status está "aberto", confere o valor total, recebe o valor e altera o status para "fechado";

STATUS DA RESERVA:
- PENDENTE (se tem reserva e não fez check-in);
- ABERTO (se tem reserva e fez check-in);
- FECHADO (se tem reserva e fez check-out);

TELAS:
• Cadastro de cliente/hóspede (inclusão, alteração, buscar todos e buscar por ID);
• Cadastro de reserva (inclusão, alteração, buscar todos e buscar por ID):
- Controle de reserva (buscar todas reservas por hóspede, buscar por ID, buscar por data de check-in);
  • Tela de consulta de quartos (buscar todos disponíveis, buscar por qtd ocupantes, buscar todos ocupados, buscar todos com vista mar e disponíveis);

