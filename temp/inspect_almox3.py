from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
# print first occurrence of tab-card
import re
m = re.search(r"tab-card\" \*ngIf=\"activeTab", text)
print(m.start() if m else 'no')
print(text[m.start()-100:m.start()+200] if m else '')
