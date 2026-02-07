from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
# find all tab-card *ngIf
import re
for m in re.finditer(r"<div class=\"tab-card\" \*ngIf=\"activeTab === '([^']+)'\"", text):
    print(m.group(1))
