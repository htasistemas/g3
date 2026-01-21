import csv
import re
import sys
from pathlib import Path

import pdfplumber


RAIZ_PROJETO = Path(__file__).resolve().parents[1]
ARQUIVO_PDF = RAIZ_PROJETO / "Beneficiarios.pdf"
ARQUIVO_CSV_ENTRADA = RAIZ_PROJETO / "dados-beneficiarios.csv"
ARQUIVO_CSV = RAIZ_PROJETO / "scripts" / "beneficiarios_extraidos.csv"
ARQUIVO_SQL = RAIZ_PROJETO / "scripts" / "importar_beneficiarios.sql"


def limpar_texto(texto):
    if texto is None:
        return None
    texto = texto.replace("\u00a0", " ").replace("\n", " ").strip()
    texto = re.sub(r"\s+", " ", texto)
    return texto


def somente_digitos(texto):
    if not texto:
        return None
    digitos = re.sub(r"\D+", "", texto)
    return digitos or None


def extrair_com_regex(texto, padrao, grupo=1):
    match = re.search(padrao, texto, flags=re.IGNORECASE)
    if not match:
        return None
    return limpar_texto(match.group(grupo))


def extrair_dois_com_regex(texto, padrao):
    match = re.search(padrao, texto, flags=re.IGNORECASE)
    if not match:
        return None, None
    return limpar_texto(match.group(1)), limpar_texto(match.group(2))


def extrair_beneficiario(texto):
    nome, data_nascimento = extrair_dois_com_regex(
        texto,
        r"Benefici.*?:\s*(.+?)\s*Data Nascimento:\s*([0-9]{2}/[0-9]{2}/[0-9]{4})",
    )
    if not nome:
        return None

    nome_mae, sexo_biologico = extrair_dois_com_regex(
        texto,
        r"M.*?Resp:\s*(.+?)\s*Sexo:\s*([A-Za-zÀ-ÿ ]+)",
    )
    logradouro, numero = extrair_dois_com_regex(
        texto,
        r"Endere.*?:\s*(.+?)\s*N.*?mero:\s*([0-9A-Za-z/\\-]+)",
    )
    bairro, complemento = extrair_dois_com_regex(
        texto,
        r"Bairro:\s*(.+?)\s*Complemento:\s*(.+)",
    )
    cidade, estado = extrair_dois_com_regex(texto, r"Cidade:\s*(.+?)\s*Estado:\s*([A-Z]{2})")
    cep, telefone_principal = extrair_dois_com_regex(texto, r"CEP:\s*([0-9]{5,8})\s*Telefone:\s*(.+)")
    cpf, cor_raca = extrair_dois_com_regex(texto, r"CPF:\s*([0-9\\.\\-]+)\s*Ra.*?Cor:\s*(.+)")

    return {
        "nomeCompleto": nome,
        "dataNascimento": data_nascimento,
        "sexoBiologico": sexo_biologico,
        "nomeMae": nome_mae,
        "corRaca": cor_raca,
        "logradouro": logradouro,
        "numero": numero,
        "bairro": bairro,
        "cidade": cidade,
        "estado": estado,
        "cep": somente_digitos(cep) if cep else None,
        "telefonePrincipal": limpar_texto(telefone_principal),
        "cpf": somente_digitos(cpf) if cpf else None,
        "complemento": complemento,
    }


def extrair_beneficiarios_do_pdf():
    if not ARQUIVO_PDF.exists():
        raise FileNotFoundError(f"Arquivo nao encontrado: {ARQUIVO_PDF}")

    beneficiarios = []
    with pdfplumber.open(ARQUIVO_PDF) as pdf:
        for indice, pagina in enumerate(pdf.pages, start=1):
            texto = pagina.extract_text() or ""
            dados = extrair_beneficiario(texto)
            if not dados:
                continue
            dados["pagina"] = indice
            beneficiarios.append(dados)
    return beneficiarios


