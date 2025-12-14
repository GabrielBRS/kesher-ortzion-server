package com.ortzion_technology.ortzion_telecom_server.security.mfa;

public record MfaSetupDto(
        String manualEntryKey,
        String qrCodeImageBase64
) {}