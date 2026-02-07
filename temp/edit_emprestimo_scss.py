from pathlib import Path
p = Path(r'C:\g3\frontend\src\app\components\emprestimos-eventos\emprestimos-eventos-page.component.scss')
text = p.read_text(encoding='utf-8', errors='replace')
text = text.replace('padding: clamp(1rem, 2vw, 2rem);', 'padding: 0;')
if '.cadastro-layout' not in text:
    text += "\n.cadastro {\n  width: 100%;\n  margin: 0;\n  padding: 0 1rem;\n  box-sizing: border-box;\n  display: flex;\n  flex-direction: column;\n  gap: 1rem;\n}\n\n.cadastro-layout {\n  display: grid;\n  grid-template-columns: minmax(220px, 260px) 1fr;\n  gap: 1.5rem;\n  align-items: start;\n}\n\n.cadastro-menu {\n  position: sticky;\n  top: 0.5rem;\n}\n\n.cadastro-conteudo {\n  width: 100%;\n}\n\n.tab-shell__title--destaque {\n  font-size: 0.95rem;\n  font-weight: 800;\n  letter-spacing: 0.02em;\n  color: #0f7a43;\n  text-transform: uppercase;\n  text-align: center;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  gap: 0.5rem;\n}\n\n.tab-shell__icon {\n  font-size: 1.45rem;\n}\n\n.step-tabs {\n  display: flex;\n  flex-direction: column;\n  gap: 0.4rem;\n}\n\n.step-tabs__item {\n  min-height: 44px;\n}\n\n.step-tabs__label {\n  font-size: 0.8rem;\n}\n\n.step-tabs__index {\n  width: 22px;\n  height: 22px;\n  font-size: 0.7rem;\n}\n\n.step-tabs__item::after {\n  display: none;\n}\n\n.step-tabs__item.completed .step-tabs__button {\n  background: #dcfce7;\n  color: #166534;\n  border-color: #bbf7d0;\n}\n\n.step-tabs__button {\n  padding: 0.35rem 0.55rem;\n  min-height: 44px;\n}\n"

p.write_text(text, encoding='utf-8')
