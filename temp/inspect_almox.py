from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\almoxarifado\almoxarifado.component.html')
text = p.read_text(encoding='utf-8', errors='replace')
print('activeTab count', text.count("activeTab === '"))
idx = text.find('step-tabs')
print(text[idx:idx+400])
idx2 = text.find("activeTab === 'dashboards'")
print(text[idx2-80:idx2+120])
