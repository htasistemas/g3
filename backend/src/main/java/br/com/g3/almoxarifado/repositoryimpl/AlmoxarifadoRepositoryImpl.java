package br.com.g3.almoxarifado.repositoryimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import br.com.g3.almoxarifado.repository.AlmoxarifadoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlmoxarifadoRepositoryImpl implements AlmoxarifadoRepository {
  private final AlmoxarifadoItemJpaRepository itemRepository;
  private final AlmoxarifadoMovimentacaoJpaRepository movimentacaoRepository;
  private final JdbcTemplate jdbcTemplate;

  public AlmoxarifadoRepositoryImpl(
      AlmoxarifadoItemJpaRepository itemRepository,
      AlmoxarifadoMovimentacaoJpaRepository movimentacaoRepository,
      JdbcTemplate jdbcTemplate) {
    this.itemRepository = itemRepository;
    this.movimentacaoRepository = movimentacaoRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<AlmoxarifadoItem> listarItens() {
    return itemRepository.findAll();
  }

  @Override
  public Optional<AlmoxarifadoItem> buscarItemPorId(Long id) {
    return itemRepository.findById(id);
  }

  @Override
  public Optional<AlmoxarifadoItem> buscarItemPorCodigo(String codigo) {
    return itemRepository.findByCodigo(codigo);
  }

  @Override
  public Optional<AlmoxarifadoItem> buscarItemPorCodigoBarras(String codigoBarras) {
    return itemRepository.findByCodigoBarras(codigoBarras);
  }

  @Override
  public Optional<AlmoxarifadoItem> buscarItemDuplicado(
      String descricao,
      String categoria,
      String unidade,
      String localizacao,
      String localizacaoInterna) {
    return itemRepository.buscarDuplicado(descricao, categoria, unidade, localizacao, localizacaoInterna);
  }

  @Override
  public AlmoxarifadoItem salvarItem(AlmoxarifadoItem item) {
    return itemRepository.save(item);
  }

  @Override
  public List<AlmoxarifadoMovimentacao> listarMovimentacoes() {
    return movimentacaoRepository.findAllByOrderByDataMovimentacaoDescIdDesc();
  }

  @Override
  public AlmoxarifadoMovimentacao salvarMovimentacao(AlmoxarifadoMovimentacao movimentacao) {
    return movimentacaoRepository.save(movimentacao);
  }

  @Override
  public int obterProximoCodigo() {
    String sql =
        "SELECT COALESCE(MAX(CAST(codigo AS INTEGER)), 0) FROM almoxarifado_item " +
        "WHERE codigo ~ '^[0-9]+$'";
    Integer total = jdbcTemplate.queryForObject(sql, Integer.class);
    return (total != null ? total : 0) + 1;
  }
}
