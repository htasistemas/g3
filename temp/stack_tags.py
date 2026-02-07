from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
lines = p.read_text(encoding='utf-8', errors='replace').splitlines()
stack=[]
for i,l in enumerate(lines,1):
    # find tags in line
    for tag in ['div','section']:
        if f'<{tag}' in l:
            # count occurrences
            for _ in range(l.count(f'<{tag}')):
                stack.append((tag,i))
        if f'</{tag}>' in l:
            for _ in range(l.count(f'</{tag}>')):
                if stack and stack[-1][0]==tag:
                    stack.pop()
                else:
                    print('unexpected closing', tag, 'at', i, 'line', l)
                    raise SystemExit
print('unclosed', stack[-5:])
