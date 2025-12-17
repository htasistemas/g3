# Backend (G3)

O backend utiliza exclusivamente PostgreSQL. Configure as variáveis de ambiente no `.env` (veja `.env.example`):

- DB_HOST
- DB_PORT (para acessar o Postgres do docker-compose a partir da máquina host use 5434)
- DB_USERNAME
- DB_PASSWORD
- DB_NAME

A aplicação espera encontrar um banco PostgreSQL acessível com essas credenciais.

### Subindo o Postgres local
- Com Docker e Compose v2 instalados, suba apenas o serviço de banco:

  ```powershell
  cd C:\G3\g3
  docker compose up -d db
  docker compose ps
  ```

  A coluna `Ports` deve mostrar `0.0.0.0:5434->5432/tcp`. Use sempre `docker compose` (com espaço); se vir "comando não encontrado", atualize o Docker Desktop ou use o comando equivalente com `docker run` mostrado em [`docs/postgres-local-run.md`](../docs/postgres-local-run.md).

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
npm run dev
```

## Scripts SQL manuais
Há um script para criação de tabelas em PostgreSQL, caso prefira executar diretamente no gerenciador do banco:

- PostgreSQL: `backend/postgresSQL/sql-editor-create-tables.sql`
