# AGENTS.md — Sistema G3 (Padrões para Codex/Agentes)

> Objetivo: orientar **implementação de telas** e mudanças correlatas no G3 com padrão único, sem gambiarras e sem divergências visuais/arquiteturais.

---

## REGRAS GERAIS (OBRIGATÓRIAS)

### Idioma
- MUST escrever comunicação, código (quando aplicável), comentários, commits e docs em **português**.
- MAY manter termos técnicos em inglês quando padrão (ex.: DTO, Service, Repository).

### Nomenclatura
- MUST nomear variáveis, classes, métodos, tabelas e colunas em **português**.
- MUST usar **camelCase** para variáveis e métodos (ex.: `unidadeAssistencial`).
- MUST evitar abreviações obscuras; nomes devem ser claros e completos.

### Postura / Arquitetura
- MUST respeitar a arquitetura existente.
- MUST evitar reescritas desnecessárias.
- MUST manter compatibilidade com produção.
- MUST sinalizar riscos técnicos quando houver.
- MUST ser direto e objetivo; NÃO assumir requisitos implícitos.

### Código
- MUST manter código simples, legível e coeso.
- MUST evitar duplicação; reutilizar serviços/utilitários existentes.
- SHOULD aplicar responsabilidade única por método.
- SHOULD preferir composição em vez de herança.
- MUST NOT criar implementações temporárias sem registro técnico.
- MUST NOT introduzir dependências sem justificativa técnica clara.
- SHOULD implementar padrão global de Design System.
- SHOULD ter biblioteca única de formulários.
- SHOULD implementar auditoria automática (quem alterou o quê).

---

## PADRÃO OBRIGATÓRIO PARA TELAS (UI/UX)

### Referência oficial
- MUST usar como referência **Cadastro de Beneficiário** do G3.
- MUST replicar padrão visual/estrutural/funcional já consolidado.

### Base da tela
- MUST usar `app-tela-padrao` como base.
- MUST usar cards padronizados do G3 para organizar conteúdo.
- SHOULD usar abas/etapas quando houver separação lógica.
- MUST seguir o padrão da tela **Cadastro de Beneficiários** para:
  - Abas laterais em formato de menu.
  - Título com ícone ao lado.
  - Barra de ações no topo seguindo o mesmo layout.
  - Tela responsiva ocupando 100% da área útil, com pequeno respiro nas laterais.
  - Layout principal preenchendo 100% da altura e largura disponíveis.

### Cabeçalho da página (sempre)
- MUST exibir no topo:
  - Linha 1: **MENU PAI** em letras maiúsculas, cinza.
  - Linha 2: **Tela atual** (Primeira letra maiúscula), preta.
- MUST NOT omitir este cabeçalho.
- MUST NOT criar variações visuais por tela.

### Título e subtítulo (anti-redundância)
- MUST existir **apenas 1 título principal** por área visual.
- MUST validar que não há títulos/headers duplicados (header global + título local).
- MUST NOT renderizar títulos/subtítulos sobrepostos.

### Barra de ações CRUD (obrigatória)
- MUST ficar logo abaixo do título.
- MUST ser horizontal (ícone + texto).
- MUST seguir esta ordem fixa:
  - **Buscar → Novo → Salvar → Cancelar → Excluir → Imprimir → Fechar**
- MUST NOT alterar a ordem.
- MUST dar destaque visual de alerta ao botão **Excluir**.
- MUST manter **Fechar** como último botão.
- SHOULD garantir que **Excluir** não fique “colado” em **Salvar**.

### Formulários e clique
- MUST garantir que nenhuma ação exija duplo clique.
- MUST garantir 1 clique por ação/navegação.
- MUST NOT duplicar eventos (`click` + `mouseup`), múltiplos listeners ou HostListener duplicado.
- Em botões dentro de `form`:
  - MUST usar `type="button"` por padrão.
  - MAY usar `type="submit"` apenas quando estritamente necessário.
- MUST evitar overlays capturando clique quando inativos (`pointer-events`, `z-index`).

### Validação e mensagens
- MUST exibir `(*)` em todo campo obrigatório.
- Ao clicar **Salvar** com campos obrigatórios vazios:
  - MUST mostrar mensagem clara de erro ao usuário.
- MUST exibir popup de erro padronizado em telas novas e existentes.

### Abas / Etapas (quando aplicável)
- Telas com múltiplas seções:
  - SHOULD usar navegação por etapas numeradas ou abas no padrão G3.
- Se houver abas, MUST seguir padrão visual:
  - Aba ativa: verde escuro + texto branco
  - Abas anteriores: verde claro + texto verde escuro
  - Abas futuras: neutras
- MUST usar **abas laterais em formato de menu**, sem linhas/separadores entre os itens (igual Cadastro de Beneficiários).
- MUST manter o **mesmo tamanho das abas** do Cadastro de Beneficiários:
  - `min-height: 44px`
  - `padding: 0.35rem 0.55rem`
  - `font-size: 0.8rem` no rótulo
  - Índice: `22px` x `22px`, `font-size: 0.7rem`
- MUST permitir apenas 1 aba ativa por vez.
- MUST manter comportamento idêntico ao padrão já existente (cadastros do G3).

