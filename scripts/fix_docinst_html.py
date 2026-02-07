from pathlib import Path
path = Path('frontend/src/app/components/documentos-institucionais/documentos-institucionais.component.html')
text = path.read_bytes().decode('latin-1')
reps = [
    ('Alvar?','Alvará'),
    ('Resumo do documento, ?mbito e Observações','Resumo do documento, âmbito e Observações'),
    ('Observa??o','Observação'),
    ('anteced?ncia','antecedência'),
    ('Salvar altera??es','Salvar alterações'),
    ('Observa??o','Observação'),
    ('altera??es','alterações'),
    ('anota??o','anotação'),
    ('Observa??o','Observação'),
    ('Organiza??o','Organização'),
    ('Navegue pelas configuraÃ§Ãµes','Navegue pelas configurações')
]
for a,b in reps:
    text = text.replace(a,b)
# fix remaining mojibake in file
reps2 = [
    ('Ã£','ã'),('Ã¡','á'),('Ã©','é'),('Ãª','ê'),('Ãº','ú'),('Ã³','ó'),('Ã§','ç'),('Ã­','í'),('Ã‰','É'),('Ã“','Ó'),('Ã“rgão','Órgão')
]
for a,b in reps2:
    text = text.replace(a,b)
path.write_text(text, encoding='utf-8')
