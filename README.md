# G3 Angular + Node

Este repositório contém o front-end em Angular e o back-end em Node/Express para autenticação básica e inicialização automática
do banco.

## Backend
- Localização: `backend/`
- Comandos:
  - `npm install`
  - `npm run dev`
  - Configuração via `.env` (exemplo em `backend/.env.example`). O backend utiliza PostgreSQL e já vem
    configurado para apontar para um banco local em `localhost:5434` (porta externa do serviço `db` no
    `docker-compose`).
- Variáveis principais: `DB_HOST`, `DB_PORT`, `DB_USERNAME`, `DB_PASSWORD`, `DB_NAME`.
  Se nada for informado, o backend tenta `g3/admin` no host configurado (por padrão `localhost`).
- Ao iniciar, a API cria as tabelas necessárias e garante o usuário `admin/123`.
- Padrão de modelagem: chaves primárias devem ser inteiros autoincrementais (`@PrimaryGeneratedColumn()`), nunca UUID.
- Endpoints principais:
  - `POST /api/auth/login`
  - `GET /api/health`

## Frontend
- Localização: `frontend/`
- Comandos:
  - `npm install`
  - `npm run dev`
- Angular 21 com Tailwind. A rota `/login` autentica e abre o painel com menu lateral pronto para novas telas.

## Docker
- Suba tudo com `docker compose up --build`.
- Serviços:
  - `db` (PostgreSQL) exposto em `5434` (interno `5432`).
  - `backend` exposto em `3000` (já apontando para o serviço `db`).
  - `frontend` exposto em `4200`.

### Erro Postgres 28P01 (senha do usuário `g3` falhou)

Se ao subir o backend você receber `authentication type password failed for user "g3" (28P01)`, siga o
guia detalhado em [`docs/postgres-28P01.md`](docs/postgres-28P01.md) para provar qual instância do
Postgres está respondendo, alinhar a senha do usuário `g3` e, se necessário, ajustar portas e volumes do
Docker.
