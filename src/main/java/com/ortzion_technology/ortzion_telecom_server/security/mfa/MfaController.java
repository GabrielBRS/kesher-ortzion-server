package com.ortzion_technology.ortzion_telecom_server.security.mfa;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mfa")
public class MfaController {

    private final MfaService mfaService;

    public MfaController(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @PostMapping("/setup")
    public ResponseEntity<MfaSetupDto> setupMfa(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        MfaSetupDto setupDto = mfaService.setupMfa(username);
        return ResponseEntity.ok(setupDto);
    }

    @PostMapping("/confirm")
    public ResponseEntity<MfaRecoveryCodesDto> confirmMfa(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MfaConfirmationDto confirmationDto) {

        String username = userDetails.getUsername();
        MfaRecoveryCodesDto result = mfaService.confirmMfa(username, confirmationDto.code());

        if (result.success()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}