# Backend (G3)

O backend pode usar PostgreSQL (padrão) ou SQLite local para desenvolvimento. A escolha é feita via variáveis de ambiente.

## Configuração do banco de dados
As variáveis abaixo são lidas pelo TypeORM (ver `src/data-source.ts`). Caso não sejam informadas, usamos o Postgres remoto em `72.60.156.202` com usuário `g3`, senha `admin` e banco `g3`.

- `DB_TYPE` (`postgres` | `mysql` | `mariadb` | `sqlite`) – padrão: `postgres`
- `DB_HOST` – padrão: `72.60.156.202`
- `DB_PORT` – padrão: `5432` (ou `3306` para MySQL/MariaDB)
- `DB_USER` – padrão: `g3`
- `DB_PASSWORD` – padrão: `admin`
- `DB_NAME` – para SQLite aceita caminho relativo/absoluto do arquivo

Opcionalmente você pode usar `DATABASE_URL` para fornecer a string completa de conexão.

## Migrations
Para aplicar as migrations utilizando o DataSource em TypeScript:

```bash
cd backend
npm run migration:run
```

As migrations foram ajustadas para serem idempotentes em PostgreSQL (sem criação de tabelas temporárias ou colunas duplicadas).

## Ambiente de desenvolvimento
Após configurar as variáveis de ambiente do banco, inicie o backend em modo de desenvolvimento com:

```bash
cd backend
npm run dev
```

O projeto utiliza TypeORM e pode cair para SQLite local se `DB_TYPE=sqlite` for definido.
