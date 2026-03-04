from pathlib import Path
path = Path('frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.ts')
raw = path.read_bytes()
replacements = {
    b'CPF n\xc3\xa3o informado para este benefici\xc3\xa1rio.': b'CPF n\\u00e3o informado para este benefici\\u00e1rio.',
    b'Telefone do benefici\xc3\xa1rio n\xc3\xa3o informado.': b'Telefone principal do benefici\\u00e1rio n\\u00e3o informado.',
    b'N\xc3\xa3o foi poss\xc3\xadvel localizar os dados do benefici\xc3\xa1rio.': b'N\\u00e3o foi poss\\u00edvel localizar os dados do benefici\\u00e1rio.'
}
for old, new in replacements.items():
    raw = raw.replace(old, new)
raw = raw.replace(b'const telefone = (beneficiario?.telefone_principal || beneficiario?.telefone || \'\').toString();',
                  b'const telefone = (beneficiario?.telefone_principal || \'\').toString();')
path.write_bytes(raw)
