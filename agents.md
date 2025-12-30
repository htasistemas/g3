# AGENTS.md - Diretrizes para Agentes e Contribuicoes (G3)

Este documento define como agentes humanos ou assistentes de IA devem atuar ao contribuir com o sistema G3.

## Idioma
- Sempre comunicar em portugues.
- Nomes tecnicos podem permanecer em ingles quando fizer sentido (ex.: DTO, Service, Repository).
- Esta diretriz e obrigatoria para todas as respostas.

## Padrao de nomenclatura
- Padrao de nome para variaveis e classes: exemplo `unidadeAssistencial`.
- Nomes sempre em portugues (codigo, banco de dados, tabelas e colunas).

## Principios fundamentais
- Priorizar clareza, manutencao, estabilidade e seguranca.
- Nenhuma solucao deve ser implementada apenas para funcionar.
- Toda alteracao deve considerar impacto futuro.

## Escopo de atuacao
Agentes devem:
- Sugerir solucoes alinhadas ao padrao do projeto.
- Respeitar a arquitetura existente.
- Evitar reescritas desnecessarias.
- Manter compatibilidade com o que ja esta em producao.

Agentes nao devem:
- Criar solucoes paralelas para o mesmo problema.
- Introduzir dependencias sem justificativa clara.
- Alterar padroes sem alinhar com este documento.

## Regras de codigo
- Codigo deve ser legivel, simples e coeso.
- Evitar duplicacao; reutilizar servicos e utilitarios existentes.
- Preferir composicao a heranca.
- Metodos devem ter responsabilidade unica.
- Evitar logica oculta ou efeitos colaterais inesperados.
- Todas as telas novas ou ja criadas precisam gerar o popup de error message.   
- Todo campo obrigatorio deve exibir (*) ao lado do titulo do campo e, ao clicar em "salvar" sem preenchimento, deve apresentar mensagem de erro clara ao usuario.
- Nenhuma acao deve exigir duplo clique: todo botao de acao/navegacao deve executar com 1 clique.
- Botoes dentro de `form` devem ser `type="button"` por padrao; usar `type="submit"` apenas quando necessario.
- E proibido duplicar eventos de clique (ex.: `click` + `mouseup`, multiplos listeners ou `HostListener` duplicado).
- Evitar overlays capturando clique quando nao estiverem ativos (ajustar `pointer-events`/`z-index` quando aplicavel).

## Angular (Frontend)
- Nao adicionar logica pesada em templates.
- Sempre usar servicos para regras de negocio.
- Tipagem estrita e obrigatoria; evitar `any`.
- Seguir os padroes visuais e estruturais definidos no README.md.
- Componentes devem ser pequenos e focados.
- Separar smart/dumb components quando aplicavel.
- Todas as telas devem usar `app-tela-padrao` como base, mantendo o formulario centralizado e o cabecalho alinhado ao padrao da tela de cadastro de unidade.
- `app-tela-padrao` deve posicionar a `app-barra-acoes-crud` no canto superior direito, alinhada ao titulo (fora de cards e formularios), com as acoes via input; "cancelar" deve limpar todos os campos, mesmo em cadastro novo.
- Titulos de telas devem seguir o mesmo padrao visual dos cadastros base (classe `page-title__eyebrow` com tamanho 0.95rem e `page-title__label` com tamanho 1.5rem e peso 800).
- Sempre que for solicitado criar uma listagem, usar como modelo a listagem de beneficiarios (filtros, card de item ativo, lista e paginacao) e manter o mesmo padrao visual.

### Campos padronizados
- Sempre aplicar capitalizacao: cada palavra deve iniciar com maiuscula e o restante minusculo em campos de texto (nome fantasia, razao social, endereco, bairro, cidade, horario, responsavel etc.), exceto quando o campo aceita livre texto longo (observacoes) ou emails.
- Qualquer campo de CNPJ precisa ter mascara `00.000.000/0000-00` no frontend e validar a estrutura (incluindo calculo dos digitos verificadores) antes de enviar ao backend, exibindo erro claro ao usuario.
- Qualquer campo de CPF precisa ter mascara `000.000.000-00` no frontend e validar a estrutura (incluindo calculo dos digitos verificadores) antes de enviar ao backend, exibindo erro claro ao usuario e destacando o campo com borda vermelha quando invalido.
- No modulo de endereco da unidade, o campo `cidade` deve oferecer autocomplete/lista baseada nas cidades de Minas Gerais e, ao confirmar um valor valido, preencher automaticamente o campo `estado` com a UF correspondente.

