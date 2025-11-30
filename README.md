# G3 Angular + Node

Este repositório contém o front-end em Angular e o back-end em Node/Express para autenticação básica e inicialização automática do banco.

## Backend
- Localização: `backend/`
- Comandos:
  - `npm install`
  - `npm run dev`
- Configuração via `.env` (exemplo em `backend/.env.example`). Ao iniciar, a API cria as tabelas necessárias e garante o usuário `admin/123`.
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
  - `backend` exposto em `3000` (usa variáveis de ambiente para apontar para o banco em `72.60.156.202` por padrão).
  - `frontend` exposto em `4200`.

## Git
- O repositório local deve ter o remoto configurado como `origin` apontando para `https://github.com/htasistemas/g3.git`.
- Caso o `git pull` retorne erro de rastreamento, rode `git checkout -B main origin/main` para garantir que o branch `main` esteja rastreando `origin/main`.
