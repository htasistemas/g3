#!/usr/bin/env bash
set -euo pipefail

DOMAIN="${DOMAIN:-https://g3.htasistemas.com.br}"
LOGIN_USER="${LOGIN_USER:-}"
LOGIN_PASS="${LOGIN_PASS:-}"

log() { printf "[%s] %s\n" "$(date -u +%Y-%m-%dT%H:%M:%SZ)" "$*"; }
fail() { log "ERRO: $*"; exit 1; }

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || fail "Comando nao encontrado: $1"
}

require_cmd docker
require_cmd curl

log "Verificando containers..."
docker compose ps -a

log "Checando /api/config/versao em $DOMAIN"
ver_status="$(curl -sS -o /tmp/g3-versao.json -w '%{http_code}' "$DOMAIN/api/config/versao")"
if [ "$ver_status" != "200" ]; then
  fail "Falha em /api/config/versao (HTTP $ver_status)"
fi
log "Versao OK: $(cat /tmp/g3-versao.json)"

if [ -n "$LOGIN_USER" ] && [ -n "$LOGIN_PASS" ]; then
  log "Checando login em $DOMAIN (usuario: $LOGIN_USER)"
  login_status="$(
    curl -sS -o /tmp/g3-login.json -w '%{http_code}' \
      -H 'Content-Type: application/json' \
      -d "{\"nomeUsuario\":\"$LOGIN_USER\",\"senha\":\"$LOGIN_PASS\"}" \
      "$DOMAIN/api/api/auth/login"
  )"
  if [ "$login_status" != "200" ]; then
    fail "Falha no login (HTTP $login_status) - resposta: $(cat /tmp/g3-login.json)"
  fi
  log "Login OK"
else
  log "Login nao verificado (defina LOGIN_USER e LOGIN_PASS)."
fi

log "Concluido com sucesso."
