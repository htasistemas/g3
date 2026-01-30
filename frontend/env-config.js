(function (window) {
  window.__env = window.__env || {};

  // URL base da API que o Angular deve consumir. Se não for informada, usa a origem atual.
  window.__env.apiUrl = 'apig3.htasistemas.com.br';

  // Caso queira controlar múltiplos valores, adicione novas chaves aqui e consuma
  // dentro da aplicação por meio de window.__env.<nome>. Exemplo:
  // window.__env.appVersion = '1.0.0';
  // window.__env.featureFlag = false;
})(this);
