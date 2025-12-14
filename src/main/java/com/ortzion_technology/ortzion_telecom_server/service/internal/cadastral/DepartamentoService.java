package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.google.protobuf.ServiceException; // Ou use uma exceção customizada
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroDepartamentoRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.DepartamentoDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.EmpresaRepository; // <-- NECESSÁRIO
import com.ortzion_technology.ortzion_telecom_server.security.repository.DepartamentoRepository;
import com.ortzion_technology.ortzion_telecom_server.security.vo.DepartamentoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpresaRepository empresaRepository;

    @Autowired
    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               EmpresaRepository empresaRepository) { // <-- INJETADO
        this.departamentoRepository = departamentoRepository;
        this.empresaRepository = empresaRepository;
    }

    public Departamento pegarDepartamentoPorId(Integer idDepartamento) throws ServiceException {
        Optional<Departamento> departamentoOptional = this.departamentoRepository.findByIdDepartamento(idDepartamento);

        if(departamentoOptional.isPresent()) {
            return departamentoOptional.get();
        }else{
            throw new ServiceException("Nenhum departamento foi achado");
        }

    }

    public String pegarDepartamentoPorIdCampanha(Integer idDepartamento){
        Optional<Departamento> departamentoOptional = this.departamentoRepository.findByIdDepartamento(idDepartamento);
        return departamentoOptional.map(Departamento::getNomeDepartamento).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<DepartamentoVO> pegarDepartamentosPorEmpresa(FiltroDepartamentoRequest request) throws ServiceException {
        Empresa empresa = empresaRepository.findById(request.getEmpresa().getIdEmpresa())
                .orElseThrow(() -> new ServiceException("Empresa não encontrada"));

        // Assumindo que Empresa tem um getDepartamentos()
        return converterParaVO(empresa.getDepartamentos());
    }

    public Departamento atribuirAoColaboradorDepartamento(DepartamentoDTO departamentoDTO) throws ServiceException {
        Optional<Departamento> departamentoOptional = this.departamentoRepository.findByIdDepartamento(departamentoDTO.getIdDepartamento());

        if(departamentoOptional.isPresent()) {
            Departamento departamento = departamentoOptional.get();

            departamento.setIdDepartamento(1);
            departamento.setCodigoDepartamento("DPP1");
            departamento.setNomeDepartamento("DEPARTAMENTO PESSOA FÍSICA");
            departamento = departamentoRepository.save(departamento);
            return departamento;

        }else{
            throw new ServiceException("Nenhum departamento foi achado");
        }

    }

    /**
     * Pega a LISTA MESTRA de todos os departamentos disponíveis no sistema.
     */
    @Transactional(readOnly = true)
    public List<DepartamentoVO> pegarDepartamentosMestres(FiltroDepartamentoRequest request) {
        // Opcional: filtrar a lista mestra se necessário
        List<Departamento> todosDepartamentos = departamentoRepository.findAll();
        return converterParaVO(todosDepartamentos);
    }

    /**
     * Associa um departamento (da lista mestra) a uma empresa.
     */
    @Transactional
    public List<DepartamentoVO> associarDepartamento(FiltroDepartamentoRequest request) throws ServiceException {
        Empresa empresa = empresaRepository.findById(request.getEmpresa().getIdEmpresa())
                .orElseThrow(() -> new ServiceException("Empresa não encontrada"));

        Departamento departamento = departamentoRepository.findById(request.getDepartamento().getIdDepartamento())
                .orElseThrow(() -> new ServiceException("Departamento não encontrado"));

        // Assumindo que Empresa tem um Set/List de Departamentos com @ManyToMany
        empresa.getDepartamentos().add(departamento);
        empresaRepository.save(empresa);

        // Retorna a nova lista de departamentos associados
        return converterParaVO(empresa.getDepartamentos());
    }

    /**
     * Desassocia um departamento de uma empresa.
     */
    @Transactional
    public List<DepartamentoVO> desassociarDepartamento(FiltroDepartamentoRequest request) throws ServiceException {
        Empresa empresa = empresaRepository.findById(request.getEmpresa().getIdEmpresa())
                .orElseThrow(() -> new ServiceException("Empresa não encontrada"));

        Departamento departamento = departamentoRepository.findById(request.getDepartamento().getIdDepartamento())
                .orElseThrow(() -> new ServiceException("Departamento não encontrado"));

        empresa.getDepartamentos().remove(departamento);
        empresaRepository.save(empresa);

        // Retorna a nova lista de departamentos associados
        return converterParaVO(empresa.getDepartamentos());
    }


    // --- MÉTODOS AUXILIARES DE CONVERSÃO ---

    private DepartamentoVO converterParaVO(Departamento d) {
        DepartamentoVO vo = new DepartamentoVO();
        vo.setIdDepartamento(d.getIdDepartamento());
        vo.setNomeDepartamento(d.getNomeDepartamento());
        vo.setCodigoDepartamento(d.getCodigoDepartamento());
        // Adicione outros campos se o VO tiver
        return vo;
    }

    private List<DepartamentoVO> converterParaVO(List<Departamento> departamentos) {
        return departamentos.stream()
                .filter(Objects::nonNull)
                .map(this::converterParaVO)
                .collect(Collectors.toList());
    }
}