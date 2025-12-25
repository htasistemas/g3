package br.com.g3.cadastrobeneficiario.mapper;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.unidadeassistencial.domain.Endereco;
import java.time.LocalDateTime;

public class CadastroBeneficiarioMapper {
  private CadastroBeneficiarioMapper() {}

  public static CadastroBeneficiario toDomain(CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro = new CadastroBeneficiario();
    cadastro.setNomeCompleto(request.getNomeCompleto());
    cadastro.setNomeSocial(request.getNomeSocial());
    cadastro.setApelido(request.getApelido());
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setSexoBiologico(request.getSexoBiologico());
    cadastro.setIdentidadeGenero(request.getIdentidadeGenero());
    cadastro.setCorRaca(request.getCorRaca());
    cadastro.setEstadoCivil(request.getEstadoCivil());
    cadastro.setNacionalidade(request.getNacionalidade());
    cadastro.setNaturalidadeCidade(request.getNaturalidadeCidade());
    cadastro.setNaturalidadeUf(request.getNaturalidadeUf());
    cadastro.setNomeMae(request.getNomeMae());
    cadastro.setNomePai(request.getNomePai());
    LocalDateTime agora = LocalDateTime.now();
    cadastro.setCriadoEm(agora);
    cadastro.setAtualizadoEm(agora);
    cadastro.setEndereco(criarEndereco(request, agora));
    return cadastro;
  }

  public static void aplicarAtualizacao(CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request) {
    cadastro.setNomeCompleto(request.getNomeCompleto());
    cadastro.setNomeSocial(request.getNomeSocial());
    cadastro.setApelido(request.getApelido());
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setSexoBiologico(request.getSexoBiologico());
    cadastro.setIdentidadeGenero(request.getIdentidadeGenero());
    cadastro.setCorRaca(request.getCorRaca());
    cadastro.setEstadoCivil(request.getEstadoCivil());
    cadastro.setNacionalidade(request.getNacionalidade());
    cadastro.setNaturalidadeCidade(request.getNaturalidadeCidade());
    cadastro.setNaturalidadeUf(request.getNaturalidadeUf());
    cadastro.setNomeMae(request.getNomeMae());
    cadastro.setNomePai(request.getNomePai());
    cadastro.setAtualizadoEm(LocalDateTime.now());
    aplicarEndereco(cadastro, request);
  }

  public static CadastroBeneficiarioResponse toResponse(CadastroBeneficiario cadastro) {
    Endereco endereco = cadastro.getEndereco();
    return new CadastroBeneficiarioResponse(
        cadastro.getId(),
        cadastro.getNomeCompleto(),
        cadastro.getNomeSocial(),
        cadastro.getApelido(),
        cadastro.getDataNascimento(),
        cadastro.getSexoBiologico(),
        cadastro.getIdentidadeGenero(),
        cadastro.getCorRaca(),
        cadastro.getEstadoCivil(),
        cadastro.getNacionalidade(),
        cadastro.getNaturalidadeCidade(),
        cadastro.getNaturalidadeUf(),
        cadastro.getNomeMae(),
        cadastro.getNomePai(),
        endereco != null ? endereco.getCep() : null,
        endereco != null ? endereco.getLogradouro() : null,
        endereco != null ? endereco.getNumero() : null,
        endereco != null ? endereco.getComplemento() : null,
        endereco != null ? endereco.getBairro() : null,
        endereco != null ? endereco.getPontoReferencia() : null,
        endereco != null ? endereco.getCidade() : null,
        endereco != null ? endereco.getZona() : null,
        endereco != null ? endereco.getSubzona() : null,
        endereco != null ? endereco.getEstado() : null,
        cadastro.getCriadoEm(),
        cadastro.getAtualizadoEm());
  }

  private static Endereco criarEndereco(CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEndereco(request)) {
      return null;
    }
    Endereco endereco = new Endereco();
    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
    endereco.setCriadoEm(agora);
    endereco.setAtualizadoEm(agora);
    return endereco;
  }

  private static void aplicarEndereco(CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request) {
    if (!possuiDadosEndereco(request)) {
      cadastro.setEndereco(null);
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    Endereco endereco = cadastro.getEndereco();

    if (endereco == null) {
      endereco = new Endereco();
      endereco.setCriadoEm(agora);
      cadastro.setEndereco(endereco);
    } else if (endereco.getCriadoEm() == null) {
      endereco.setCriadoEm(agora);
    }

    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
    endereco.setAtualizadoEm(agora);
  }

  private static boolean possuiDadosEndereco(CadastroBeneficiarioCriacaoRequest request) {
    return temValor(request.getCep())
        || temValor(request.getLogradouro())
        || temValor(request.getNumero())
        || temValor(request.getComplemento())
        || temValor(request.getBairro())
        || temValor(request.getPontoReferencia())
        || temValor(request.getMunicipio())
        || temValor(request.getZona())
        || temValor(request.getSubzona())
        || temValor(request.getUf());
  }

  private static boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }
}
