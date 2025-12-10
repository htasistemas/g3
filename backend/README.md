# Backend (G3)

O backend utiliza exclusivamente PostgreSQL. Configure as variáveis de ambiente no `.env` (veja `.env.example`):

- DB_HOST
- DB_PORT
- DB_USERNAME
- DB_PASSWORD
- DB_NAME

A aplicação espera encontrar um banco PostgreSQL acessível com essas credenciais.

## Migrations
Para aplicar as migrations utilizando o DataSource em TypeScript:

```bash
cd backend
npm run typeorm migration:run
```

## Ambiente de desenvolvimento
Após configurar as variáveis de ambiente do banco, inicie o backend em modo de desenvolvimento com:

```bash
cd backend
npm run dev
```

## Scripts SQL manuais
Há um script para criação de tabelas em PostgreSQL, caso prefira executar diretamente no gerenciador do banco:

- PostgreSQL: `backend/postgresSQL/sql-editor-create-tables.sql`
