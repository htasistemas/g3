from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
idx2 = text.find("activeTab === 'dashboards'")
print(text[idx2-200:idx2+400])
