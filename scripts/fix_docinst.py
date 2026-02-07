from pathlib import Path
path = Path('frontend/src/app/components/documentos-institucionais/documentos-institucionais.component.ts')
text = path.read_bytes().decode('latin-1')
reps = [
    ('JurÃ­dico','Jurídico'),
    ('LicenÃ§as','Licenças'),
    ('SeguranÃ§a','Segurança'),
    ('CertidÃ£o','Certidão'),
    ('DÃ©bitos','Débitos'),
    ('AlvarÃ¡','Alvará'),
    ('PrestaÃ§Ã£o','Prestação'),
    ('ServiÃ§os','Serviços'),
    ('LicenÃ§a','Licença'),
    ('SanitÃ¡ria','Sanitária'),
    ('AutomÃ¡tica','Automática'),
    ('NÃ£o','Não'),
    ('nÃ£o','não'),
    ('jÃ¡','já'),
    ('estÃ¡','está'),
    ('incluÃ­do','incluído'),
    ('disponÃ­vel','disponível'),
    ('obrigatÃ³rios','obrigatórios'),
    ('Ã ','à'),
    ('emissÃ£o','emissão'),
    ('ObservaÃ§Ãµes','Observações'),
    ('ResponsÃ¡vel','Responsável'),
    ('RenovaÃ§Ã£o','Renovação'),
    ('VÃ¡lido','Válido'),
    ('SituaÃ§Ã£o','Situação'),
    ('Ã“rgÃ£o','Órgão'),
    ('RELATÃ“RIO','RELATÓRIO'),
    ('RelaÃ§Ã£o','Relação'),
    ('RelatÃ³rio','Relatório'),
    ('â€”','—')
]
for a,b in reps:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
