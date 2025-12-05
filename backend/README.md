# Backend (G3)

Este backend está configurado exclusivamente para PostgreSQL. Nenhuma dependência ou driver de SQLite permanece no projeto.

## Configuração do banco de dados
Defina as variáveis de ambiente antes de subir a aplicação ou rodar migrations:

- `DB_HOST`
- `DB_PORT`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_DATABASE`

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

O projeto utiliza TypeORM sem qualquer fallback para SQLite.
