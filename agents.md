# AGENTS.md – Diretrizes para Agentes e Contribuições (G3)

Este documento define **como agentes humanos ou assistentes de IA devem atuar** ao contribuir com o sistema **G3 – Sistema de Gestão do Terceiro Setor**.

O objetivo é garantir **consistência técnica**, **qualidade de código**, **segurança** e **evolução sustentável** do sistema.

---

## Idioma
- **Sempre comunicar em português**.
- Nomes técnicos podem permanecer em inglês quando fizer sentido (ex.: DTO, Service, Repository).

---

## Princípios Fundamentais
- O sistema G3 deve priorizar:
  - Clareza
  - Manutenção
  - Estabilidade
  - Segurança
- Nenhuma solução deve ser implementada apenas “para funcionar”.
- Toda alteração deve considerar impacto futuro.

---

## Escopo de Atuação dos Agentes
Agentes devem:
- Sugerir soluções alinhadas ao padrão do projeto.
- Respeitar a arquitetura existente.
- Evitar reescritas desnecessárias.
- Manter compatibilidade com o que já está em produção.

Agentes **não devem**:
- Criar soluções paralelas para o mesmo problema.
- Introduzir dependências sem justificativa clara.
- Alterar padrões sem alinhar com este documento.

---

## Regras de Código
- Código deve ser:
  - Legível
  - Simples
  - Coeso
- Evitar duplicação; reutilizar serviços e utilitários existentes.
- Preferir composição a herança.
- Métodos devem ter responsabilidade única.
- Evitar lógica oculta ou efeitos colaterais inesperados.
- Todas as telas novas ou ja criadas precisam ser geradas o popup error message

---

## Angular (Frontend)
- Não adicionar lógica pesada em templates.
- Sempre usar serviços para regras de negócio.
- Tipagem estrita é obrigatória; evitar `any`.
- Seguir os padrões visuais e estruturais definidos no README.md.
- Componentes devem ser pequenos e focados.
- Separar smart/dumb components quando aplicável.

### Componentes Compartilhados
- **Autocomplete**:
  - Usar exclusivamente `app-autocomplete`.
  - Normalizar buscas (case-insensitive e sem acento) no frontend e backend.
- **Mensagens**:
  - Formulários: `PopupErrorBuilder` + `app-popup-messages`
  - Globais: `ErrorService` + `ToastComponent`
- **Confirmações**:
  - Usar sempre `app-dialog`

---

## Backend (Java)
- Manter separação clara de camadas:
  - Controller
  - Service
  - Repository
  - Domain
- Usar DTOs para entrada e saída.
- Nunca expor entidades diretamente.
- Validar dados no backend mesmo que já validados no frontend.
- Priorizar imutabilidade quando possível.
- Tratar erros de forma explícita e previsível.

---

## Segurança
- Nunca confiar apenas em dados vindos do frontend.
- Evitar exposição de informações sensíveis em logs.
- Aplicar princípio do menor privilégio.
- Criar defesas contra:
  - Registros nulos
  - Estados inválidos
  - Fluxos incompletos

---

## Banco de Dados
- Toda alteração estrutural deve estar em `init.sql`.
- Scripts devem ser **idempotentes**.
- Evitar blocos `DO $$`.
- Usar nomes em português, claros e objetivos.
- Modelagem relacional correta é obrigatória.
- Sempre usar `id` sequencial como chave primária.

---

## Testes
- Criar ou atualizar testes sempre que alterar comportamento.
- Priorizar testes de fluxos críticos.
- Testes devem ser determinísticos e rápidos.
- Código sem teste deve ser exceção, não regra.

---

## Revisões e Mudanças
- Mudanças devem ser pequenas e com propósito claro.
- Não misturar refatoração com novas funcionalidades.
- Decisões importantes devem ser documentadas.
- Atualizar README.md ou documentação técnica quando necessário.

---

## Postura Esperada do Agente
- Ser direto e objetivo.
- Apontar riscos técnicos quando existirem.
- Não assumir requisitos não informados.
- Não simplificar problemas complexos sem justificativa.
- Manter alinhamento com a arquitetura e visão do sistema G3.
