# G3 - Backend Java

Este repositorio esta em migracao gradual de Node para Java.

## Stack
- Java 21
- Spring Boot 3.3.x
- PostgreSQL

## Estrutura do Backend
Local: `backend/`

Camadas principais (arquitetura limpa):
- controller (resource)
- service / serviceimpl
- repository / repositoryimpl
- domain
- dto

## Banco de Dados
- Banco local: `g3`
- Usuario: `postgres`
- Senha: `admin`
- Nomes em portugues para tabelas e colunas
- Chave primaria sempre `id` sequencial
- Relacionamentos sempre por FK

## init.db (criacao idempotente)
Ao subir o backend, o sistema executa o arquivo `backend/src/main/resources/init.db` para verificar/criar tabelas.
Esse arquivo deve ser idempotente.

## Subir o backend
1) `cd backend`
2) `mvn spring-boot:run`

## Configuracao de email (Docker)
- O backend le as variaveis de ambiente `APP_EMAIL_*` e `MAIL_*`.
- Para uso com Docker, crie o arquivo `docker/secrets/configuracao servidor email.txt`
  usando o modelo `docker/secrets/configuracao servidor email.txt.example`.
- Esse arquivo **nao deve** ser versionado.

## Endpoints iniciais
- `POST /api/unidades-assistenciais`
- `GET /api/unidades-assistenciais`

## Painel/Chamada de Senhas (integrado)

### Rotas (frontend)
- Operador: `/senhas/chamar`
- Painel publico: `/senhas/painel` (pode receber `?unidadeId=...`)

### Endpoints (backend)
- `POST /api/senhas/emitir`
- `POST /api/senhas/chamar`
- `POST /api/senhas/finalizar`
- `GET /api/senhas/aguardando?unidadeId=...`
- `GET /api/senhas/painel?unidadeId=...&limite=...`
- `GET /api/senhas/historico?unidadeId=...&limite=...`
- `GET /api/senhas/atual?unidadeId=...`

### Realtime
- WebSocket endpoint: `/ws`
- Topico: `/topic/senhas`
- Evento: `CHAMADA_SENHA`

### Permissoes
- Segue o mesmo controle de acesso do G3 (auth guard do frontend). O painel pode ser acessado sem login.
