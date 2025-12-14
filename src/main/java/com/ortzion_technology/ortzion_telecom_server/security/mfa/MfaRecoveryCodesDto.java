package com.ortzion_technology.ortzion_telecom_server.security.mfa;

import java.util.List;

public record MfaRecoveryCodesDto(
        boolean success,
        List<String> recoveryCodes
) {}