package br.com.g3.usuario.serviceimpl;

import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.repository.PermissaoRepository;
import br.com.g3.usuario.service.PermissaoService;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PermissaoServiceImpl implements PermissaoService {
  private final PermissaoRepository repository;

  public PermissaoServiceImpl(PermissaoRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<String> listarNomes() {
    return repository.findAll().stream().map(Permissao::getNome).collect(Collectors.toList());
  }

  @Override
  public List<Permissao> listar() {
    return repository.findAll();
  }

  @Override
  public List<Permissao> buscarPorNomes(Collection<String> nomes) {
    if (nomes == null || nomes.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "É necessário fornecer ao menos uma permissão.");
    }
    Map<String, Permissao> mapa =
        repository.findAll().stream()
            .collect(Collectors.toMap(p -> p.getNome().toLowerCase(Locale.ROOT), p -> p));

    List<Permissao> encontradas =
        nomes.stream()
            .map(nome -> mapa.get(nome.toLowerCase(Locale.ROOT)))
            .filter(p -> p != null)
            .collect(Collectors.toList());

    if (encontradas.size() != nomes.size()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Algumas permissões informadas não existem.");
    }

    return encontradas;
  }
}
