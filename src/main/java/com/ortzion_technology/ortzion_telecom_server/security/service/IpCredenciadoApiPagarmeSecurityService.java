package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.security.entity.IpCredenciadoApiPagarmeSecurity;
import com.ortzion_technology.ortzion_telecom_server.security.repository.IpCredenciadoApiPagarmeSecurityRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
public class IpCredenciadoApiPagarmeSecurityService {

    private static final Logger log = LoggerFactory.getLogger(IpCredenciadoApiPagarmeSecurityService.class);

    private final IpCredenciadoApiPagarmeSecurityRepository ipCredenciadoApiPagarmeSecurityRepository;

    private final Set<String> pagarmeIpWhitelist = new CopyOnWriteArraySet<>();

    public IpCredenciadoApiPagarmeSecurityService(IpCredenciadoApiPagarmeSecurityRepository ipCredenciadoApiPagarmeSecurityRepository) {
        this.ipCredenciadoApiPagarmeSecurityRepository = ipCredenciadoApiPagarmeSecurityRepository;
    }

    @PostConstruct
    public void carregarIpsNaInicializacao() {
        log.info("Iniciando carregamento inicial da whitelist de IPs Pagar.me...");
        this.recarregarIps();
    }

    public void recarregarIps() {
        List<String> novosIps = ipCredenciadoApiPagarmeSecurityRepository.findAllIpAddresses();

        pagarmeIpWhitelist.clear();
        pagarmeIpWhitelist.addAll(novosIps);

        log.info("Whitelist de IPs Pagar.me atualizada. Total de IPs carregados: {}", pagarmeIpWhitelist.size());
    }

    public Set<String> getPagarmeIpWhitelist() {
        return Collections.unmodifiableSet(pagarmeIpWhitelist);
    }

    @Scheduled(fixedDelay = 3600000)
    public void recarregarIpsAgendado() {
        this.recarregarIps();
    }

    public List<IpCredenciadoApiPagarmeSecurity> pegarTodosIpCredenciadoApiPagarmeSecurity (){
        return ipCredenciadoApiPagarmeSecurityRepository.findAll();
    }

    public void salvarTodos(List<IpCredenciadoApiPagarmeSecurity> ipsParaSalvar){
        ipCredenciadoApiPagarmeSecurityRepository.saveAll(ipsParaSalvar);
    }

    public void salvarIpSeNaoExistir(String ipString) {
        boolean exists = ipCredenciadoApiPagarmeSecurityRepository
                .findByIpCredenciadoApiPagarme(ipString)
                .isPresent();

        if (!exists) {
            IpCredenciadoApiPagarmeSecurity novoIp = new IpCredenciadoApiPagarmeSecurity(null, ipString);
            ipCredenciadoApiPagarmeSecurityRepository.save(novoIp);
            log.info("IP da Pagar.me persistido com sucesso: {}", ipString);
        }
    }

}
