#!/usr/bin/env bash
set -euo pipefail

cd /home/srv/g3

if [ ! -f "docker/cloudflared/config.yml" ]; then
  echo "Arquivo docker/cloudflared/config.yml nao encontrado."
  exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker nao encontrado."
  exit 1
fi

if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE="docker-compose"
else
  echo "Docker Compose nao encontrado."
  exit 1
fi

 up -d --build backend frontend

echo "Aguardando backend..."
for i in {1..20}; do
  if curl -fsS http://localhost:8080/health >/dev/null; then
    echo "Backend respondeu /health"
    break
  fi
  sleep 3
 done

if ! curl -fsS http://localhost:8080/health >/dev/null; then
  echo "Erro: backend nao respondeu /health"
  exit 1
fi

if ! curl -fsS http://localhost/ >/dev/null; then
  echo "Erro: frontend nao respondeu"
  exit 1
fi

echo "Deploy finalizado."
