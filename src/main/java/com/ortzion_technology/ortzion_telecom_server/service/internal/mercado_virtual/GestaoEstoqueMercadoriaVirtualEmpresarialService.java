package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics.Dashboard;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarialRepository;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.service.internal.analytics.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestaoEstoqueMercadoriaVirtualEmpresarialService {

    private final GestaoEstoqueMercadoriaVirtualEmpresarialRepository gestaoEstoqueMercadoriaVirtualEmpresarialRepository;
    private final CanalMensageriaService canalMensageriaService;
    private final DashboardService dashboardService;

    public GestaoEstoqueMercadoriaVirtualEmpresarialService(GestaoEstoqueMercadoriaVirtualEmpresarialRepository gestaoEstoqueMercadoriaVirtualEmpresarialRepository, CanalMensageriaService canalMensageriaService, DashboardService dashboardService) {
        this.gestaoEstoqueMercadoriaVirtualEmpresarialRepository = gestaoEstoqueMercadoriaVirtualEmpresarialRepository;
        this.canalMensageriaService = canalMensageriaService;
        this.dashboardService = dashboardService;
    }

    public List<GestaoEstoqueMercadoriaVirtualEmpresarial> atribuirGestaoEstoqueEmpresaAoColaborador(AcessoUsuario usuario, Pessoa pessoa, Empresa empresa, Departamento departamento, Colaborador colaborador) {

        List<GestaoEstoqueMercadoriaVirtualEmpresarial> gestaoEstoqueMercadoriaVirtualEmpresarialList = this.gestaoEstoqueMercadoriaVirtualEmpresarialRepository.buscarTodosPorIdEmpresa(empresa.getIdEmpresa());
        if(gestaoEstoqueMercadoriaVirtualEmpresarialList.isEmpty()){
            return gestaoEstoqueMercadoriaVirtualEmpresarialList;
        }

        List<CanalMensageria> canalMensagerias = this.canalMensageriaService.pegarTodosCanaisMensageria();
        if(canalMensagerias.isEmpty()){
            return gestaoEstoqueMercadoriaVirtualEmpresarialList;
        }

        for(CanalMensageria  canalMensageria : canalMensagerias) {

            GestaoEstoqueMercadoriaVirtualEmpresarial gestaoEstoqueMercadoriaVirtualEmpresarial = new GestaoEstoqueMercadoriaVirtualEmpresarial();

            gestaoEstoqueMercadoriaVirtualEmpresarial.setEmpresa(empresa);
            gestaoEstoqueMercadoriaVirtualEmpresarial.setDepartamento(departamento);
            gestaoEstoqueMercadoriaVirtualEmpresarial.setColaborador(colaborador);

            Dashboard dashboard = new Dashboard();
            dashboard.setTipoPessoa(2);
            dashboard.setIdSubjectus(empresa.getIdEmpresa());
            dashboard.setIdDepartamento(departamento.getIdDepartamento());
            dashboard.setIdColaborador(colaborador.getIdColaborador());
            dashboard.setIdCanalMensageria(canalMensageria.getIdCanalMensageria());
            dashboard.setDisponivel(0L);
            dashboard = this.dashboardService.salvarDashboard(dashboard);
        }

        return this.gestaoEstoqueMercadoriaVirtualEmpresarialRepository.saveAll(gestaoEstoqueMercadoriaVirtualEmpresarialList);
    }

}
