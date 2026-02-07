from pathlib import Path
import re
p = Path(r'C:\g3\frontend\src\app\components\emprestimos-eventos\emprestimos-eventos-page.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
# remove header block
text = re.sub(r"\s*<header class=\"page-title[\s\S]*?</header>\s*", "\n", text, count=1)
# add fullWidth
if '[fullWidth]' not in text.split('\n',2)[0]:
    text = text.replace('<app-tela-padrao', '<app-tela-padrao\n  [fullWidth]="true"', 1)
# replace section tab-shell with cadastro layout
text = text.replace('<section class="tab-shell">', '<section class="cadastro">\n  <div class="cadastro-layout">\n    <aside class="cadastro-menu">\n      <div class="tab-shell">\n        <p class="tab-shell__title tab-shell__title--destaque">\n          <fa-icon [icon]="faCalendarCheck" class="tab-shell__icon"></fa-icon>\n          Empréstimo para Eventos\n        </p>')
# remove tab-shell title text
text = text.replace('<p class="tab-shell__title">Area principal</p>', '')
text = text.replace('<p class="tab-shell__title">Área principal</p>', '')
# convert step-abas classes
text = text.replace('step-abas', 'step-tabs')
# close aside / open content before feedback
text = text.replace('</ul>\n  </section>', '</ul>\n      </div>\n    </aside>\n\n    <div class="cadastro-conteudo">', 1)
# close layout before end section
text = text.replace('</section>', '    </div>\n  </div>\n</section>', 1)
# remove muted helper lines in dashboard header
text = text.replace('<p class="muted">Visao geral</p>', '')
# replace dashboard header with tab-card__header
text = text.replace('<div class="dashboard-header">', '<div class="tab-card__header">')
text = text.replace('<h3>Indicadores do emprestimo</h3>', '<p class="tab-card__highlight">Dashboard</p>')
# remove extra wrapping div in dashboard header if empty
text = text.replace('<div>\n        \n        <p class="tab-card__highlight">Dashboard</p>\n      </div>', '<div class="tab-card__title-stack">\n        <p class="tab-card__highlight">Dashboard</p>\n      </div>')
# move nav button to header actions
text = text.replace('<button type="button" class="nav-button" (click)="navegarParaAba(\'agenda\')">', '<div class="tab-card__actions">\n      <button type="button" class="nav-button" (click)="navegarParaAba(\'agenda\')">')
text = text.replace('Ver agenda\n      </button>', 'Ver agenda\n      </button>\n    </div>')
# add headers for other tab-cards
def add_header(tab_id, title):
    pattern = f'<section class="tab-card" *ngIf="abaAtiva === \'{tab_id}\'">'
    if pattern in text and f'<p class="tab-card__highlight">{title}</p>' not in text:
        return text.replace(pattern, pattern + f"\n    <div class=\"tab-card__header\">\n      <div class=\"tab-card__title-stack\">\n        <p class=\"tab-card__highlight\">{title}</p>\n      </div>\n    </div>")
    return text
text = add_header('agenda', 'Agenda')
text = add_header('lista', 'Listagem de Empréstimos')
# fix accents
repl = {
    'Emprestimo para eventos':'Empréstimo para eventos',
    'Emprestimo':'Empréstimo',
    'Visao':'Visão',
    'emprestimo':'empréstimo',
    'emprestimos':'empréstimos',
    'Calendario':'Calendário',
    'periodo':'período',
    'Area':'Área'
}
for k,v in repl.items():
    text = text.replace(k,v)

p.write_text(text, encoding='utf-8')
