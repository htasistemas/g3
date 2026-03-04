from pathlib import Path
path = Path('frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.ts')
text = path.read_text(encoding='utf-8')
replacements = {
    'CPF nÃ£o informado para este beneficiÃ¡rio.': 'CPF não informado para este beneficiário.',
    'Telefone do beneficiÃ¡rio nÃ£o informado.': 'Telefone principal do beneficiário não informado.',
    'NÃ£o foi possÃ­vel localizar os dados do beneficiÃ¡rio.': 'Não foi possível localizar os dados do beneficiário.'
}
for old, new in replacements.items():
    text = text.replace(old, new)
text = text.replace('const telefone = (beneficiario?.telefone_principal || beneficiario?.telefone || \'\').toString();',
                    'const telefone = (beneficiario?.telefone_principal || \'\').toString();')
path.write_text(text, encoding='utf-8')
