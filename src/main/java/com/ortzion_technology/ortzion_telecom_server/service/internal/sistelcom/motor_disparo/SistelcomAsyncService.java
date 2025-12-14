package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.motor_disparo;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPrioridadeMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.CampanhaMensageriaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SistelcomAsyncService {

    private static final int BATCH_SIZE = 100;

    private final DisparadorCampanhaService disparadorCampanhaService;
    private final CampanhaMensageriaService campanhaMensageriaService;

    public SistelcomAsyncService(DisparadorCampanhaService disparadorCampanhaService, CampanhaMensageriaService campanhaMensageriaService) {
        this.disparadorCampanhaService = disparadorCampanhaService;
        this.campanhaMensageriaService = campanhaMensageriaService;
    }

    @Scheduled(fixedRate = 5000)
    public void ProcessarMensagensPendentesPrioridadeAlta() {

        List<CampanhaMensageria> mensagensParaProcessar = this.campanhaMensageriaService
                .pegarCampanhasPendentesParaProcessarPrioridadeAlta(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico(), StatusPrioridadeMensagemEnum.ALTA.getCodigoNumerico(), BATCH_SIZE, LocalDateTime.now());

        for (CampanhaMensageria campanha : mensagensParaProcessar) {
            disparadorCampanhaService.processarCampanhaIndividualmentePrioridadeAlta(campanha);
        }

    }

    @Scheduled(fixedRate = 10000)
    public void ProcessarMensagensPendentesPrioridadeMedia() {

        List<CampanhaMensageria> mensagensParaProcessar = this.campanhaMensageriaService
                .pegarCampanhasPendentesParaProcessarPrioridadeMedia(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico(), StatusPrioridadeMensagemEnum.MEDIA.getCodigoNumerico(), BATCH_SIZE, LocalDateTime.now());

        for (CampanhaMensageria campanha : mensagensParaProcessar) {
            disparadorCampanhaService.processarCampanhaIndividualmentePrioridadeMedia(campanha);
        }

    }

    @Scheduled(fixedRate = 15000)
    public void ProcessarMensagensPendentesPrioridadeBaixa() {

        List<CampanhaMensageria> mensagensParaProcessar = this.campanhaMensageriaService
                .pegarCampanhasPendentesParaProcessarPrioridadeBaixa(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico(), StatusPrioridadeMensagemEnum.BAIXA.getCodigoNumerico(), BATCH_SIZE, LocalDateTime.now());

        for (CampanhaMensageria campanha : mensagensParaProcessar) {
            disparadorCampanhaService.processarCampanhaIndividualmentePriorIDADEBaixa(campanha);
        }

    }

}
