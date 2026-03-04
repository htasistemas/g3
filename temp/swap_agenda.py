from pathlib import Path
path = Path(r'frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.html')
text = path.read_text(encoding='utf-8')
marker1 = '<div class= agenda-panel>'
marker2 = '<div class=agenda-panel [formGroup]=agendamentoForm>'
marker_after = '<div class=list-card>'
start1 = text.find(marker1)
start2 = text.find(marker2, start1)
end2 = text.find(marker_after, start2)
if start1 == -1 or start2 == -1 or end2 == -1:
    raise SystemExit('Blocos nao encontrados')
block1 = text[start1:start2]
block2 = text[start2:end2]
new_text = text[:start1] + block2 + block1 + text[end2:]
path.write_text(new_text, encoding='utf-8')