def ler_beneficiarios_csv(caminho_csv):
    if not caminho_csv.exists():
        return []
    beneficiarios = []
    with caminho_csv.open("r", encoding="utf-8") as arquivo:
        leitor = csv.DictReader(arquivo, delimiter=";")
        for indice, linha in enumerate(leitor, start=1):
            beneficiarios.append(
                {
                    "nomeCompleto": limpar_texto(linha.get("nomeCompleto")),
                    "dataNascimento": limpar_texto(linha.get("dataNascimento")),
                    "sexoBiologico": limpar_texto(linha.get("sexoBiologico")),
                    "nomeMae": limpar_texto(linha.get("nomeMae")),
                    "corRaca": limpar_texto(linha.get("corRaca")),
                    "logradouro": limpar_texto(linha.get("logradouro")),
                    "numero": limpar_texto(linha.get("numero")),
                    "bairro": limpar_texto(linha.get("bairro")),
                    "cidade": limpar_texto(linha.get("cidade")),
                    "estado": limpar_texto(linha.get("estado")),
                    "cep": somente_digitos(linha.get("cep")),
                    "telefonePrincipal": limpar_texto(linha.get("telefonePrincipal")),
                    "cpf": somente_digitos(linha.get("cpf")),
                    "complemento": limpar_texto(linha.get("complemento")),
                    "pagina": indice,
                }
            )
    return beneficiarios


def escrever_csv(beneficiarios):
    campos = [
        "nomeCompleto",
        "dataNascimento",
        "sexoBiologico",
        "nomeMae",
        "corRaca",
        "logradouro",
        "numero",
        "bairro",
        "cidade",
        "estado",
        "cep",
        "telefonePrincipal",
        "cpf",
        "complemento",
    ]
    with ARQUIVO_CSV.open("w", newline="", encoding="utf-8") as arquivo:
        writer = csv.DictWriter(arquivo, fieldnames=campos, delimiter=";")
        writer.writeheader()
        for item in beneficiarios:
            writer.writerow({campo: item.get(campo) for campo in campos})


def escapar_sql(texto):
    if texto is None:
        return None
    return texto.replace("'", "''")


def valor_sql(texto):
    if texto is None or str(texto).strip() == "":
        return "NULL"
    return f"'{escapar_sql(str(texto).strip())}'"


def data_sql(data):
    if not data:
        return "NULL::date"
    return f"to_date('{escapar_sql(data)}', 'DD/MM/YYYY')"


