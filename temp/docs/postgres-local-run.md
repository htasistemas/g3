# Como subir o Postgres local para o backend

Este passo a passo foca em eliminar erros como `connect ECONNREFUSED 127.0.0.1:5434` ou `password authentication failed for user "g3"` ao rodar `npm run dev` ou `npm run typeorm migration:run` no backend.

- Porta esperada pelo backend local: `DB_HOST=localhost` e `DB_PORT=5434` (veja `backend/.env.example`).
- Usuário/senha padrão: `g3` / `admin` (valor do `docker-compose.yml`).

## 1) Subir apenas o Postgres com Docker

Com Docker Desktop/Docker Engine instalado (Compose v2), use **`docker compose`** (com espaço):

```powershell
cd C:\G3\g3
docker compose up -d db
docker compose ps
```

Saída esperada na coluna `Ports`: `0.0.0.0:5434->5432/tcp`.

> Se seu terminal não reconhece `docker compose`, instale/atualize o Docker Desktop ou o plugin Compose v2. A variante antiga era `docker-compose` (com hífen); o repositório já assume o comando novo.

### Alternativa rápida sem Compose

Se você não quiser instalar o plugin Compose, pode subir o mesmo container manualmente:

```powershell
docker run --name g3-db -e POSTGRES_USER=g3 -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=g3 -p 5434:5432 -d postgres:15
```

Depois, confirme que ele está rodando:

```powershell
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Ports}}"
```

## 2) Conferir credenciais dentro do container

> Use este passo se ainda receber `password authentication failed for user "g3"` (erro 28P01).

```powershell
docker exec -it g3-db psql -U postgres -d postgres -c "\du"
docker exec -it g3-db psql -U postgres -d postgres -c "ALTER USER g3 WITH PASSWORD 'admin';"
```

Se o usuário `g3` não existir:

```powershell
docker exec -it g3-db psql -U postgres -d postgres -c "CREATE USER g3 WITH PASSWORD 'admin';"
docker exec -it g3-db psql -U postgres -d postgres -c "CREATE DATABASE g3 OWNER g3;"
```

## 3) Rodar migrations e testar

Com o Postgres ativo na porta 5434:

```powershell
cd C:\G3\g3\backend
npm install
npm run typeorm migration:run
npm run dev
```

Se preferir apenas testar a conexão:

```powershell
npm run db:check
```

## 4) Erro `ECONNREFUSED`

- Certifique-se de que o container está de pé (`docker compose ps`).
- Se houver outro Postgres nativo ocupando a porta 5432, não há problema: o container usa a porta **5434** no host; garanta que o `.env` do backend aponta para `DB_PORT=5434`.
- Caso queira trocar a porta exposta, ajuste tanto o `docker-compose.yml` quanto o `.env` para o mesmo valor.
