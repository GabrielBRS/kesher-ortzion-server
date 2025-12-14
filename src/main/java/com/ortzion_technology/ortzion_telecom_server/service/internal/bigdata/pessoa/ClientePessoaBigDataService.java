package com.ortzion_technology.ortzion_telecom_server.service.internal.bigdata.pessoa;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizarClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastrarClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa.ClientePessoaBigData;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.*;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.ClientePessoaBigDataDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EmpresaDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.FiltroClientePessoaBigDataDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.ClientePessoaBigDataDAORepositoryImpl;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.bigdata.pessoa.ClientePessoaBigDataRepository;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.repository.*;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.EmpresaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientePessoaBigDataService {

    private final ClientePessoaBigDataRepository clientePessoaBigDataRepository;
    private final ClientePessoaBigDataDAORepositoryImpl clientePessoaBigDataDAO;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MulticontaRepository multicontaRepository;
    private final EmpresaService empresaService;

    @Autowired
    public ClientePessoaBigDataService(ClientePessoaBigDataRepository clientePessoaBigDataRepository, ClientePessoaBigDataDAORepositoryImpl clientePessoaBigDataDAORepository, BCryptPasswordEncoder bCryptPasswordEncoder, MulticontaRepository multicontaRepository, EmpresaService empresaService) {
        this.clientePessoaBigDataRepository = clientePessoaBigDataRepository;
        this.clientePessoaBigDataDAO = clientePessoaBigDataDAORepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.multicontaRepository = multicontaRepository;
        this.empresaService = empresaService;
    }

    public List<ClientePessoaBigData> pegarClientePessoaBigData(FiltroClientePessoaBigDataRequest filtroClientePessoaBigData){
        return clientePessoaBigDataDAO.pegarClientePessoaBigData(filtroClientePessoaBigData);
    }

    public void cadastrarPessoa(CadastrarClientePessoaBigDataRequest cadastrarRequest) {
        List<ClientePessoaBigDataDTO> dtoList = cadastrarRequest.getClientePessoa();
        EmpresaDTO empresaDTO = cadastrarRequest.getEmpresa();

        if (dtoList == null || dtoList.isEmpty() || empresaDTO == null) {
            return;
        }

        Set<String> telefones = dtoList.stream().map(ClientePessoaBigDataDTO::getTelefoneCompletoDestinatario).collect(Collectors.toSet());
        Set<String> emails = dtoList.stream().map(ClientePessoaBigDataDTO::getEmailDestinatario).collect(Collectors.toSet());
        Set<String> documentos = dtoList.stream().map(ClientePessoaBigDataDTO::getDocumentoDestinatario).collect(Collectors.toSet());

        List<ClientePessoaBigData> existentes = clientePessoaBigDataRepository
                .buscarExistentesPorIdentificadores(telefones, emails, documentos);

        Map<String, ClientePessoaBigData> existentesPorTelefone = existentes.stream()
                .collect(Collectors.toMap(ClientePessoaBigData::getTelefoneCompletoCliente, Function.identity(), (e1, e2) -> e1));
        Map<String, ClientePessoaBigData> existentesPorEmail = existentes.stream()
                .collect(Collectors.toMap(ClientePessoaBigData::getEmailCliente, Function.identity(), (e1, e2) -> e1));
        Map<String, ClientePessoaBigData> existentesPorDocumento = existentes.stream()
                .collect(Collectors.toMap(ClientePessoaBigData::getDocumentoCliente, Function.identity(), (e1, e2) -> e1));

        List<ClientePessoaBigData> paraSalvar = new ArrayList<>();
        Set<Long> idsProcessados = new HashSet<>();

        for (ClientePessoaBigDataDTO dto : dtoList) {
            ClientePessoaBigData entidade = existentesPorTelefone.get(dto.getTelefoneCompletoDestinatario());
            if (entidade == null) entidade = existentesPorEmail.get(dto.getEmailDestinatario());
            if (entidade == null) entidade = existentesPorDocumento.get(dto.getDocumentoDestinatario());

            if (entidade != null) {
                if (!idsProcessados.contains(entidade.getIdClientePessoaBigData())) {
                    updateEntityFromDto(entidade, dto, empresaDTO);
                    paraSalvar.add(entidade);
                    idsProcessados.add(entidade.getIdClientePessoaBigData());
                }
            } else {
                ClientePessoaBigData novaEntidade = new ClientePessoaBigData();
                updateEntityFromDto(novaEntidade, dto, empresaDTO);
                paraSalvar.add(novaEntidade);
            }
        }

        if (!paraSalvar.isEmpty()) {
            this.clientePessoaBigDataRepository.saveAll(paraSalvar);
        }

    }

    public void atualizarClientePessoaBigData(AtualizarClientePessoaBigDataRequest atualizarRequest) {
        FiltroClientePessoaBigDataDTO filtro = atualizarRequest.getFiltro();
        ClientePessoaBigDataDTO novosDadosDto = atualizarRequest.getClientePessoa();

        if (filtro == null || novosDadosDto == null) {
            throw new IllegalArgumentException("Filtro e dados para atualização são obrigatórios.");
        }

        List<ClientePessoaBigData> entidadesParaAtualizar = clientePessoaBigDataDAO.buscarPorFiltroDinamico(filtro);

        if (entidadesParaAtualizar.isEmpty()) {
            return;
        }

        for (ClientePessoaBigData entidade : entidadesParaAtualizar) {
            updateEntityFromDto(entidade, novosDadosDto, atualizarRequest.getEmpresa());
        }

        this.clientePessoaBigDataRepository.saveAll(entidadesParaAtualizar);
    }

    private void updateEntityFromDto(ClientePessoaBigData entidade, ClientePessoaBigDataDTO dto, EmpresaDTO empresaDTO) {
        entidade.setTipoPessoa(2);
        entidade.setIdSubjectus(empresaDTO.getIdEmpresa());
        entidade.setTelefoneCompletoCliente(dto.getTelefoneCompletoDestinatario());
        entidade.setEmailCliente(dto.getEmailDestinatario());
        entidade.setDocumentoCliente(dto.getDocumentoDestinatario());
        entidade.setNomeCliente(dto.getNomeDestinatario());
        entidade.setSobrenomeCliente(dto.getSobrenomeDestinatario());
        entidade.setSexoCliente(dto.getSexoDestinatario());
        entidade.setNaturalidadeCliente(dto.getNaturalidadeDestinatario());
        entidade.setNacionalidadeCliente(dto.getNacionalidadeDestinatario());
        entidade.setDataNascimentoCliente(dto.getDataNascimentoDestinatario());
        entidade.setNomePaiCliente(dto.getNomePaiDestinatario());
        entidade.setNomeCliente(dto.getNomeMaeDestinatario());
        entidade.setCepCliente(dto.getCepDestinatario());
        entidade.setLogradouroCliente(dto.getLogradouroDestinatario());
        entidade.setNumeroCliente(dto.getNumeroDestinatario());
        entidade.setComplementoCliente(dto.getComplementoDestinatario());
        entidade.setBairroCliente(dto.getBairroDestinatario());
        entidade.setCidadeCliente(dto.getCidadeDestinatario());
        entidade.setEstadoCliente(dto.getEstadoDestinatario());
        entidade.setIdControladoraDados(dto.getIdControladoraDados());
        entidade.setIdEncarregadoDados(dto.getIdEncarregadoDados());
        entidade.setIdOperadoraDados(dto.getIdOperadoraDados());
        entidade.setAutorizacaoPosseDados(dto.getAutorizacaoPosseDados());
        entidade.setAutorizacaoPosseDadosTerceiros(dto.getAutorizacaoPosseDadosTerceiros());
    }

}

