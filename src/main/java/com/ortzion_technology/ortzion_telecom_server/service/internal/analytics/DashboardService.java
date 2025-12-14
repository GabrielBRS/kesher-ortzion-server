package com.ortzion_technology.ortzion_telecom_server.service.internal.analytics;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.DashboardRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal.DashboardResponse;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics.Dashboard;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.DashboardDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.analytics.DashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public Dashboard salvarDashboard(Dashboard dashboard) {
        return this.dashboardRepository.save(dashboard);
    }

    public Dashboard pegarDashboardMulticonta(DashboardRequest dashboardRequest) {
        return dashboardRepository.pegarDashboardMulticonta(
                dashboardRequest.getMulticonta().getTipoPessoa(),
                dashboardRequest.getMulticonta().getIdSubjectus(),
                dashboardRequest.getMulticonta().getIdDepartamento(),
                dashboardRequest.getIdCanalMensageria());
    }

    public Dashboard atualizarDashboard(Dashboard dashboard) {
        return this.dashboardRepository.save(dashboard);
    }

    @Transactional
    public void atualizarDashboardEnvioMensagem(CampanhaMensageria campanha) {
        Dashboard dashboard = this.dashboardRepository.pegarDashboardMulticonta(campanha.getTipoPessoa(), campanha.getIdSubjectus(), campanha.getIdDepartamento(), campanha.getIdCanalMensageria());

        Long entregue = Optional.ofNullable(dashboard.getEntregue()).orElse(0L);
        Long entregueAtual = (entregue + (campanha.getEntregue() != null ? campanha.getEntregue() : 0));
        dashboard.setEntregue(entregueAtual);

        Long naoEntregue = Optional.ofNullable(dashboard.getNaoEntregue()).orElse(0L);
        Long naoEntregueAtual = (naoEntregue + (campanha.getNaoEntregue() != null ? campanha.getNaoEntregue() : 0));
        dashboard.setNaoEntregue(naoEntregueAtual);

        this.dashboardRepository.save(dashboard);
    }

    @Transactional
    public void atualizarDashboardPreparacaoCampanha(CampanhaMensageria campanhaMensageria, EstoqueMercadoriaVirtual estoqueMercadoriaVirtual, Integer idCanalMensageria, SistelcomDTO sistelcomDTO) {

        Dashboard dashboard = this.dashboardRepository.pegarDashboardMulticonta(campanhaMensageria.getTipoPessoa(), campanhaMensageria.getIdSubjectus(), campanhaMensageria.getIdDepartamento(), idCanalMensageria);

        Long solicitados = Optional.ofNullable(dashboard.getSolicitado()).orElse(0L);
        Long solicitadosAtual = (solicitados + sistelcomDTO.getTotal());
        dashboard.setSolicitado(solicitadosAtual);

        Long validos = Optional.ofNullable(dashboard.getValidos()).orElse(0L);
        Long validosAtual = (validos + sistelcomDTO.getValidos());
        dashboard.setValidos(validosAtual);

        Long invalidos = Optional.ofNullable(dashboard.getInvalidos()).orElse(0L);
        Long invalidosAtual = (invalidos + sistelcomDTO.getInvalidos());
        dashboard.setInvalidos(invalidosAtual);

        Long enviados = Optional.ofNullable(dashboard.getEnviados()).orElse(0L);
        Long enviadosAtual = (enviados + sistelcomDTO.getValidos());
        dashboard.setEnviados(enviadosAtual);

        Long consumido = Optional.ofNullable(dashboard.getConsumido()).orElse(0L);
        Long consumidoAtual = (consumido + sistelcomDTO.getValidos());
        dashboard.setConsumido(consumidoAtual);

        Long disponivel = Optional.ofNullable(dashboard.getDisponivel()).orElse(0L);
        Long disponivelAtual = (disponivel - sistelcomDTO.getValidos());
        dashboard.setDisponivel(disponivelAtual);

        Long higienizado = Optional.ofNullable(dashboard.getHigienizado()).orElse(0L);
        Long higienizadoAtual = (higienizado - sistelcomDTO.getHigienizados());
        dashboard.setHigienizado(higienizadoAtual);

        dashboard = this.dashboardRepository.save(dashboard);

    }

}
