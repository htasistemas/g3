from pathlib import Path
import re
text = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html').read_text(encoding='utf-8', errors='replace')
# tags to track
track = ['div','section','table','tbody','thead','tr','td']
pattern = re.compile(r'</?(' + '|'.join(track) + r')\b')
stack=[]
for m in pattern.finditer(text):
    tag = m.group(0)
    name = m.group(1)
    if tag.startswith('</'):
        # pop until match
        if not stack:
            print('extra closing', name, 'at', m.start())
            break
        if stack[-1]!=name:
            print('mismatch closing', name, 'at', m.start(), 'top', stack[-1])
            break
        stack.pop()
    else:
        stack.append(name)
print('stack', stack[-10:])
