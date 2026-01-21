package br.com.g3.chamadas.beneficiario.service;

import br.com.g3.chamadas.beneficiario.dto.BeneficiarioRespostaDto;
import br.com.g3.chamadas.beneficiario.entity.BeneficiarioEntity;
import br.com.g3.chamadas.beneficiario.repository.BeneficiarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BeneficiarioService {
  private final BeneficiarioRepository beneficiarioRepository;

  public BeneficiarioService(BeneficiarioRepository beneficiarioRepository) {
    this.beneficiarioRepository = beneficiarioRepository;
  }

  public List<BeneficiarioRespostaDto> listarAtivos() {
    List<BeneficiarioEntity> beneficiarios = beneficiarioRepository.findByAtivoTrueOrderByNomeBeneficiarioAsc();
    return beneficiarios.stream()
        .map(item -> new BeneficiarioRespostaDto(item.getIdBeneficiario(), item.getNomeBeneficiario()))
        .collect(Collectors.toList());
  }
}
