# Guia definitivo: erro Postgres 28P01 (senha falhou para usuário "g3")

Este roteiro em português ajuda a provar qual instância do Postgres está respondendo e corrige a senha do usuário `g3` sem achismos. Os comandos são para Windows (PowerShell ou Prompt) com Docker opcional.

> **Contexto do projeto**
>
> - O `docker-compose.yml` expõe o Postgres da stack em `5434:5432` (porta externa 5434, interna 5432).
> - O `backend/.env.example` aponta para `DB_HOST=localhost` e `DB_PORT=5434`. Se você usar outra porta, ajuste o `.env` e/ou o `docker-compose.yml` em conjunto.

## Trilha A — Postgres rodando local (serviço do Windows) em `localhost:5432`
1. **Descobrir o processo/serviço na porta 5432:**
   ```bat
   netstat -ano | findstr :5432
   tasklist /FI "PID eq <PID>"
   ```
2. **Testar login com o mesmo usuário/banco:**
   ```bat
   psql -h localhost -p 5432 -U g3 -d g3
   ```
   - Se o `psql` retornar `28P01`, a senha armazenada nesse Postgres **não** é a do `.env`.
3. **Corrigir senha do usuário `g3` (sem recriar tudo):**
   - Conectar como admin (usuário `postgres`):
     ```bat
     psql -h localhost -p 5432 -U postgres -d postgres
     ```
   - Listar usuários e aplicar correção:
     ```sql
     SELECT usename FROM pg_user;
     ALTER USER g3 WITH PASSWORD '<SENHA_EXATA_DO_.env>';
     GRANT ALL PRIVILEGES ON DATABASE g3 TO g3;
     ALTER DATABASE g3 OWNER TO g3;
     ```
   - Se o usuário não existir:
     ```sql
     CREATE USER g3 WITH PASSWORD '<SENHA_EXATA_DO_.env>';
     CREATE DATABASE g3 OWNER g3;
     ```

## Trilha B — Postgres em Docker (mais provável na stack do projeto)
1. **Provar se há container Postgres e qual porta expõe:**
   ```bat
   docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Ports}}"
   ```
2. **Se houver Postgres, inspecionar usuários dentro do container:**
   ```bat
   docker exec -it <CONTAINER> psql -U postgres -d postgres -c "\\du"
   ```
3. **Alinhar a senha do usuário `g3` com o `.env`:**
   ```bat
   docker exec -it <CONTAINER> psql -U postgres -d postgres -c "ALTER USER g3 WITH PASSWORD '<SENHA_EXATA_DO_.env>';"
   ```
4. **Sobre volumes que preservam senha antiga:**
   - Se o container foi criado com outra senha e você mudou `POSTGRES_PASSWORD`, o volume mantém a senha antiga.
   - Preferencial: altere a senha via `ALTER USER` (passo anterior) para não perder dados.
   - Se aceitar perder dados, recrie o volume:
     ```bat
     docker compose down -v
     docker compose up -d
     ```
5. **Conferir variáveis no `docker-compose.yml`** (já alinhadas no repo):
   - `POSTGRES_USER=g3`
   - `POSTGRES_PASSWORD` deve ser a mesma do `.env`
   - `POSTGRES_DB=g3`
   - Porta mapeada `5434:5432` → use `DB_PORT=5434` no backend local.

## Trilha C — Conectando no "Postgres errado" no `localhost`
1. **Sintoma clássico:** existe um Postgres nativo em `5432` e o Postgres do Docker em outra porta (ex.: `5434`).
2. **Forçar a prova alterando a porta do Docker:**
   - No `docker-compose.yml`, troque para:
     ```yaml
     ports:
       - "5433:5432"
     ```
   - Ajuste o `.env` do backend para `DB_PORT=5433`.
3. **Suba e teste:** se passar a funcionar nessa porta nova, o Postgres de `5432` era outro serviço.

## Checklist rápido para o backend do G3
- Garanta que `.env` do backend usa `DB_HOST=localhost` e `DB_PORT=5434` (ou a porta que você mapeou no Docker).
- Se rodar `docker compose up -d`, confirme que a porta externa escolhida não conflita com outro Postgres ativo.
- Sempre confirme a senha efetiva do usuário `g3` dentro da instância com `ALTER USER` antes de subir o backend.
