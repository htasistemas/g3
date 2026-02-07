from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\controle-veiculos\controle-veiculos.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
# remove header block
import re
text = re.sub(r"\s*<header class=\"page-title[\s\S]*?</header>\s*", "\n", text, count=1)
# replace section class
text = text.replace('<section class="controle-veiculos">', '<section class="cadastro">')
# inject cadastro layout around tab-shell
marker = '<div class="tab-shell">'
if marker in text:
    text = text.replace(marker, '<div class="cadastro-layout">\n    <aside class="cadastro-menu">\n      <div class="tab-shell">\n        <p class="tab-shell__title tab-shell__title--destaque">\n          <fa-icon [icon]="faCarSide" class="tab-shell__icon"></fa-icon>\n          Controle de Veículos\n        </p>', 1)
    # close aside and open content after tab-shell block
    text = text.replace('</div>\n\n    <form *ngIf="abaAtiva === \'cadastro\'"', '</div>\n    </aside>\n\n    <div class="cadastro-conteudo">\n    <form *ngIf="abaAtiva === \'cadastro\'"', 1)
    # close layout before end of section
    text = text.replace('</section>', '    </div>\n  </div>\n</section>', 1)
# remove muted helper lines under highlights
text = text.replace('<p class="muted">Mantenha a frota organizada com dados completos.</p>', '')
text = text.replace('<p class="muted">Registre cada viagem e acompanhe o consumo.</p>', '')
text = text.replace('<p class="muted">Defina quem esta autorizado a conduzir veiculos da instituicao.</p>', '')
# fix accents in visible text
repl = {
    'Controle de veiculos':'Controle de veículos',
    'Diario de bordo e cadastro de veiculos.':'Diário de bordo e cadastro de veículos.',
    'Carregando dados do controle de veiculos...':'Carregando dados do controle de veículos...',
    'cadastro de veiculos':'Cadastro de veículos',
    'Mapa de bordo':'Mapa de bordo',
    'Nenhum registro de bordo cadastrado ate o momento.':'Nenhum registro de bordo cadastrado até o momento.',
    'veiculos':'veículos',
    'instituicao':'instituição',
    'esta':'está',
    'autorizados':'autorizados'
}
for k,v in repl.items():
    text = text.replace(k,v)
# specific accents
text = text.replace('placa', 'placa')
text = text.replace('responsavel', 'responsável')
text = text.replace('Motoristas autorizados', 'Motoristas autorizados')

p.write_text(text, encoding='utf-8')
