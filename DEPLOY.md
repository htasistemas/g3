# Deploy G3

Use somente o compose principal para evitar instabilidade do tunnel.

Comandos recomendados:

```bash
cd /home/srv/g3
docker compose up -d --build --remove-orphans
```

Checklist rapido:
- `docker compose ps`
- O backend expõe a porta localmente no host (`127.0.0.1:8080:8080`) apenas para o healthcheck do runner.
- `curl -fsS http://localhost:8080/health`
- `curl -fsS http://localhost/`

Observacao:
- Nao use `docker-compose.tunnel.yml` (arquivo removido). O tunnel correto esta no `docker-compose.yml`.
