#!/bin/sh
set -e

if [ -n "$API_BASE_URL" ]; then
  cat <<CONFIG >/usr/share/nginx/html/env-config.js
(function (window) {
  window.__env = window.__env || {};
  window.__env.apiUrl = "$API_BASE_URL";
})(this);
CONFIG
fi

exec nginx -g 'daemon off;'
