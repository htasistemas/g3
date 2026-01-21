# G3 Chamadas - Painel de Chamada de Beneficiario

Projeto completo para chamada de beneficiarios com tela do operador e painel publico, com atualizacao em tempo real via WebSocket STOMP.

## Requisitos
- Java 17+
- Node.js 18+
- Docker (opcional para banco e backend)

## Estrutura
```
g3-chamadas/
  backend/
  frontend/
  docker-compose.yml
```

## Como rodar (Docker)
1. Suba o banco e o backend:
```
docker compose up -d --build
```
2. O backend estara em `http://localhost:8085`.

## Como rodar (local)
### Banco
```
docker compose up -d postgres
```

### Backend
```
cd backend
mvn spring-boot:run
```

### Frontend
```
cd frontend
npm install
npm run start
```

## Rotas
- Operador: `http://localhost:4201/#/operador/chamadas`
- Painel publico: `http://localhost:4201/#/painel/senha`

## Endpoints principais
- GET `/api/beneficiarios`
- GET `/api/fila/aguardando`
- POST `/api/fila/entrar`
- POST `/api/chamadas/chamar`
- GET `/api/chamadas/ultima`
- GET `/api/chamadas/ultimas?limite=10`

## Inserir beneficiarios (exemplo)
Use o banco PostgreSQL para cadastrar beneficiarios, sem seed automatico:
```
INSERT INTO beneficiario (nome_beneficiario, documento, ativo)
VALUES ('Maria Souza', '000.000.000-00', true);
```
Em seguida, use o endpoint `/api/fila/entrar` para colocar na fila.

## WebSocket
- Endpoint: `/ws`
- Topico: `/topic/chamadas`
- Evento: `CHAMADA_BENEFICIARIO`

## Observacoes
- O painel publico exibe apenas nome e local.
- Toda persistencia e feita no PostgreSQL via Flyway.
