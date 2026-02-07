from pathlib import Path
path = Path('frontend/src/app/components/prestacao-contas/prestacao-contas.component.ts')
raw = path.read_bytes().decode('utf-8', errors='ignore')
reps = [
    ('VisÃ£o', 'Visão'),
    ('disponÃ­vel', 'disponível'),
    ('destinaÃ§Ã£o', 'destinação'),
    ('TransparÃªncia', 'Transparência'),
    ('evidÃªncias', 'evidências'),
    ('Historico', 'Histórico'),
    ('transparencia', 'transparência'),
    ('Transparencia', 'Transparência'),
    ('Nao', 'Não'),
    ('excluida', 'excluída'),
    ('possivel', 'possível'),
    ('versao', 'versão'),
    ('Ã¢â¬â', '—')
]
text = raw
for a,b in reps:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
