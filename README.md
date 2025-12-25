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

## Endpoints iniciais
- `POST /api/unidades-assistenciais`
- `GET /api/unidades-assistenciais`
