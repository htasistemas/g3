from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\emprestimos-eventos\emprestimos-eventos-page.component.ts')
text = p.read_text(encoding='utf-8', errors='replace')
if 'FontAwesomeModule' not in text:
    text = text.replace("import { ActivatedRoute, Router, RouterModule } from '@angular/router';",
                        "import { ActivatedRoute, Router, RouterModule } from '@angular/router';\nimport { FontAwesomeModule } from '@fortawesome/angular-fontawesome';\nimport { faCalendarCheck } from '@fortawesome/free-solid-svg-icons';")
# add FontAwesomeModule to imports array
text = text.replace('TelaPadraoComponent', 'TelaPadraoComponent,\n    FontAwesomeModule')
# add icon property
if 'faCalendarCheck' not in text:
    text = text.replace('export class EmprestimosEventosPageComponent implements OnInit, OnDestroy {',
                        'export class EmprestimosEventosPageComponent implements OnInit, OnDestroy {\n  readonly faCalendarCheck = faCalendarCheck;')
# fix abas accents
text = text.replace('Visao geral dos emprestimos e itens.', 'Visão geral dos empréstimos e itens.')
text = text.replace('Calendario de indisponibilidade e eventos.', 'Calendário de indisponibilidade e eventos.')
text = text.replace('Resumo dos emprestimos cadastrados.', 'Resumo dos empréstimos cadastrados.')
text = text.replace('emprestimos', 'empréstimos')
text = text.replace('emprestimo', 'empréstimo')
text = text.replace('Calendario', 'Calendário')
text = text.replace('Visao', 'Visão')
text = text.replace('periodo', 'período')
text = text.replace('Nao', 'Não')
# clean duplicate FontAwesomeModule if any
text = text.replace('FontAwesomeModule,\n    FontAwesomeModule', 'FontAwesomeModule')

p.write_text(text, encoding='utf-8')
