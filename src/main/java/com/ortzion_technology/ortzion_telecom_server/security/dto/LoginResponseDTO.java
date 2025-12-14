package com.ortzion_technology.ortzion_telecom_server.security.dto;

import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private List<MulticontaVO> contas;
}
