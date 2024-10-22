package com.smirnov.services.security;

import com.smirnov.exception.ExtractCredentialsException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Извлекает учетные данные из заголовков для
 */
@Component
public class BasicAuth {

    private static final String BASIC_NAME = "Basic ";

    public String[] extractCredentials(String authHeader) {
        if (authHeader != null && authHeader.startsWith(BASIC_NAME)) {
            String base64Credentials = authHeader.substring(BASIC_NAME.length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            return credentials.split(":", 2);
        }
        throw new ExtractCredentialsException("Не удалось извлечь учетные данные");
    }
}
