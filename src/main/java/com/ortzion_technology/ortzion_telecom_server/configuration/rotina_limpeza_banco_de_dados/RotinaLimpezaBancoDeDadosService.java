package com.ortzion_technology.ortzion_telecom_server.configuration.rotina_limpeza_banco_de_dados;

import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPagamentoEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.CompraRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class RotinaLimpezaBancoDeDadosService {

    private static final Logger log = LoggerFactory.getLogger(RotinaLimpezaBancoDeDadosService.class);
    private final CompraRepository compraRepository;

    public RotinaLimpezaBancoDeDadosService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void limparComprasAbandonadas() {
        log.info("Iniciando limpeza de compras abandonadas...");

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(3);

        int deletados = compraRepository.deletarComprasAntigasNaoPagas(
                dataLimite,
                StatusPagamentoEnum.PROCESSANDO.getCodigoNumerico()
        );

        log.info("Limpeza concluída. {} compras abandonadas (Pendente/Processando) foram removidas.", deletados);
    }

}
