package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CampanhaMensageriaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.CampanhaMensageriaDAORepositoryImpl;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.sistelcom.CampanhaMensageriaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampanhaMensageriaService {

    private final CampanhaMensageriaRepository campanhaMensageriaRepository;
    private final CampanhaMensageriaDAORepositoryImpl campanhaMensageriaDAORepository;

    public CampanhaMensageriaService(CampanhaMensageriaRepository campanhaMensageriaRepository, CampanhaMensageriaDAORepositoryImpl campanhaMensageriaDAORepository) {
        this.campanhaMensageriaRepository = campanhaMensageriaRepository;
        this.campanhaMensageriaDAORepository = campanhaMensageriaDAORepository;
    }

    public Page<CampanhaMensageria> pegarCampanhasPorMulticonta(CampanhaMensageriaRequest campanhaMensageriaRequest, AcessoUsuario usuario) {

        Pageable pageable = PageRequest.of(
                campanhaMensageriaRequest.getPaginavel().getPagina(),
                campanhaMensageriaRequest.getPaginavel().getTotal()
        );

        return this.campanhaMensageriaDAORepository.pegarCampanhasPorMulticonta(campanhaMensageriaRequest.getMulticontaRequestDTO().getIdUsuario(), campanhaMensageriaRequest.getMulticontaRequestDTO().getTipoPessoa(), campanhaMensageriaRequest.getMulticontaRequestDTO().getIdSubjectus(), campanhaMensageriaRequest.getMulticontaRequestDTO().getIdDepartamento(), campanhaMensageriaRequest.getDataHoraInicio(), campanhaMensageriaRequest.getDataHoraFim(), pageable);
    }

    public CampanhaMensageria salvarCampanha(CampanhaMensageria campanhaMensageria){
        return campanhaMensageriaRepository.save(campanhaMensageria);
    }

    @Transactional
    public List<CampanhaMensageria> pegarCampanhasPendentesParaProcessarPrioridadeAlta(Integer idStatusCampanha, Integer statusPrioridadeMensagem ,Integer batchSize, LocalDateTime dataAtual) {
        Pageable pageable = PageRequest.of(0, batchSize);

        List<Long> idsParaProcessar = this.campanhaMensageriaRepository.pegarIdsCampanhasPendentesPrioridadeAlta(
                idStatusCampanha, statusPrioridadeMensagem, dataAtual, pageable
        );

        if (idsParaProcessar.isEmpty()) {
            return new ArrayList<>();
        }

        List<CampanhaMensageria> campanhaMensagerias = this.campanhaMensageriaRepository.pegarCampanhasComPublicoAlvoPorIds(idsParaProcessar);

        for(CampanhaMensageria campanhaMensageria : campanhaMensagerias) {
            campanhaMensageria.setIdStatusCampanha(StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico());
        }

        return this.campanhaMensageriaRepository.saveAll(campanhaMensagerias);
    }

    @Transactional
    public List<CampanhaMensageria> pegarCampanhasPendentesParaProcessarPrioridadeMedia(Integer idStatusCampanha, Integer statusPrioridadeMensagem ,Integer batchSize, LocalDateTime dataAtual) {
        Pageable pageable = PageRequest.of(0, batchSize);

        List<Long> idsParaProcessar = this.campanhaMensageriaRepository.pegarIdsCampanhasPendentesPrioridadeMedia(
                idStatusCampanha, statusPrioridadeMensagem, dataAtual, pageable
        );

        if (idsParaProcessar.isEmpty()) {
            return new ArrayList<>();
        }

        List<CampanhaMensageria> campanhaMensagerias = this.campanhaMensageriaRepository.pegarCampanhasComPublicoAlvoPorIds(idsParaProcessar);

        for(CampanhaMensageria campanhaMensageria : campanhaMensagerias) {
            campanhaMensageria.setIdStatusCampanha(StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico());
        }

        return this.campanhaMensageriaRepository.saveAll(campanhaMensagerias);
    }

    @Transactional
    public List<CampanhaMensageria> pegarCampanhasPendentesParaProcessarPrioridadeBaixa(Integer idStatusCampanha, Integer statusPrioridadeMensagem , Integer batchSize, LocalDateTime dataAtual) {
        Pageable pageable = PageRequest.of(0, batchSize);

        List<Long> idsParaProcessar = this.campanhaMensageriaRepository.pegarIdsCampanhasPendentesPrioridadeBaixa(
                idStatusCampanha, statusPrioridadeMensagem, dataAtual, pageable
        );

        if (idsParaProcessar.isEmpty()) {
            return new ArrayList<>();
        }

        List<CampanhaMensageria> campanhaMensagerias = this.campanhaMensageriaRepository.pegarCampanhasComPublicoAlvoPorIds(idsParaProcessar);

        for(CampanhaMensageria campanhaMensageria : campanhaMensagerias) {
            campanhaMensageria.setIdStatusCampanha(StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico());
        }

        return this.campanhaMensageriaRepository.saveAll(campanhaMensagerias);
    }

    public CampanhaMensageria atualizarCampanha(CampanhaMensageria campanhaMensageria) {
        return campanhaMensageriaRepository.save(campanhaMensageria);
    }

}