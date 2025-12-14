package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.motor_disparo;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.PublicoAlvoCampanha;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.service.internal.analytics.DashboardService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.PublicoAlvoCampanhaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.CampanhaMensageriaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.sms.EnvioSMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisparadorCampanhaService {

    private static final Logger logger = LoggerFactory.getLogger(DisparadorCampanhaService.class);

    private final CampanhaMensageriaService campanhaMensageriaService;
    private final PublicoAlvoCampanhaService publicoAlvoCampanhaService;
    private final EnvioSMSService envioSMSService;
    private final DashboardService dashboardService;

    private static final int TAMANHO_LOTE_ALVOS = 200;

    public DisparadorCampanhaService(
            CampanhaMensageriaService campanhaMensageriaService,
            PublicoAlvoCampanhaService publicoAlvoCampanhaService,
            EnvioSMSService envioSMSService, DashboardService dashboardService
    ) {
        this.campanhaMensageriaService = campanhaMensageriaService;
        this.publicoAlvoCampanhaService = publicoAlvoCampanhaService;
        this.envioSMSService = envioSMSService;
        this.dashboardService = dashboardService;
    }

    private void processarCampanha(CampanhaMensageria campanha) throws ServiceException {
        int numeroPagina = 0;
        List<PublicoAlvoCampanha> loteDeAlvos;
        boolean houveProcessamentoDeLote = false;

        do {
            Pageable pageable = PageRequest.of(numeroPagina, TAMANHO_LOTE_ALVOS);
            loteDeAlvos = this.publicoAlvoCampanhaService.pegarPublicoAlvoCampanhaPendentesParaProcessar(campanha, pageable);

            if (!loteDeAlvos.isEmpty()) {
                houveProcessamentoDeLote = true;

                switch (campanha.getIdCanalMensageria()) {
                    case 1:
                        this.envioSMSService.enviarSMSEmLote(loteDeAlvos, campanha);
                        atualizarStatusPublicoAlvoSucesso(loteDeAlvos);
                        break;
                    default:
                        throw new UnsupportedOperationException("Canal de mensageria não suportado: " + campanha.getIdCanalMensageria());
                }
            }

            numeroPagina++;

        } while (!loteDeAlvos.isEmpty());
        if (houveProcessamentoDeLote) {
            atualizarStatusCampanhaSucesso(campanha);
            this.dashboardService.atualizarDashboardEnvioMensagem(campanha);
        }
        else {
            logger.info("Campanha {} concluída sem alvos pendentes para processar.", campanha.getIdCampanhaMensageria());
            atualizarStatusCampanhaSucesso(campanha);
        }
    }

    @Async("taskExecutorSMS")
    public void processarCampanhaIndividualmentePrioridadeAlta(CampanhaMensageria campanha) {
        try {
            processarCampanha(campanha);
        } catch (Exception e) {
            logger.error("Falha CRÍTICA ao processar campanha {}: {}", campanha.getIdCampanhaMensageria(), e.getMessage(), e);
            atualizarStatusCampanhaFalha(campanha);
            this.publicoAlvoCampanhaService.marcarPendentesComoFalha(campanha.getIdCampanhaMensageria());
        }
    }

    @Async("taskExecutorSMS")
    public void processarCampanhaIndividualmentePrioridadeMedia(CampanhaMensageria campanha) {
        try {
            processarCampanha(campanha);
        } catch (Exception e) {
            logger.error("Falha CRÍTICA ao processar campanha {}: {}", campanha.getIdCampanhaMensageria(), e.getMessage(), e);
            atualizarStatusCampanhaFalha(campanha);
            this.publicoAlvoCampanhaService.marcarPendentesComoFalha(campanha.getIdCampanhaMensageria());
        }
    }

    @Async("taskExecutorSMS")
    public void processarCampanhaIndividualmentePriorIDADEBaixa(CampanhaMensageria campanha) {
        try {
            processarCampanha(campanha);
        } catch (Exception e) {
            logger.error("Falha CRÍTICA ao processar campanha {}: {}", campanha.getIdCampanhaMensageria(), e.getMessage(), e);
            atualizarStatusCampanhaFalha(campanha);
            this.publicoAlvoCampanhaService.marcarPendentesComoFalha(campanha.getIdCampanhaMensageria());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarStatusCampanhaFalha(CampanhaMensageria campanha) {
        campanha.setIdStatusCampanha(StatusEnvioMensagemEnum.FALHOU.getCodigoNumerico());
        campanha.setDataEnvio(LocalDateTime.now());
        this.campanhaMensageriaService.atualizarCampanha(campanha);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarStatusPublicoAlvoFalha(List<PublicoAlvoCampanha> listaPublicoAlvoCampanha) {
        logger.warn("CHAMADA DE MÉTODO DEPRECADO: atualizarStatusPublicoAlvoFalha foi chamado. Considere usar marcarPendentesComoFalha(id) para melhor performance.");
        List<PublicoAlvoCampanha> alvosParaFalhar = listaPublicoAlvoCampanha.stream()
                .filter(pac -> pac.getIdStatusMensagem().equals(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico()) ||
                        pac.getIdStatusMensagem().equals(StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico()))
                .collect(Collectors.toList());

        alvosParaFalhar.forEach(pac -> {
            pac.setIdStatusMensagem(StatusEnvioMensagemEnum.FALHOU.getCodigoNumerico());
            pac.setDataEnvio(LocalDateTime.now());
            pac.setStatus("FALHOU");
        });

        if (!alvosParaFalhar.isEmpty()) {
            this.publicoAlvoCampanhaService.salvarPublicoAlvoCampanha(alvosParaFalhar);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarStatusCampanhaSucesso(CampanhaMensageria campanha) {
        campanha.setIdStatusCampanha(StatusEnvioMensagemEnum.ENTREGUE.getCodigoNumerico());
        campanha.setDataEnvio(LocalDateTime.now());
        this.campanhaMensageriaService.atualizarCampanha(campanha);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarStatusPublicoAlvoSucesso(List<PublicoAlvoCampanha> loteDeAlvos) {
        this.publicoAlvoCampanhaService.atualizarPublicoAlvoCampanha(loteDeAlvos);
    }
}