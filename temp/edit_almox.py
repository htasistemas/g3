from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
lines = p.read_text(encoding='utf-8', errors='replace').splitlines()
# replace dashboard header block (lines 56-73 1-based)
start = 55
end = 73
new_block = [
    '      <div class="tab-card__header">',
    '        <div class="tab-card__title-stack">',
    '          <p class="tab-card__highlight">Dashboards</p>',
    '        </div>',
    '      </div>',
]
lines[start-1:end] = new_block
# insert actions block after cards grid
# find first grid starting line for dashboards
idx_grid = None
for i,l in enumerate(lines):
    if 'grid grid-cols-1' in l:
        idx_grid = i
        break
insert_after = None
if idx_grid is not None:
    depth = 0
    for j in range(idx_grid, len(lines)):
        depth += lines[j].count('<div')
        depth -= lines[j].count('</div>')
        if depth == 0:
            insert_after = j
            break
if insert_after is not None:
    actions = [
        '      <div class="tab-card__actions tab-card__actions--below">',
        '        <button class="ghost" type="button" (click)="abrirEntradaProdutos()">',
        '          <fa-icon [icon]="faPlus" class="mr-2"></fa-icon>',
        '          Entrada de produtos',
        '        </button>',
        '        <button class="ghost" type="button" (click)="resetItemForm()">',
        '          <fa-icon [icon]="faRotate" class="mr-2"></fa-icon>',
        '          Limpar formulário',
        '        </button>',
        '        <button class="primary" type="button" (click)="openMovementModal()">',
        '          <fa-icon [icon]="faPlus" class="mr-2"></fa-icon>',
        '          Nova movimentação',
        '        </button>',
        '      </div>',
    ]
    lines[insert_after+1:insert_after+1] = actions
# update metric icon classes and icons
for i,l in enumerate(lines):
    if 'metric-icon--primary' in l or 'metric-icon--alert' in l or 'metric-icon--info' in l or 'metric-icon--muted' in l:
        l = l.replace('metric-icon--primary','metric-icon--success')
        l = l.replace('metric-icon--alert','metric-icon--success')
        l = l.replace('metric-icon--info','metric-icon--success')
        l = l.replace('metric-icon--muted','metric-icon--success')
    if 'faTriangleExclamation' in l:
        l = l.replace('faTriangleExclamation','faBoxesStacked')
    lines[i] = l
# insert titles in other tabs
repls = {
    119: 'Cadastro de Itens',
    326: 'Composição do Kit',
    414: 'Itens do Almoxarifado',
    524: 'Movimentações'
}
for line_no,title in repls.items():
    idx = line_no - 1
    if idx < len(lines) and lines[idx].strip() == '<div class="tab-card__title-stack">':
        # ensure next line is title
        if idx+1 < len(lines) and 'tab-card__highlight' in lines[idx+1]:
            lines[idx+1] = f'          <p class="tab-card__highlight">{title}</p>'
        else:
            lines[idx+1:idx+1] = [f'          <p class="tab-card__highlight">{title}</p>']
# update sidebar title/icon
for i,l in enumerate(lines):
    if 'faBoxArchive' in l:
        lines[i] = l.replace('faBoxArchive','faBoxesStacked')
    if 'Controle de Estoque' in l:
        lines[i] = l.replace('Controle de Estoque','Almoxarifado')

p.write_text('\n'.join(lines)+'\n', encoding='utf-8')
