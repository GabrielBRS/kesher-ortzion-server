package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.analytics;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.DashboardRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics.Dashboard;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.UserDetailsServiceImpl;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.analytics.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

   private final DashboardService dashboardService;
   private final SecurityService securityService;

    public DashboardController(DashboardService dashboardService, SecurityService securityService) {
        this.dashboardService = dashboardService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/pegar", method = RequestMethod.POST)
    public ResponseEntity<?> pegarDashboardMulticonta(
            @RequestBody DashboardRequest dashboardRequest) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, dashboardRequest.getMulticonta());

        Dashboard dashboardResponse = this.dashboardService.pegarDashboardMulticonta(dashboardRequest);

        return ResponseEntity.ok(dashboardResponse);
    }

}
