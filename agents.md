# AGENTS.md - Diretrizes Oficiais para Agentes e Contribuicoes (Sistema G3)

Este documento define regras obrigatorias para atuacao de agentes humanos e assistentes de IA na manutencao, evolucao e implementacao de funcionalidades do Sistema G3.
O nao cumprimento destas diretrizes caracteriza desvio de padrao do projeto.

## 1. Idioma
- Toda comunicacao deve ser feita em portugues.
- Termos tecnicos podem permanecer em ingles quando forem padrao de mercado (ex.: DTO, Service, Repository).
- Esta regra e obrigatoria para respostas, codigo, comentarios, commits e documentacao.

## 2. Padrao de nomenclatura
- Variaveis, classes, metodos, tabelas e colunas devem estar em portugues.
- Padrao obrigatorio: camelCase para variaveis e metodos.
- Exemplo: `unidadeAssistencial`.
- Nomes devem ser claros, completos e sem abreviacoes obscuras.

## 3. Principios fundamentais
- Priorizar clareza, manutencao, estabilidade e seguranca.
- Nenhuma solucao deve existir apenas para funcionar.
- Toda alteracao deve considerar: impacto futuro, reuso, compatibilidade com producao e evolucao do sistema.

## 4. Escopo de atuacao do agente
O agente DEVE:
- Propor solucoes alinhadas ao padrao do projeto.
- Respeitar integralmente a arquitetura existente.
- Evitar reescritas desnecessarias.
- Manter compatibilidade com o sistema em producao.

O agente NAO DEVE:
- Criar solucoes paralelas para o mesmo problema.
- Introduzir dependencias sem justificativa tecnica clara.
- Alterar padroes sem alinhamento explicito com este documento.

## 5. Regras gerais de codigo
- Codigo deve ser legivel, simples e coeso.
- Evitar duplicacao; reutilizar servicos e utilitarios existentes.
- Preferir composicao em vez de heranca.
- Metodos devem ter responsabilidade unica.
- E proibido:
  - Logica oculta.
  - Efeitos colaterais inesperados.
  - Implementacoes temporarias sem registro tecnico.

### UX e comportamento obrigatorio
- Nenhuma acao pode exigir duplo clique.
- Todo botao de acao ou navegacao deve executar com 1 clique.
- E proibido:
  - Duplicar eventos (click + mouseup).
  - Multiplos listeners.
  - HostListener duplicado.
- Botoes dentro de `form`:
  - `type="button"` por padrao.
  - `type="submit"` apenas quando estritamente necessario.
- Evitar overlays capturando clique quando inativos (ajustar `pointer-events` e `z-index`).
- Todas as telas novas ou ja criadas precisam gerar popup de error message.
- Todo campo obrigatorio deve exibir (*) ao lado do titulo do campo e, ao clicar em "salvar" sem preenchimento, deve apresentar mensagem de erro clara ao usuario.

## 6. Regra geral - criacao de novas telas (obrigatoria)
AO CRIAR QUALQUER NOVA TELA NO SISTEMA G3, o agente DEVE:
- USAR COMO REFERENCIA O CADASTRO DE BENEFICIARIO DO G3.
- REPLICAR O PADRAO VISUAL, ESTRUTURAL E FUNCIONAL ja existente no sistema.

O padrao obrigatorio inclui, sem excecoes:
- Uso de `app-tela-padrao` como base da tela.
- Organizacao do conteudo em cards padronizados do G3.
- Uso de abas (tabs) sempre que houver separacao logica de informacoes.
- Aplicacao da barra de acoes CRUD padrao:
  - Buscar, Novo, Salvar, Cancelar, Excluir, Imprimir, Fechar.
- Uso dos componentes compartilhados oficiais.
- Manutencao das funcionalidades padrao:
  - Validacoes visuais e funcionais.
  - Mensagens de erro padronizadas.
  - Comportamento consistente de formularios e acoes.

E PROIBIDO:
- Criar telas fora do padrao visual do G3.
- Introduzir layouts alternativos ou fluxos divergentes.
- Ignorar cards, abas ou estruturas consolidadas.

Em caso de duvida, o agente DEVE copiar o padrao do cadastro de beneficiario.
Excecoes devem ser justificadas, documentadas e aprovadas previamente.

## 7. Padrao oficial de botoes de navegacao (G3)
- Ordem obrigatoria (linha unica): Buscar, Novo, Salvar, Cancelar, Excluir, Imprimir, Fechar.
- Regras:
  - Layout deve seguir o Cadastro de Beneficiario.
  - Excluir nao pode ficar colado em Salvar.
  - Fechar deve ser sempre o ultimo botao.

### Impressao
- Botao Imprimir (topo):
  - Se houver mais de uma opcao, abrir popup com lista.
  - Popups de impressao devem ter botao "X" para fechar e permitir fechamento via tecla ESC.

