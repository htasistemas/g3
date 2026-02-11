package br.com.g3.rh.serviceimpl;

import br.com.g3.auditoria.service.AuditoriaService;
import br.com.g3.rh.domain.RhConfiguracaoPonto;
import br.com.g3.rh.domain.RhLocalPonto;
import br.com.g3.rh.domain.RhPontoDia;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.repository.RhConfiguracaoPontoRepository;
import br.com.g3.rh.repository.RhLocalPontoRepository;
import br.com.g3.rh.repository.RhPontoAuditoriaRepository;
import br.com.g3.rh.repository.RhPontoDiaRepository;
import br.com.g3.rh.repository.RhPontoMarcacaoRepository;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class RhPontoServiceImplTeste {
  @Mock private RhLocalPontoRepository localRepository;
  @Mock private RhConfiguracaoPontoRepository configuracaoRepository;
  @Mock private RhPontoDiaRepository pontoDiaRepository;
  @Mock private RhPontoMarcacaoRepository marcacaoRepository;
  @Mock private RhPontoAuditoriaRepository auditoriaRepository;
  @Mock private UsuarioRepository usuarioRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuditoriaService auditoriaService;

  @InjectMocks private RhPontoServiceImpl service;

  private RhLocalPonto local;
  private RhConfiguracaoPonto configuracao;
  private Usuario usuario;

  @BeforeEach
  void setup() {
    local = new RhLocalPonto();
    local.setId(1L);
    local.setNome("Sede");
    local.setLatitude(-19.9);
    local.setLongitude(-43.9);
    local.setRaioMetros(100);
    local.setAccuracyMaxMetros(80);
    local.setAtivo(true);
    local.setCriadoEm(LocalDateTime.now());
    local.setAtualizadoEm(LocalDateTime.now());

    configuracao = new RhConfiguracaoPonto();
    configuracao.setId(1L);
    configuracao.setCargaSegQuiMinutos(540);
    configuracao.setCargaSextaMinutos(240);
    configuracao.setCargaSabadoMinutos(0);
    configuracao.setCargaDomingoMinutos(0);
    configuracao.setCargaSemanalMinutos(2400);
    configuracao.setToleranciaMinutos(10);
    configuracao.setAtualizadoEm(LocalDateTime.now());

    usuario = new Usuario();
    usuario.setId(10L);
    usuario.setSenhaHash("hash");
  }

  @Test
  void baterPonto_deveNegarQuandoSenhaInvalida() {
    RhPontoBaterRequest request = new RhPontoBaterRequest();
    request.setFuncionarioId(10L);
    request.setTipo("E1");
    request.setSenha("errada");
    request.setLatitude(-19.9);
    request.setLongitude(-43.9);
    request.setAccuracy(10.0);

    Mockito.when(usuarioRepository.buscarPorId(10L)).thenReturn(Optional.of(usuario));
    Mockito.when(passwordEncoder.matches(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);

    ResponseStatusException ex = Assertions.assertThrows(
        ResponseStatusException.class,
        () -> service.baterPonto(request, "127.0.0.1", "tester"));

    Assertions.assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    Mockito.verify(auditoriaRepository, Mockito.times(1)).salvar(ArgumentMatchers.any());
  }

  @Test
  void baterPonto_deveValidarSequencia() {
    RhPontoBaterRequest request = new RhPontoBaterRequest();
    request.setFuncionarioId(10L);
    request.setTipo("S1");
    request.setSenha("ok");
    request.setLatitude(-19.9);
    request.setLongitude(-43.9);
    request.setAccuracy(10.0);

    Mockito.when(usuarioRepository.buscarPorId(10L)).thenReturn(Optional.of(usuario));
    Mockito.when(passwordEncoder.matches(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
    Mockito.when(localRepository.buscarPrimeiroAtivo()).thenReturn(Optional.of(local));
    Mockito.when(configuracaoRepository.buscarAtual()).thenReturn(Optional.of(configuracao));

    RhPontoDia pontoDia = new RhPontoDia();
    pontoDia.setId(1L);
    pontoDia.setFuncionarioId(10L);
    pontoDia.setData(LocalDate.now());
    pontoDia.setOcorrencia("NORMAL");
    pontoDia.setCargaPrevistaMinutos(540);
    pontoDia.setToleranciaMinutos(10);
    pontoDia.setTotalTrabalhadoMinutos(0);
    pontoDia.setExtrasMinutos(0);
    pontoDia.setFaltasAtrasosMinutos(0);
    pontoDia.setCriadoEm(LocalDateTime.now());
    pontoDia.setAtualizadoEm(LocalDateTime.now());

    Mockito.when(pontoDiaRepository.buscarPorFuncionarioEData(10L, LocalDate.now()))
        .thenReturn(Optional.of(pontoDia));

    ResponseStatusException ex = Assertions.assertThrows(
        ResponseStatusException.class,
        () -> service.baterPonto(request, "127.0.0.1", "tester"));

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }
}
