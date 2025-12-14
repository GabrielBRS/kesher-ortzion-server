package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import com.ortzion_technology.ortzion_telecom_server.entity.dto.DashboardDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private DashboardDTO dashboardDTO;

}