## 8. Angular - Frontend
- Proibido logica pesada em templates.
- Regras de negocio sempre em services.
- Tipagem estrita obrigatoria (evitar `any`).
- Componentes devem ser pequenos e focados.
- Separar smart e dumb quando aplicavel.
- Seguir os padroes visuais e estruturais definidos no README.md.

### Estrutura obrigatoria
- Todas as telas devem usar `app-tela-padrao`.
- `app-barra-acoes-crud`:
  - Canto superior direito.
  - Alinhada ao titulo.
  - Fora de cards e formularios.
  - "cancelar" deve limpar todos os campos, inclusive em novo cadastro.

### Titulos
- `page-title__eyebrow`: 0.95rem.
- `page-title__label`: 1.5rem, peso 800.
- Todos os titulos e subtitulos de telas novas ou existentes devem seguir exatamente o mesmo padrao do cadastro de beneficiario (espacamento, tamanho, peso e formato).
- Sempre que criar uma tela nova ou alterar uma tela existente, aplicar obrigatoriamente o padrao de titulo/subtitulo do cadastro de beneficiario.

### Listagens
- Usar como modelo a listagem de beneficiarios (filtros, cards, lista e paginacao).

### Comentario didatico
- Toda nova tela deve conter um comentario explicativo no topo direito da area de titulo.

## 9. Campos padronizados
- Capitalizacao obrigatoria (Primeira Letra Maiuscula).
- CPF:
  - Mascara `000.000.000-00`.
  - Validacao completa.
  - Campo invalido com borda vermelha.
- CNPJ:
  - Mascara `00.000.000/0000-00`.
  - Validacao completa.
- Endereco da unidade:
  - `cidade` com autocomplete (MG).
  - Preencher `estado` automaticamente.

## 10. Componentes compartilhados
- Autocomplete: usar exclusivamente `app-autocomplete`.
- Normalizacao de busca: sem acento e case-insensitive.
- Mensagens:
  - Formularios: `PopupErrorBuilder` + `app-popup-messages`.
  - Globais: `ErrorService` + `ToastComponent`.
  - Feedback local: temporario (10s) com botao "X".
- Confirmacoes: usar sempre `app-dialog`.
- Envio de email: sempre usar as configuracoes do servidor de email ja configurado no sistema (mesma configuracao usada no link "clique aqui para recuperar" da tela de login).

## 11. Relatorios - padrao oficial G3
- Formato A4, margens 20mm, fonte Arial.
- Cabecalho, corpo e rodape obrigatorios.
- Numeracao: Pagina X de Y.
- Dados sempre reais.
- HTML + PDF (via HTML).
- 1 clique apenas.
- Template unico reutilizavel.
- Todos os relatorios devem seguir este padrao, inclusive os existentes.

## 12. Backend - Java
- Camadas obrigatorias: Controller, Service, Repository, Domain.
- Uso obrigatorio de DTOs.
- Nunca expor entidades.
- Validar dados no backend.
- Tratar erros explicitamente.
- Priorizar imutabilidade.

### CORS
- Configurar corretamente para evitar 403 no preflight.
- Dev: http://localhost:4200
- Prod: apenas dominio oficial.

## 13. Seguranca
- Nunca confiar apenas no frontend.
- Nao expor dados sensiveis em logs.
- Aplicar principio do menor privilegio.
- Defender contra estados invalidos e fluxos incompletos.

## 14. Banco de dados
- Toda alteracao estrutural deve estar em `init.db`.
- Scripts idempotentes.
- Evitar `DO $$`.
- Nomes em portugues.
- PK `id` sequencial.
- FKs obrigatorias.
- Ao "colocar no banco":
  - Atualizar `init.db`.
  - Criar Domain, DTO, Repository, Service e Controller.

## 15. Prontuario do beneficiario
- Tabelas: `prontuario_registros`, `prontuario_anexos`.
- Uso de `dados_extra` (JSONB) para campos especificos.
- Novos eventos sempre geram novo registro.
- Anexos vinculados por FK com ON DELETE CASCADE.

## 16. Testes
- Criar/atualizar testes ao alterar comportamento.
- Priorizar fluxos criticos.
- Testes rapidos e deterministas.
- Codigo sem teste e excecao.

## 17. Versao e historico do sistema
- Atualizar versao em Configuracoes Gerais.
- Registrar versao, data/hora e mudancas.
- Formato obrigatorio: `1.00.0`.
- Incrementar apenas o ultimo grupo.
- Historico deve ser objetivo e sequencial.

## 18. Postura esperada do agente
- Ser direto e objetivo.
- Apontar riscos tecnicos.
- Nao assumir requisitos implicitos.
- Nao simplificar problemas complexos sem justificativa.
- Manter alinhamento total com a arquitetura e visao do G3.
