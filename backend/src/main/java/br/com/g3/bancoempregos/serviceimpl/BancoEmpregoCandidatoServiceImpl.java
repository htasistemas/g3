package br.com.g3.bancoempregos.serviceimpl;

import br.com.g3.bancoempregos.domain.BancoEmpregoCandidato;
import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoResponse;
import br.com.g3.bancoempregos.repository.BancoEmpregoCandidatoRepository;
import br.com.g3.bancoempregos.service.BancoEmpregoCandidatoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BancoEmpregoCandidatoServiceImpl implements BancoEmpregoCandidatoService {
  private final BancoEmpregoCandidatoRepository repository;

  public BancoEmpregoCandidatoServiceImpl(BancoEmpregoCandidatoRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<BancoEmpregoCandidatoResponse> listar(Long empregoId) {
    if (empregoId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Emprego nao informado.");
    }
    return repository.listarPorEmprego(empregoId).stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public BancoEmpregoCandidatoResponse criar(
      Long empregoId, BancoEmpregoCandidatoRequest request) {
    if (empregoId == null || request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados invalidos.");
    }
    if (request.getBeneficiarioNome() == null || request.getBeneficiarioNome().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario nao informado.");
    }

    BancoEmpregoCandidato candidato = new BancoEmpregoCandidato();
    candidato.setEmpregoId(empregoId);
    candidato.setBeneficiarioId(request.getBeneficiarioId());
    candidato.setBeneficiarioNome(request.getBeneficiarioNome().trim());
    candidato.setNecessidadesProfissionais(request.getNecessidadesProfissionais());
    candidato.setStatus(normalizarStatus(request.getStatus()));
    candidato.setCurriculoNome(request.getCurriculoNome());
    candidato.setCurriculoTipo(request.getCurriculoTipo());
    candidato.setCurriculoConteudo(request.getCurriculoConteudo());

    BancoEmpregoCandidato salvo = repository.salvar(candidato);
    return toResponse(salvo);
  }

  @Override
  public void remover(Long id) {
    BancoEmpregoCandidato candidato =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(candidato);
  }

  private BancoEmpregoCandidatoResponse toResponse(BancoEmpregoCandidato candidato) {
    return new BancoEmpregoCandidatoResponse(
        candidato.getId(),
        candidato.getEmpregoId(),
        candidato.getBeneficiarioId(),
        candidato.getBeneficiarioNome(),
        candidato.getNecessidadesProfissionais(),
        candidato.getStatus(),
        candidato.getCurriculoNome(),
        candidato.getCurriculoTipo(),
        candidato.getCurriculoConteudo(),
        candidato.getCriadoEm());
  }

  private String normalizarStatus(String status) {
    if (status == null || status.trim().isEmpty()) {
      return "EM_ANALISE";
    }
    return status.trim().toUpperCase();
  }
}
