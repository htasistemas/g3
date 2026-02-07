from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
lines = p.read_text(encoding='utf-8', errors='replace').splitlines()
stack=[]
for i,l in enumerate(lines,1):
    if '<div class="cadastro-layout"' in l:
        stack.append(('cadastro-layout', i))
    if '<div class="cadastro-conteudo"' in l:
        stack.append(('cadastro-conteudo', i))
    if '<div class="tab-card"' in l:
        stack.append(('tab-card', i))
    if '</div>' in l:
        # pop one
        if stack:
            stack.pop()
        else:
            print('extra </div> at', i)
            break
print('stack tail', stack[-5:])
