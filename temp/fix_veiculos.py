from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\controle-veiculos\controle-veiculos.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
text = text.replace('tab-shell__title--destáque', 'tab-shell__title--destaque')
text = text.replace('tab-shell__title--dest�que', 'tab-shell__title--destaque')
text = text.replace('<p class="tab-shell__title">Navegue pelas informações</p>', '')
text = text.replace('<p class="tab-shell__title">Navegue pelas informa��es</p>', '')
p.write_text(text, encoding='utf-8')
