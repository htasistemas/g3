#!/usr/bin/env bash
set -euo pipefail

APP_COMPOSE="/home/srv/g3/docker-compose.yml"
TUNNEL_COMPOSE="/home/srv/g3/docker-compose.tunnel.yml"

log() { printf "[%s] %s\n" "$(date -u +%Y-%m-%dT%H:%M:%SZ)" "$*"; }

if rg -n "^[[:space:]]*tunnel:" "$APP_COMPOSE" >/dev/null 2>&1; then
  log "ERROR: tunnel service found in $APP_COMPOSE. This would cause downtime."
  log "Remove it and keep the tunnel only in $TUNNEL_COMPOSE."
  exit 1
fi

log "Deploy app stack"
docker compose -f "$APP_COMPOSE" up -d --build

log "Ensure tunnel stack is up"
docker compose -f "$TUNNEL_COMPOSE" up -d

log "Done"