### Regras visuais (anti-bug)
- MUST verificar visualmente: **nenhum texto sobreposto** (títulos, breadcrumbs, subtítulos, comentários).
- MUST evitar CSS com sobreposição:
  - MUST NOT usar `position:absolute` sem justificativa.
  - MUST NOT usar margem negativa para “ajeitar layout”.
  - MUST evitar conflitos de `z-index`.

### Comentário didático
- MUST incluir comentário explicativo no topo direito da área de título (conforme padrão do Beneficiário).

---

## CARREGAMENTO AUTOMÁTICO DE DADOS (OBRIGATÓRIO)
- MUST carregar e exibir dados automaticamente ao abrir a tela.
- MUST NOT depender de clique/foco/hover/troca de aba para popular dados.
- MUST carregar no lifecycle adequado (ex.: `ngOnInit`, ou `ngAfterViewInit` quando necessário).
- Se usar `ChangeDetectionStrategy.OnPush`:
  - MUST garantir atualização correta (imutabilidade, `async pipe`, ou `markForCheck`).
- MUST NOT usar gambiarras (`setTimeout`, cliques forçados, eventos artificiais).

---

## ANGULAR (FRONTEND)
- MUST NOT colocar lógica pesada no template.
- MUST colocar regras de negócio em services.
- MUST usar tipagem estrita; evitar `any`.
- SHOULD manter componentes pequenos e focados.
- MAY separar smart/dumb quando aplicável.

### Estrutura específica
- MUST usar `app-barra-acoes-crud`:
  - alinhada ao título; fora de cards e formulários.
- **Cancelar**:
  - MUST limpar todos os campos, inclusive em “novo cadastro”.

### Títulos (CSS)
- MUST seguir padrão do Beneficiário (mesmo espaçamento/tamanho/peso):
  - `page-title__eyebrow`: 0.95rem
  - `page-title__label`: 1.5rem, peso 800

### Listagens
- SHOULD usar como modelo a listagem de beneficiários (filtros, cards, lista e paginação).
- MUST manter botões de filtro (Buscar/Limpar) alinhados ao campo **Status** e com a mesma altura dos campos.
- MUST manter espaçamento horizontal entre os campos de filtro (ex.: Nome, Categoria, Status), evitando campos colados.
- MUST remover botões de **Editar** nas listagens e permitir **duplo clique** na linha/card para abrir a edição.

---

## CAMPOS PADRONIZADOS
- MUST capitalizar labels (Primeira Letra Maiúscula).
- CPF:
  - MUST usar máscara `000.000.000-00`
  - MUST validar completo
  - MUST indicar inválido com borda vermelha
- CNPJ:
  - MUST usar máscara `00.000.000/0000-00`
  - MUST validar completo
- Endereço da unidade:
  - `cidade` com autocomplete (MG)
  - MUST preencher `estado` automaticamente

---

## COMPONENTES COMPARTILHADOS (OBRIGATÓRIO REUTILIZAR)
- Autocomplete: MUST usar `app-autocomplete`.
- Busca: MUST normalizar (sem acento e case-insensitive).
- Mensagens:
  - Formulários: MUST usar `PopupErrorBuilder` + `app-popup-messages`
  - Globais: MUST usar `ErrorService` + `ToastComponent`
  - Feedback local: MAY usar temporário (10s) com botão "X"
- Confirmações: MUST usar `app-dialog`.
- Email: MUST usar configuração do servidor já existente (mesma do “recuperar” login).

---

## BACKEND (SE ENVOLVER DADOS)
### Java
- MUST seguir camadas: Controller, Service, Repository, Domain.
- MUST usar DTOs; MUST NOT expor entidades.
- MUST validar dados no backend e tratar erros explicitamente.
- SHOULD priorizar imutabilidade.

### CORS
- MUST configurar para evitar 403 preflight.
- Dev: `http://localhost:4200`
- Prod: apenas domínio oficial.

### Banco de dados
- MUST registrar alteração estrutural em `init.db`.
- MUST criar scripts idempotentes.
- MUST usar nomes em português; PK `id` sequencial; FKs obrigatórias.
- Ao “colocar no banco”, MUST criar Domain, DTO, Repository, Service e Controller.

---

## RELATÓRIOS (SE APLICAR)
- MUST seguir padrão:
  - A4, margens 20mm, fonte Arial
  - cabeçalho/corpo/rodapé
  - “Página X de Y”
  - dados reais
  - HTML + PDF (via HTML)
  - 1 clique
  - template único reutilizável

---

## TESTES (SE ALTERAR COMPORTAMENTO)
- MUST criar/atualizar testes ao alterar comportamento.
- SHOULD priorizar fluxos críticos; testes rápidos e deterministas.

---

## VERSÃO / HISTÓRICO
- MUST atualizar versão em Configurações Gerais.
- MUST registrar versão, data/hora e mudanças de forma objetiva.
- MUST usar formato `1.00.0` e incrementar apenas o último grupo.
- MUST NOT repetir número de versão (sequencial e único).

---

## ✅ CHECKLIST FINAL (ANTES DE ENTREGAR QUALQUER TELA)

Todo agente MUST confirmar:

- Tela igual Beneficiário
- CRUD correto
- Nenhum clique duplicado
- Dados carregam automaticamente
- Validação completa
- Código sem duplicação
- Backend seguro
- Banco atualizado via init.db
- Layout sem sobreposição
