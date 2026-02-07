from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\controle-veiculos\controle-veiculos.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
text = text.replace('<p class="muted">Mantenha a frota organizada com dados completos.</p>', '')
text = text.replace('<p class="muted">Registre cada viagem e acompanhe o consumo.</p>', '')
text = text.replace('<p class="muted">Defina quem esta autorizado a conduzir veiculos da instituicao.</p>', '')
repl = {
    'Controle de veiculos':'Controle de veículos',
    'Diario de bordo e cadastro de veiculos.':'Diário de bordo e cadastro de veículos.',
    'Carregando dados do controle de veiculos...':'Carregando dados do controle de veículos...',
    'cadastro de veiculos':'Cadastro de veículos',
    'Veiculos':'Veículos',
    'veiculos':'veículos',
    'instituicao':'instituição',
    'esta':'está',
    'ate o momento':'até o momento',
    'Nenhum registro de bordo cadastrado ate o momento.':'Nenhum registro de bordo cadastrado até o momento.',
    'Diario':'Diário',
    'Responsavel':'Responsável'
}
for k,v in repl.items():
    text = text.replace(k,v)
text = text.replace('tab-shell__title--destáque','tab-shell__title--destaque')
text = text.replace('tab-shell__title--dest�que','tab-shell__title--destaque')
text = text.replace('<p class="tab-shell__title">Navegue pelas informações</p>','')
text = text.replace('<p class="tab-shell__title">Navegue pelas informa��es</p>','')
Path(r'C:\g3\frontend\src\app\components\controle-veiculos\controle-veiculos.component.html').write_text(text, encoding='utf-8')
