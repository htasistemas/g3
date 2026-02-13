#!/bin/sh
set -e

API_BASE_URL="${API_BASE_URL:-http://localhost:8080}"
export API_BASE_URL

CONFIG_FILE="/usr/share/nginx/html/assets/config.json"
if [ -f "$CONFIG_FILE" ]; then
  envsubst < "$CONFIG_FILE" > "${CONFIG_FILE}.tmp"
  mv "${CONFIG_FILE}.tmp" "$CONFIG_FILE"
fi

find /usr/share/nginx/html -type f -name "*.js" -exec \
  sed -i "s|http://localhost:8080|${API_BASE_URL}|g" {} +

exec nginx -g 'daemon off;'
