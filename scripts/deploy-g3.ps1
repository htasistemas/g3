# Deploy G3 com tunel Cloudflare usando config.yml
Continue = 'Stop'

Write-Host 'Iniciando deploy do G3...' -ForegroundColor Cyan

if (!(Test-Path -Path ".\docker\cloudflared\config.yml")) {
  Write-Error 'Arquivo docker/cloudflared/config.yml nao encontrado.'
  exit 1
}

# Sempre recria containers garantindo o entrypoint do tunel
Write-Host 'Subindo stack via docker compose...' -ForegroundColor Cyan

# Compatibilidade: docker compose (plugin) ou docker-compose (binario)
 = 
if (Get-Command docker -ErrorAction SilentlyContinue) {
  try {
    docker compose version | Out-Null
     = 'docker compose'
  } catch {
     = 
  }
}
if (-not  -and (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
   = 'docker-compose'
}

if (-not ) {
  Write-Error 'Docker Compose nao encontrado.'
  exit 1
}

Invoke-Expression " down"
Invoke-Expression " up -d --build"

Write-Host 'Deploy finalizado.' -ForegroundColor Green
