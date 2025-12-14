package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.PublicoAlvoCampanha;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPrioridadeMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.service.internal.analytics.DashboardService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.DepartamentoService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.EmpresaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PessoaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.PublicoAlvoCampanhaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio.GeradorRelatorioService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.CampanhaMensageriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SistelcomService {

    private final CampanhaMensageriaService campanhaMensageriaService;
    private final PublicoAlvoCampanhaService publicoAlvoCampanhaService;
    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final DashboardService dashboardService;
    private final GeradorRelatorioService geradorRelatorioService;

    public SistelcomService(CampanhaMensageriaService campanhaMensageriaService, PublicoAlvoCampanhaService publicoAlvoCampanhaService, EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, DashboardService dashboardService, GeradorRelatorioService geradorRelatorioService) {
        this.campanhaMensageriaService = campanhaMensageriaService;
        this.publicoAlvoCampanhaService = publicoAlvoCampanhaService;
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.dashboardService = dashboardService;
        this.geradorRelatorioService = geradorRelatorioService;
    }

    @Transactional
    public byte[] agendarEnvioEmLote(PedidoDisparoCanalMensageriaSMSRequest pedidoDisparoCanalMensageriaSMSRequest, SistelcomDTO sistelcomDTO, EstoqueMercadoriaVirtual estoqueMercadoriaVirtual, AcessoUsuario usuario) throws IOException {

        if (sistelcomDTO != null && sistelcomDTO.getSmsDestinatarios() != null && !sistelcomDTO.getSmsDestinatarios().isEmpty()) {
            CampanhaMensageria novaCampanhaMensageria = new CampanhaMensageria();

            novaCampanhaMensageria.setCampanha(pedidoDisparoCanalMensageriaSMSRequest.getCampanha());
            novaCampanhaMensageria.setIdUsuarioEnvio(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdUsuario());
            novaCampanhaMensageria.setTipoPessoa(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getTipoPessoa());
            novaCampanhaMensageria.setIdSubjectus(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdSubjectus());
            novaCampanhaMensageria.setIdDepartamento(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdDepartamento());
            novaCampanhaMensageria.setIdColaborador(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdColaborador() != null ? pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdColaborador() : null);

            novaCampanhaMensageria.setIdCanalMensageria(pedidoDisparoCanalMensageriaSMSRequest.getIdCanalMensageria());

            novaCampanhaMensageria.setDataInicio(LocalDateTime.now());
            novaCampanhaMensageria.setDataAgendada(pedidoDisparoCanalMensageriaSMSRequest.getDataAgendada());
            novaCampanhaMensageria.setDataAgendadaFinal(pedidoDisparoCanalMensageriaSMSRequest.getDataAgendadaFinal() != null ? pedidoDisparoCanalMensageriaSMSRequest.getDataAgendadaFinal() : null);

            novaCampanhaMensageria.setIdStatusCampanha(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico());

            novaCampanhaMensageria.setTotal(sistelcomDTO.getTotal());
            novaCampanhaMensageria.setInvalido(sistelcomDTO.getInvalidos());
            novaCampanhaMensageria.setValido(sistelcomDTO.getValidos());
            novaCampanhaMensageria.setHigienizado(sistelcomDTO.getHigienizados());

            novaCampanhaMensageria.setOrdemFatiamentoLote(pedidoDisparoCanalMensageriaSMSRequest.getQuantidadeLotes() != null ? pedidoDisparoCanalMensageriaSMSRequest.getQuantidadeLotes() : 1);

            novaCampanhaMensageria.setIdPrioridadeMensagem(StatusPrioridadeMensagemEnum.BAIXA.getCodigoNumerico());

            novaCampanhaMensageria.setNomeEmissor(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getNomeSubjectus() != null ? pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getNomeSubjectus() : "NÃO INFORMADO");
            novaCampanhaMensageria.setNomeDepartamentoEmissor(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getNomeDepartamento() != null ? pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getNomeDepartamento() : "NÃO INFORMADO");

            novaCampanhaMensageria.setCanalMensageria("SMS");

            novaCampanhaMensageria = this.campanhaMensageriaService.salvarCampanha(novaCampanhaMensageria);

            try {
                List<PublicoAlvoCampanha> novosPublicoAlvoCampanhasDestaCampanha = this.publicoAlvoCampanhaService.criarPublicoAlvoCampanhaValidos(sistelcomDTO, pedidoDisparoCanalMensageriaSMSRequest, novaCampanhaMensageria);
                this.publicoAlvoCampanhaService.atualizarPublicoAlvoCampanhaInvalidosOuHigienizados(sistelcomDTO, pedidoDisparoCanalMensageriaSMSRequest, novaCampanhaMensageria, novosPublicoAlvoCampanhasDestaCampanha);
                novosPublicoAlvoCampanhasDestaCampanha = this.publicoAlvoCampanhaService.salvarPublicoAlvoCampanha(novosPublicoAlvoCampanhasDestaCampanha);

                novaCampanhaMensageria.setPublicosAlvoCampanha(novosPublicoAlvoCampanhasDestaCampanha);
                novaCampanhaMensageria = this.campanhaMensageriaService.salvarCampanha(novaCampanhaMensageria);

                estoqueMercadoriaVirtual.subtrair((long) novosPublicoAlvoCampanhasDestaCampanha.size());
                estoqueMercadoriaVirtual = this.estoqueMercadoriaVirtualService.atualizarEstoque(estoqueMercadoriaVirtual);

                this.dashboardService.atualizarDashboardPreparacaoCampanha(novaCampanhaMensageria, estoqueMercadoriaVirtual, pedidoDisparoCanalMensageriaSMSRequest.getIdCanalMensageria(), sistelcomDTO);

                byte[] pdfContents = this.geradorRelatorioService.generatePdfRelatorioEnvio(sistelcomDTO, estoqueMercadoriaVirtual, novaCampanhaMensageria, novosPublicoAlvoCampanhasDestaCampanha, pedidoDisparoCanalMensageriaSMSRequest.getIdCanalMensageria());
                byte[] validosExcelContents = this.geradorRelatorioService.generateExcelRelatorioValidos(sistelcomDTO);
                return this.geradorRelatorioService.gerarZipRelatorios(sistelcomDTO, pdfContents, validosExcelContents);

            } catch (Exception e) {
                System.err.println("Erro ao agendar envio de SMS em lote: " + e.getMessage());
                throw new RuntimeException("Falha ao processar agendamento de SMS em lote.", e);
            }

        } else {
            System.out.println("Nenhum contato válido para agendar o envio de SMS.");
            return this.geradorRelatorioService.gerarZipRelatorios(sistelcomDTO, null, null);
        }
    }

}
