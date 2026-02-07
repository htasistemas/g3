from pathlib import Path
path = Path('frontend/src/app/components/prestacao-contas/prestacao-contas.component.ts')
text = path.read_bytes().decode('latin-1')
reps = [
    ('VisÃ£o','Visão'),
    ('disponÃ­vel','disponível'),
    ('destinaÃ§Ã£o','destinação'),
    ('TransparÃªncia','Transparência'),
    ('evidÃªncias','evidências'),
    ('Historico','Histórico'),
    ('transparencia','transparência'),
    ('Transparencia','Transparência'),
    ('Nao','Não'),
    ('excluida','excluída'),
    ('possivel','possível'),
    ('versao','versão'),
    ('â€”','—')
]
for a,b in reps:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
