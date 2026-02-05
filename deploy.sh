#!/usr/bin/env bash
set -euo pipefail

APP_COMPOSE="/home/srv/g3/docker-compose.yml"
TUNNEL_COMPOSE="/home/srv/g3/docker-compose.tunnel.yml"

log() { printf "[%s] %s\n" "$(date -u +%Y-%m-%dT%H:%M:%SZ)" "$*"; }

if [ -f "$TUNNEL_COMPOSE" ]; then
  log "ERROR: $TUNNEL_COMPOSE existe. Este arquivo nao deve ser usado."
  log "Remova-o para evitar queda do sistema."
  exit 1
fi

log "Deploy app stack"
docker compose -f "$APP_COMPOSE" up -d --build

log "Done"
