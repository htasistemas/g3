from pathlib import Path
path = Path('frontend/src/app/components/documentos-institucionais/documentos-institucionais.component.ts')
text = path.read_text(encoding='utf-8')
reps = [
    ('Não foi possÃ­vel carregar os documentos institucionais.', 'Não foi possível carregar os documentos institucionais.'),
    ('Não foi possÃ­vel carregar os anexos.', 'Não foi possível carregar os anexos.'),
    ('Não foi possÃ­vel carregar o histÃ³rico.', 'Não foi possível carregar o histórico.'),
    ('Não foi possÃ­vel atualizar o documento.', 'Não foi possível atualizar o documento.'),
    ('Não foi possÃ­vel cadastrar o documento.', 'Não foi possível cadastrar o documento.'),
    ('Não foi possÃ­vel excluir o documento.', 'Não foi possível excluir o documento.'),
    ('Não foi possÃ­vel registrar o histÃ³rico.', 'Não foi possível registrar o histórico.'),
    ('<th>ÃrgÃ£o emissor</th>', '<th>Órgão emissor</th>'),
    ('<tr><th>ÃrgÃ£o emissor</th><td>${doc.orgaoEmissor}</td></tr>', '<tr><th>Órgão emissor</th><td>${doc.orgaoEmissor}</td></tr>'),
    ('<tr><th>EmissÃ£o</th><td>${doc.emissao}</td></tr>', '<tr><th>Emissão</th><td>${doc.emissao}</td></tr>'),
    ("em_renovacao: 'Em renovaÃ§Ã£o'", "em_renovacao: 'Em renovação'"),
    ("'ÃrgÃ£o emissor'", "'Órgão emissor'"),
    ("'RELATÃRIO TEXTO - LISTA DE DOCUMENTOS'", "'RELATÓRIO TEXTO - LISTA DE DOCUMENTOS'"),
]
for a,b in reps:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
