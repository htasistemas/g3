from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
import re
stack = []
for m in re.finditer(r'</?div\b', text):
    tag = m.group(0)
    if tag == '<div':
        stack.append(m.start())
    else:
        if stack:
            stack.pop()
        else:
            print('extra closing at', m.start())
            break
print('unclosed', len(stack))
if stack:
    pos = stack[-1]
    print('around', text[pos:pos+120].replace('\n',' '))
