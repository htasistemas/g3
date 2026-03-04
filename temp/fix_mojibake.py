from pathlib import Path
path = Path('frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.html')
text = path.read_text(encoding='utf-8')
if 'Ã' not in text:
    raise SystemExit('Sem caracteres corrompidos')
fixed = text.encode('latin1').decode('utf-8')
if fixed.count('Ã') >= text.count('Ã'):
    raise SystemExit('Conversao nao melhorou')
path.write_text(fixed, encoding='utf-8')
