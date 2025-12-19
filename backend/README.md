# Backend (G3)

O backend utiliza exclusivamente PostgreSQL. Para configurar o ambiente local, copie o exemplo e ajuste o que for necessário:

```bash
cd backend
cp .env.example .env
```

Variáveis suportadas no `.env`:

- NODE_ENV
- DB_HOST
- DB_PORT (se usar o docker-compose, ajuste para 5434)
- DB_NAME
- DB_USER
- DB_PASSWORD
- DB_SSL
- DB_SCHEMA

A aplicação espera encontrar um banco PostgreSQL acessível com essas credenciais.

### Subindo o Postgres local
- Com Docker e Compose v2 instalados, suba apenas o serviço de banco:

  ```powershell
  cd C:\G3\g3
  docker compose up -d db
  docker compose ps
  ```

  A coluna `Ports` deve mostrar `0.0.0.0:5434->5432/tcp`. Use sempre `docker compose` (com espaço); se vir "comando não encontrado", atualize o Docker Desktop ou use o comando equivalente com `docker run` mostrado em [`docs/postgres-local-run.md`](../docs/postgres-local-run.md).

  Se preferir subir manualmente:

  ```bash
  docker run --name g3-postgres -e POSTGRES_DB=g3 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
  ```

### Erros comuns
- `connect ECONNREFUSED 127.0.0.1:5434`: o Postgres não está rodando na porta 5434. Suba o serviço `db` com Docker ou ajuste a porta do `.env` para a porta na qual o banco está de pé.
- `password authentication failed for user "g3"` (28P01): alinhe a senha do usuário `g3` com o `.env` seguindo o roteiro em [`../docs/postgres-28P01.md`](../docs/postgres-28P01.md).

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
npm install
npm run dev
```

## Scripts SQL manuais
Há um script para criação de tabelas em PostgreSQL, caso prefira executar diretamente no gerenciador do banco:

- PostgreSQL: `backend/postgresSQL/sql-editor-create-tables.sql`
