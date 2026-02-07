from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
import re
stack=[]
for m in re.finditer(r'</?(div|section)\b', text):
    tag=m.group(0)
    name=m.group(1)
    if tag.startswith('</'):
        if stack and stack[-1]==name:
            stack.pop()
        else:
            print('unexpected closing', name, 'at', m.start())
            break
    else:
        stack.append(name)
print('stack tail', stack[-5:])
