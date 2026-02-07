from pathlib import Path
path = Path('frontend/src/app/components/prestacao-contas/prestacao-contas.component.ts')
text = path.read_text(encoding='utf-8', errors='ignore')
reps = [
    ('VisÃÂ£o', 'Visão'),
    ('disponÃÂ­vel', 'disponível'),
    ('destinaÃÂ§ÃÂ£o', 'destinação'),
    ('TransparÃÂªncia', 'Transparência'),
    ('evidÃÂªncias', 'evidências')
]
for a,b in reps:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
