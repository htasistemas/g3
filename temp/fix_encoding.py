from pathlib import Path
path = Path('frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.html')
text = path.read_text(encoding='cp1252')
path.write_text(text, encoding='utf-8')
