from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
lines = p.read_text(encoding='utf-8', errors='replace').splitlines()
stack=[]
for i,l in enumerate(lines,1):
    if '<div class="cadastro-layout"' in l:
        stack.append(('cadastro-layout', i))
    if '<div class="cadastro-conteudo"' in l:
        stack.append(('cadastro-conteudo', i))
    if l.strip()=='</div>':
        if stack:
            name, start = stack.pop()
            if name in ['cadastro-layout','cadastro-conteudo']:
                print('close', name, 'opened', start, 'closed', i)
        else:
            pass