def gerar_sql(beneficiarios):
    linhas = ["BEGIN;"]
    for indice, item in enumerate(beneficiarios, start=1):
        nome = item.get("nomeCompleto")
        nome_mae = item.get("nomeMae")
        data_nascimento = item.get("dataNascimento")
        sexo = item.get("sexoBiologico")
        cor_raca = item.get("corRaca")
        logradouro = item.get("logradouro")
        numero = item.get("numero")
        complemento = item.get("complemento")
        bairro = item.get("bairro")
        cidade = item.get("cidade")
        estado = item.get("estado")
        cep = item.get("cep")
        telefone = item.get("telefonePrincipal")
        cpf = item.get("cpf")
        pagina = item.get("pagina")

        linhas.append(f"-- Beneficiario {indice} (pagina {pagina})")
        linhas.append("WITH dados AS (")
        linhas.append(f"  SELECT {valor_sql(nome)}::varchar AS nome_completo,")
        linhas.append(f"         {data_sql(data_nascimento)} AS data_nascimento,")
        linhas.append(f"         {valor_sql(sexo)}::varchar AS sexo_biologico,")
        linhas.append(f"         {valor_sql(nome_mae)}::varchar AS nome_mae,")
        linhas.append(f"         {valor_sql(cor_raca)}::varchar AS cor_raca,")
        linhas.append(f"         {valor_sql(logradouro)}::varchar AS logradouro,")
        linhas.append(f"         {valor_sql(numero)}::varchar AS numero,")
        linhas.append(f"         {valor_sql(complemento)}::varchar AS complemento,")
        linhas.append(f"         {valor_sql(bairro)}::varchar AS bairro,")
        linhas.append(f"         {valor_sql(cidade)}::varchar AS cidade,")
        linhas.append(f"         {valor_sql(estado)}::varchar AS estado,")
        linhas.append(f"         {valor_sql(cep)}::varchar AS cep,")
        linhas.append(f"         {valor_sql(telefone)}::varchar AS telefone_principal,")
        linhas.append(f"         {valor_sql(cpf)}::varchar AS cpf")
        linhas.append("),")
        linhas.append("verificacao AS (")
        linhas.append("  SELECT")
        linhas.append(
            "    CASE"
            " WHEN dados.cpf IS NULL OR dados.cpf = '' THEN 0"
            " ELSE (SELECT COUNT(1) FROM documentos d"
            " WHERE d.tipo_documento = 'CPF' AND d.numero_documento = dados.cpf)"
            " END AS existe_cpf,"
        )
        linhas.append(
            "    (SELECT COUNT(1) FROM cadastro_beneficiario cb"
            " WHERE cb.nome_completo = dados.nome_completo"
            " AND cb.data_nascimento = dados.data_nascimento"
            " AND cb.nome_mae = dados.nome_mae) AS existe_beneficiario"
        )
        linhas.append("  FROM dados")
        linhas.append("),")
        linhas.append("endereco_inserido AS (")
        linhas.append(
            "  INSERT INTO endereco"
            " (cep, logradouro, numero, complemento, bairro, cidade, estado, criado_em, atualizado_em)"
        )
        linhas.append(
            "  SELECT dados.cep, dados.logradouro, dados.numero, dados.complemento,"
            " dados.bairro, dados.cidade, dados.estado, NOW(), NOW()"
        )
        linhas.append("  FROM dados, verificacao")
        linhas.append("  WHERE verificacao.existe_cpf = 0 AND verificacao.existe_beneficiario = 0")
        linhas.append(
            "    AND (dados.cep IS NOT NULL OR dados.logradouro IS NOT NULL OR dados.numero IS NOT NULL"
            " OR dados.bairro IS NOT NULL OR dados.cidade IS NOT NULL OR dados.estado IS NOT NULL)"
        )
        linhas.append("  RETURNING id")
        linhas.append("),")
        linhas.append("beneficiario_inserido AS (")
        linhas.append(
            "  INSERT INTO cadastro_beneficiario"
            " (nome_completo, data_nascimento, sexo_biologico, nome_mae, cor_raca,"
            " endereco_id, status, criado_em, atualizado_em)"
        )
        linhas.append(
            "  SELECT dados.nome_completo, dados.data_nascimento, dados.sexo_biologico,"
            " dados.nome_mae, dados.cor_raca, endereco_inserido.id, 'EM_ANALISE', NOW(), NOW()"
        )
        linhas.append("  FROM dados")
        linhas.append("  LEFT JOIN endereco_inserido ON TRUE")
        linhas.append("  JOIN verificacao ON TRUE")
        linhas.append("  WHERE verificacao.existe_cpf = 0 AND verificacao.existe_beneficiario = 0")
        linhas.append("  RETURNING id")
        linhas.append("),")
        linhas.append("contato_inserido AS (")
        linhas.append(
            "  INSERT INTO contato_beneficiario"
            " (beneficiario_id, telefone_principal, criado_em, atualizado_em)"
        )
        linhas.append(
            "  SELECT beneficiario_inserido.id, dados.telefone_principal, NOW(), NOW()"
        )
        linhas.append("  FROM beneficiario_inserido, dados")
        linhas.append(
            "  WHERE dados.telefone_principal IS NOT NULL AND dados.telefone_principal <> ''"
        )
        linhas.append("  RETURNING id")
        linhas.append("),")
        linhas.append("documento_inserido AS (")
        linhas.append(
            "  INSERT INTO documentos"
            " (beneficiario_id, tipo_documento, numero_documento, criado_em, atualizado_em)"
        )
        linhas.append(
            "  SELECT beneficiario_inserido.id, 'CPF', dados.cpf, NOW(), NOW()"
        )
        linhas.append("  FROM beneficiario_inserido, dados")
        linhas.append("  WHERE dados.cpf IS NOT NULL AND dados.cpf <> ''")
        linhas.append("  RETURNING id")
        linhas.append(")")
        linhas.append("SELECT 1;")
        linhas.append("")

    linhas.append("COMMIT;")
    linhas.append("SELECT COUNT(*) AS total_beneficiarios FROM cadastro_beneficiario;")
    ARQUIVO_SQL.write_text("\n".join(linhas), encoding="utf-8")


def main():
    forcar_pdf = "--forcar-pdf" in sys.argv
    if not forcar_pdf:
        beneficiarios = ler_beneficiarios_csv(ARQUIVO_CSV_ENTRADA)
    else:
        beneficiarios = []

    if not beneficiarios:
        beneficiarios = extrair_beneficiarios_do_pdf()
    if not beneficiarios:
        print("Nenhum beneficiario encontrado no PDF.")
        return

    escrever_csv(beneficiarios)
    gerar_sql(beneficiarios)
    print(f"CSV gerado em: {ARQUIVO_CSV}")
    print(f"SQL gerado em: {ARQUIVO_SQL}")
    print(f"Total de beneficiarios extraidos: {len(beneficiarios)}")


if __name__ == "__main__":
    main()
