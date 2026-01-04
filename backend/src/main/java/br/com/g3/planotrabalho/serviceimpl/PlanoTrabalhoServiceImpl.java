package br.com.g3.planotrabalho.serviceimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalho;
import br.com.g3.planotrabalho.domain.PlanoTrabalhoAtividade;
import br.com.g3.planotrabalho.domain.PlanoTrabalhoCronograma;
import br.com.g3.planotrabalho.domain.PlanoTrabalhoEquipe;
import br.com.g3.planotrabalho.domain.PlanoTrabalhoEtapa;
import br.com.g3.planotrabalho.domain.PlanoTrabalhoMeta;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoAtividadeRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoAtividadeResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoCronogramaRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoCronogramaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoEquipeRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoEquipeResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoEtapaRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoEtapaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoListaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoMetaRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoMetaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoTermoResumoResponse;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoAtividadeRepository;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoCronogramaRepository;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoEquipeRepository;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoEtapaRepository;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoMetaRepository;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoRepository;
import br.com.g3.planotrabalho.service.PlanoTrabalhoService;
import br.com.g3.termofomento.domain.TermoFomento;
import br.com.g3.termofomento.repository.TermoFomentoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlanoTrabalhoServiceImpl implements PlanoTrabalhoService {
  private final PlanoTrabalhoRepository repository;
  private final PlanoTrabalhoMetaRepository metaRepository;
  private final PlanoTrabalhoAtividadeRepository atividadeRepository;
  private final PlanoTrabalhoEtapaRepository etapaRepository;
  private final PlanoTrabalhoCronogramaRepository cronogramaRepository;
  private final PlanoTrabalhoEquipeRepository equipeRepository;
  private final TermoFomentoRepository termoFomentoRepository;

  public PlanoTrabalhoServiceImpl(
      PlanoTrabalhoRepository repository,
      PlanoTrabalhoMetaRepository metaRepository,
      PlanoTrabalhoAtividadeRepository atividadeRepository,
      PlanoTrabalhoEtapaRepository etapaRepository,
      PlanoTrabalhoCronogramaRepository cronogramaRepository,
      PlanoTrabalhoEquipeRepository equipeRepository,
      TermoFomentoRepository termoFomentoRepository) {
    this.repository = repository;
    this.metaRepository = metaRepository;
    this.atividadeRepository = atividadeRepository;
    this.etapaRepository = etapaRepository;
    this.cronogramaRepository = cronogramaRepository;
    this.equipeRepository = equipeRepository;
    this.termoFomentoRepository = termoFomentoRepository;
  }

  @Override
  public PlanoTrabalhoListaResponse listar() {
    List<PlanoTrabalho> planos = repository.listar();
    List<PlanoTrabalhoResponse> resposta = new ArrayList<>();
    for (PlanoTrabalho plano : planos) {
      resposta.add(mapPlano(plano));
    }
    return new PlanoTrabalhoListaResponse(resposta);
  }

  @Override
  public PlanoTrabalhoResponse obter(Long id) {
    return mapPlano(buscarPlano(id));
  }

  @Override
  @Transactional
  public PlanoTrabalhoResponse criar(PlanoTrabalhoRequest request) {
    PlanoTrabalho plano = mapPlanoRequest(new PlanoTrabalho(), request);
    LocalDateTime agora = LocalDateTime.now();
    plano.setCriadoEm(agora);
    plano.setAtualizadoEm(agora);
    if (plano.getCodigoInterno() == null || plano.getCodigoInterno().trim().isEmpty()) {
      plano.setCodigoInterno(gerarCodigoInterno());
    }
    PlanoTrabalho salvo = repository.salvar(plano);
    salvarEstrutura(salvo.getId(), request);
    return mapPlano(buscarPlano(salvo.getId()));
  }

  @Override
  @Transactional
  public PlanoTrabalhoResponse atualizar(Long id, PlanoTrabalhoRequest request) {
    PlanoTrabalho plano = buscarPlano(id);
    mapPlanoRequest(plano, request);
    plano.setAtualizadoEm(LocalDateTime.now());
    PlanoTrabalho salvo = repository.salvar(plano);
    removerEstrutura(salvo.getId());
    salvarEstrutura(salvo.getId(), request);
    return mapPlano(buscarPlano(salvo.getId()));
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    PlanoTrabalho plano = buscarPlano(id);
    removerEstrutura(id);
    repository.remover(plano);
  }

  private PlanoTrabalho buscarPlano(Long id) {
    Optional<PlanoTrabalho> plano = repository.buscarPorId(id);
    if (!plano.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano de trabalho nao encontrado.");
    }
    return plano.get();
  }

  private void removerEstrutura(Long planoId) {
    cronogramaRepository.removerPorPlano(planoId);
    equipeRepository.removerPorPlano(planoId);
    List<PlanoTrabalhoMeta> metas = metaRepository.listarPorPlano(planoId);
    for (PlanoTrabalhoMeta meta : metas) {
      List<PlanoTrabalhoAtividade> atividades = atividadeRepository.listarPorMeta(meta.getId());
      for (PlanoTrabalhoAtividade atividade : atividades) {
        etapaRepository.removerPorAtividade(atividade.getId());
      }
      atividadeRepository.removerPorMeta(meta.getId());
    }
    metaRepository.removerPorPlano(planoId);
  }

  private void salvarEstrutura(Long planoId, PlanoTrabalhoRequest request) {
    LocalDateTime agora = LocalDateTime.now();
    if (request.getMetas() != null) {
      for (PlanoTrabalhoMetaRequest metaRequest : request.getMetas()) {
        PlanoTrabalhoMeta meta = new PlanoTrabalhoMeta();
        meta.setPlanoTrabalhoId(planoId);
        meta.setCodigo(metaRequest.getCodigo());
        meta.setDescricao(metaRequest.getDescricao());
        meta.setIndicador(metaRequest.getIndicador());
        meta.setUnidadeMedida(metaRequest.getUnidadeMedida());
        meta.setQuantidadePrevista(metaRequest.getQuantidadePrevista());
        meta.setResultadoEsperado(metaRequest.getResultadoEsperado());
        meta.setCriadoEm(agora);
        PlanoTrabalhoMeta metaSalva = metaRepository.salvar(meta);
        if (metaRequest.getAtividades() != null) {
          for (PlanoTrabalhoAtividadeRequest atividadeRequest : metaRequest.getAtividades()) {
            PlanoTrabalhoAtividade atividade = new PlanoTrabalhoAtividade();
            atividade.setMetaId(metaSalva.getId());
            atividade.setDescricao(atividadeRequest.getDescricao());
            atividade.setJustificativa(atividadeRequest.getJustificativa());
            atividade.setPublicoAlvo(atividadeRequest.getPublicoAlvo());
            atividade.setLocalExecucao(atividadeRequest.getLocalExecucao());
            atividade.setProdutoEsperado(atividadeRequest.getProdutoEsperado());
            atividade.setCriadoEm(agora);
            PlanoTrabalhoAtividade atividadeSalva = atividadeRepository.salvar(atividade);
            if (atividadeRequest.getEtapas() != null) {
              for (PlanoTrabalhoEtapaRequest etapaRequest : atividadeRequest.getEtapas()) {
                PlanoTrabalhoEtapa etapa = new PlanoTrabalhoEtapa();
                etapa.setAtividadeId(atividadeSalva.getId());
                etapa.setDescricao(etapaRequest.getDescricao());
                etapa.setStatus(etapaRequest.getStatus());
                etapa.setDataInicioPrevista(etapaRequest.getDataInicioPrevista());
                etapa.setDataFimPrevista(etapaRequest.getDataFimPrevista());
                etapa.setDataConclusao(etapaRequest.getDataConclusao());
                etapa.setResponsavel(etapaRequest.getResponsavel());
                etapa.setCriadoEm(agora);
                etapaRepository.salvar(etapa);
              }
            }
          }
        }
      }
    }
    if (request.getCronograma() != null) {
      for (PlanoTrabalhoCronogramaRequest cronogramaRequest : request.getCronograma()) {
        PlanoTrabalhoCronograma cronograma = new PlanoTrabalhoCronograma();
        cronograma.setPlanoTrabalhoId(planoId);
        cronograma.setReferenciaTipo(cronogramaRequest.getReferenciaTipo());
        cronograma.setReferenciaId(cronogramaRequest.getReferenciaId());
        cronograma.setReferenciaDescricao(cronogramaRequest.getReferenciaDescricao());
        cronograma.setCompetencia(cronogramaRequest.getCompetencia());
        cronograma.setDescricaoResumida(cronogramaRequest.getDescricaoResumida());
        cronograma.setValorPrevisto(cronogramaRequest.getValorPrevisto());
        cronograma.setFonteRecurso(cronogramaRequest.getFonteRecurso());
        cronograma.setNaturezaDespesa(cronogramaRequest.getNaturezaDespesa());
        cronograma.setObservacoes(cronogramaRequest.getObservacoes());
        cronograma.setCriadoEm(agora);
        cronogramaRepository.salvar(cronograma);
      }
    }
    if (request.getEquipe() != null) {
      for (PlanoTrabalhoEquipeRequest equipeRequest : request.getEquipe()) {
        PlanoTrabalhoEquipe equipe = new PlanoTrabalhoEquipe();
        equipe.setPlanoTrabalhoId(planoId);
        equipe.setNome(equipeRequest.getNome());
        equipe.setFuncao(equipeRequest.getFuncao());
        equipe.setCpf(equipeRequest.getCpf());
        equipe.setCargaHoraria(equipeRequest.getCargaHoraria());
        equipe.setTipoVinculo(equipeRequest.getTipoVinculo());
        equipe.setContato(equipeRequest.getContato());
        equipe.setCriadoEm(agora);
        equipeRepository.salvar(equipe);
      }
    }
  }

  private PlanoTrabalho mapPlanoRequest(PlanoTrabalho plano, PlanoTrabalhoRequest request) {
    plano.setCodigoInterno(request.getCodigoInterno());
    plano.setTitulo(request.getTitulo());
    plano.setDescricaoGeral(request.getDescricaoGeral());
    plano.setStatus(request.getStatus());
    plano.setOrgaoConcedente(request.getOrgaoConcedente());
    plano.setOrgaoOutroDescricao(request.getOrgaoOutroDescricao());
    plano.setAreaPrograma(request.getAreaPrograma());
    plano.setDataElaboracao(request.getDataElaboracao());
    plano.setDataAprovacao(request.getDataAprovacao());
    plano.setVigenciaInicio(request.getVigenciaInicio());
    plano.setVigenciaFim(request.getVigenciaFim());
    plano.setTermoFomentoId(request.getTermoFomentoId());
    plano.setNumeroProcesso(request.getNumeroProcesso());
    plano.setModalidade(request.getModalidade());
    plano.setObservacoesVinculacao(request.getObservacoesVinculacao());
    plano.setArquivoFormato(request.getArquivoFormato());
    return plano;
  }

  private PlanoTrabalhoResponse mapPlano(PlanoTrabalho plano) {
    List<PlanoTrabalhoMetaResponse> metas = new ArrayList<>();
    List<PlanoTrabalhoMeta> metasDb = metaRepository.listarPorPlano(plano.getId());
    for (PlanoTrabalhoMeta meta : metasDb) {
      List<PlanoTrabalhoAtividadeResponse> atividades = new ArrayList<>();
      List<PlanoTrabalhoAtividade> atividadesDb = atividadeRepository.listarPorMeta(meta.getId());
      for (PlanoTrabalhoAtividade atividade : atividadesDb) {
        List<PlanoTrabalhoEtapaResponse> etapas = new ArrayList<>();
        for (PlanoTrabalhoEtapa etapa : etapaRepository.listarPorAtividade(atividade.getId())) {
          etapas.add(
              new PlanoTrabalhoEtapaResponse(
                  etapa.getId(),
                  etapa.getDescricao(),
                  etapa.getStatus(),
                  etapa.getDataInicioPrevista(),
                  etapa.getDataFimPrevista(),
                  etapa.getDataConclusao(),
                  etapa.getResponsavel()));
        }
        atividades.add(
            new PlanoTrabalhoAtividadeResponse(
                atividade.getId(),
                atividade.getDescricao(),
                atividade.getJustificativa(),
                atividade.getPublicoAlvo(),
                atividade.getLocalExecucao(),
                atividade.getProdutoEsperado(),
                etapas));
      }
      metas.add(
          new PlanoTrabalhoMetaResponse(
              meta.getId(),
              meta.getCodigo(),
              meta.getDescricao(),
              meta.getIndicador(),
              meta.getUnidadeMedida(),
              meta.getQuantidadePrevista(),
              meta.getResultadoEsperado(),
              atividades));
    }
    List<PlanoTrabalhoCronogramaResponse> cronogramas = new ArrayList<>();
    for (PlanoTrabalhoCronograma cronograma : cronogramaRepository.listarPorPlano(plano.getId())) {
      cronogramas.add(
          new PlanoTrabalhoCronogramaResponse(
              cronograma.getId(),
              cronograma.getReferenciaTipo(),
              cronograma.getReferenciaId(),
              cronograma.getReferenciaDescricao(),
              cronograma.getCompetencia(),
              cronograma.getDescricaoResumida(),
              cronograma.getValorPrevisto(),
              cronograma.getFonteRecurso(),
              cronograma.getNaturezaDespesa(),
              cronograma.getObservacoes()));
    }
    List<PlanoTrabalhoEquipeResponse> equipe = new ArrayList<>();
    for (PlanoTrabalhoEquipe membro : equipeRepository.listarPorPlano(plano.getId())) {
      equipe.add(
          new PlanoTrabalhoEquipeResponse(
              membro.getId(),
              membro.getNome(),
              membro.getFuncao(),
              membro.getCpf(),
              membro.getCargaHoraria(),
              membro.getTipoVinculo(),
              membro.getContato()));
    }
    PlanoTrabalhoTermoResumoResponse termoResumo = null;
    Optional<TermoFomento> termo = termoFomentoRepository.buscarPorId(plano.getTermoFomentoId());
    if (termo.isPresent()) {
      TermoFomento termoEncontrado = termo.get();
      termoResumo =
          new PlanoTrabalhoTermoResumoResponse(
              termoEncontrado.getId(),
              termoEncontrado.getNumeroTermo(),
              termoEncontrado.getDescricaoObjeto());
    }
    return new PlanoTrabalhoResponse(
        plano.getId(),
        plano.getCodigoInterno(),
        plano.getTitulo(),
        plano.getDescricaoGeral(),
        plano.getStatus(),
        plano.getOrgaoConcedente(),
        plano.getOrgaoOutroDescricao(),
        plano.getAreaPrograma(),
        plano.getDataElaboracao(),
        plano.getDataAprovacao(),
        plano.getVigenciaInicio(),
        plano.getVigenciaFim(),
        plano.getTermoFomentoId(),
        plano.getNumeroProcesso(),
        plano.getModalidade(),
        plano.getObservacoesVinculacao(),
        metas,
        cronogramas,
        equipe,
        plano.getArquivoFormato(),
        termoResumo);
  }

  private String gerarCodigoInterno() {
    String base = String.valueOf(System.currentTimeMillis());
    return "PT-" + base.substring(base.length() - 6);
  }
}
