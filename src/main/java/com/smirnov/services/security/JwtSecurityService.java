package com.smirnov.services.security;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.smirnov.configuration.JWTConfiguration;
import com.smirnov.dto.get.UserDetailsCustom;
import com.smirnov.enums.UserRight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Сервисный слой для генерации токена/
 */
@Service
public class JwtSecurityService {

    private final JWTConfiguration jwtConfiguration;

    /**
     * Время жизни токена.
     */
    private final long jwtSecretExpiration;

    public JwtSecurityService(JWTConfiguration jwtConfiguration, @Value("${security.jwtSecretExpiration}") long jwtSecretExpiration) {
        this.jwtConfiguration = jwtConfiguration;
        this.jwtSecretExpiration = jwtSecretExpiration;
    }

    /**
     * Генерирует токен для обновления
     *
     * @return Имя токена
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Возвращает сгенерированный токен
     *
     * @param user Пользователь в контексте Spring
     * @return Токен
     * @throws JOSEException
     */
    public String generateToken(UserDetailsCustom user) throws JOSEException {
        JWEObject jweObject = new JWEObject(
                new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256),
                new Payload(
                        new JWTClaimsSet.Builder()
                                .subject(user.getUsername()) // идентификатор пользователя subject
                                .claim("id", user.getId().toString())
                                .claim("roles", user.getUserRight().toString())
                                .issueTime(new Date()) // время выдачи токена
                                .expirationTime(new Date(System.currentTimeMillis() + jwtSecretExpiration * 10000))
                                .build().toJSONObject()));
        jweObject.encrypt(jwtConfiguration.encrypt());
        return jweObject.serialize();
    }

    /**
     * Извлекает токен.
     *
     * @param token JW-токен
     * @return
     * @throws BadJOSEException
     * @throws ParseException
     * @throws JOSEException
     */
    private JWTClaimsSet extractClaims(String token) throws BadJOSEException, ParseException, JOSEException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = jwtConfiguration.jwtProcessor();
        return jwtProcessor.process(token, null);
    }

    /**
     * Извлекает логин пользователя из токена
     *
     * @param token Токен
     * @return Логин
     * @throws BadJOSEException
     * @throws ParseException
     * @throws JOSEException
     */
    public String getSubject(String token) throws BadJOSEException, ParseException, JOSEException {
        JWTClaimsSet claims = extractClaims(token);
        return claims.getSubject();
    }

    /**
     * Проверяет токен на валидность.
     *
     * @param token       Токен
     * @param userDetails Пользователь в контексте Spring
     * @return true/false, если токен валидный/не валидный
     * @throws BadJOSEException
     * @throws ParseException
     * @throws JOSEException
     */
    public boolean isTokenValid(String token, UserDetails userDetails) throws BadJOSEException, ParseException, JOSEException {
        String userName = getSubject(token);
        Date expiration = extractClaims(token).getExpirationTime();
        return userName.equals(userDetails.getUsername()) && expiration.after(new Date());
    }
}
