package com.ortzion_technology.ortzion_telecom_server.configuration.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.PacoteCanalMensageriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MercadoVirtualCacheService {

    private final PacoteCanalMensageriaRepository pacoteCanalMensageriaRepository;

    private final Map<Integer, PacoteCanalMensageria> cache = new ConcurrentHashMap<>();

    public MercadoVirtualCacheService(PacoteCanalMensageriaRepository pacoteCanalMensageriaRepository) {
        this.pacoteCanalMensageriaRepository = pacoteCanalMensageriaRepository;
    }

    @PostConstruct
    public void carregarCache() {
        List<PacoteCanalMensageria> lista = pacoteCanalMensageriaRepository.findAll();
        cache.clear();
        for (PacoteCanalMensageria p : lista) {
            cache.put(p.getIdPacoteCanalMensageria(), p);
        }
        System.out.println("CACHE: " + cache.size() + " pacotes carregados na memória.");
    }

    @Scheduled(fixedRate = 600000)
    public void atualizarCache() {
        carregarCache();
    }

    public PacoteCanalMensageria getPacote(Integer id) {
        return cache.get(id);
    }


}