### Componentes compartilhados
- Autocomplete: usar exclusivamente `app-autocomplete`.
- Normalizar buscas (case-insensitive e sem acento) no frontend e backend.
- Mensagens:
  - Formularios: `PopupErrorBuilder` + `app-popup-messages`
  - Globais: `ErrorService` + `ToastComponent`
  - Feedback local de tela deve ser temporario (10s), com botao "X" para fechar manualmente.
- Confirmacoes: usar sempre `app-dialog`

### Relatorios (padrao)
- Todo relatorio deve ser impresso em A4.
- Estrutura obrigatoria:
  - Cabecalho: logomarca da instituicao (quando existir), razao social e nome do relatorio.
  - Corpo: conteudo principal com tabelas e secoes.
  - Rodape: dados da instituicao (endereco, telefone, email e CNPJ).
- Tipografia padrao: Arial (ou fallback sans-serif).
- Manter layout limpo, com margens e espacos consistentes.

## Backend (Java)
- Manter separacao clara de camadas:
  - Controller
  - Service
  - Repository
  - Domain
- Usar DTOs para entrada e saida.
- Nunca expor entidades diretamente.
- Validar dados no backend mesmo que ja validados no frontend.
- Priorizar imutabilidade quando possivel.
- Tratar erros de forma explicita e previsivel.
- CORS:
  - Se o frontend receber 403 no preflight (OPTIONS), configurar CORS no backend para liberar o Origin e headers/metodos usados.
  - Padrao recomendado: criar `CorsConfig` com `WebMvcConfigurer` e permitir `http://localhost:4200` em dev, com `allowedMethods` e `allowedHeaders` (ex.: `authorization`, `content-type`).
  - Em producao, restringir `allowedOrigins` ao dominio oficial do frontend.
  - Snippet (exemplo):
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registro) {
        registro.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("authorization", "content-type")
            .allowCredentials(true);
    }
}
```
  - Exemplo de configuracao:
```yaml
# application-dev.yml
app:
  cors:
    allowed-origins: "http://localhost:4200"

# application-prod.yml
app:
  cors:
    allowed-origins: "https://app.exemplo.com.br"
```

## Seguranca
- Nunca confiar apenas em dados vindos do frontend.
- Evitar exposicao de informacoes sensiveis em logs.
- Aplicar principio do menor privilegio.
- Criar defesas contra registros nulos, estados invalidos e fluxos incompletos.

## Banco de dados
- Toda alteracao estrutural deve estar em `init.db`.
- Scripts devem ser idempotentes.
- Evitar blocos `DO $$`.
- Usar nomes em portugues, claros e objetivos.
- Modelagem relacional correta e obrigatoria.
- Sempre usar `id` sequencial como chave primaria.
- Usar relacionamento por FK nas tabelas.
- Quando for solicitado "colocar no banco", sempre atualizar o `init.db` e criar/ajustar toda a infra Java necessaria (Domain, DTO, Repository, Service, Controller) para persistir e retornar os campos.

## Testes
- Criar ou atualizar testes sempre que alterar comportamento.
- Priorizar testes de fluxos criticos.
- Testes devem ser deterministas e rapidos.
- Codigo sem teste deve ser excecao, nao regra.

## Revisoes e mudancas
- Mudancas devem ser pequenas e com proposito claro.
- Nao misturar refatoracao com novas funcionalidades.
- Decisoes importantes devem ser documentadas.
- Atualizar README.md ou documentacao tecnica quando necessario.
- Sempre que houver uma nova versao do sistema, atualizar a versao em "Configuracoes gerais" e registrar no historico a versao, data/hora e mudancas realizadas.

## Postura esperada do agente
- Ser direto e objetivo.
- Apontar riscos tecnicos quando existirem.
- Nao assumir requisitos nao informados.
- Nao simplificar problemas complexos sem justificativa.
- Manter alinhamento com a arquitetura e visao do sistema G3.
